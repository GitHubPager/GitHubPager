package com.wind.github;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GsonUtils;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.google.gson.Gson;
import com.wind.github.page.ArticleEntry;
import com.wind.github.page.ArticleSet;
import com.wind.github.page.Settings;
import com.wind.github.page.Template;
import com.wind.utils.FileUtils;
import com.wind.utils.ZipUtils;

public class PageManager {
	
	private static Logger logger=LoggerFactory.getLogger(PageManager.class);
	private String templateRepository;
	
	/*
	 * Create a auto generate commit message
	 */
	private String generateCommitMessage()
	{
		return GitHubConstants.COMMITMESSAGE+new Date().toString();
	}
	/*
	 * 
	 * Get User Basic Info.
	 */
	@Cacheable(value = "github",key="#accessToken + 'userInfo'")
	public User getBasicUserInfo(String accessToken) throws Exception
	{
		UserService uService=new UserService();
		uService.getClient().setOAuth2Token(accessToken);
		return uService.getUser();
	}
	
	public boolean checkVerifedEmail(String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		GitHubRequest request=new GitHubRequest();
		request.setType(List.class);
		request.setUri("/user/emails");
		String result=FileUtils.dumpInputStreamIntoString(client.getStream(request),GitHubConstants.UTF8ENCODING);
		System.out.println(result);
		if(result.contains("\"verified\":true"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/*
	 * Get A Repository From Name
	 */
	public Repository getStubRepository(User u,String repoName)
	{
		Repository repo=new Repository();
		repo.setOwner(u);
		repo.setName(repoName);
		return repo;
	}
	
	/*
	 * Get Branch of Repository. You can check whether the repository is empty
	 */
	
	private List<Reference> getRepositoryRefs(Repository repo,String accessToken) throws Exception
	{
		DataService dService=new DataService();
		dService.getClient().setOAuth2Token(accessToken);
		try
		{
			return dService.getReferences(repo);
		}
		catch(IOException e)
		{
			if(e.getMessage().contains("409"))
			{
				return new ArrayList<Reference>();
			}
			else
			{
				throw e;
			}
		}
	}
	
	/*
	 * Return User's all Repositories
	 */
	@Cacheable(value = "github",key="#accessToken + 'repository'")
	public List<Repository> getUserRepositories(String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		return rService.getRepositories();
	}
	
	/*
	 * Delete repository
	 */
	private void deleteRepository(Repository repo,String accessToken) throws Exception
	{
		GitHubClient client=new GitHubClient();
		client.setOAuth2Token(accessToken);
		logger.warn("Try to delete repo {}",repo.generateId());
		client.delete("/repos/"+repo.generateId());
	}
	
	/*
	 * Create Repository
	 */
	private Repository createRepository(String repoName,String accessToken) throws Exception
	{
		GitHubClient client=new GitHubClient();
		client.setOAuth2Token(accessToken);
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("name", repoName);
		params.put("auto_init", true);
		logger.info("Try to create repo {}",repoName);
		return (Repository)client.post("/user/repos",params,Repository.class);
	}
	
	/*
	 * Create Branch
	 */
	
	private void createRepositoryBranch(Repository repo,String refString,String accessToken) throws Exception
	{
		DataService dService=new DataService();
		dService.getClient().setOAuth2Token(accessToken);
		List<Reference> refList=dService.getReferences(repo);
		Reference ref=refList.get(0);
		ref.setRef(refString);
		logger.info("Try to create branch {}",refString);
		dService.createReference(repo, ref);
	}
	
	/*
	 * Get Repository By Name
	 */
	public Repository getRepositoryByName(String repoName,User u,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		Repository repo=new Repository();
		repo.setName(repoName);
		repo.setOwner(u);
		repo=rService.getRepository(repo);
		return repo;
	}
	
	/*
	 * Get A Raw file from repository with raw content service
	 */
	public String getRawFileFromRepository(Repository repo, String path, String ref,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService rService=new RawContentsService(client);
		logger.info("Try to get file using raw content service");
		return rService.getRawFileAsString(repo, path, ref, GitHubConstants.UTF8ENCODING);
	}
	
	/*
	 * Get A Base64Encoded File
	 */
	public RepositoryContents getFileFromRepository(Repository repo, String path, String ref,String accessToken) throws Exception
	{
		
		ContentsService rService=new ContentsService();
		rService.getClient().setOAuth2Token(accessToken);
		logger.info("Try to get file using common content service");
		try
		{
			List<RepositoryContents> cList= rService.getContents(repo,path,ref);
			return cList.get(0);
		}
		catch(IOException e)
		{
			if(e.getMessage().contains("404"))
			{
				return null;
			}
			throw e;
		}
	}
	
	/*
	 * Create A File
	 */
	public void createFileInRepository(Repository repo, String path, String ref,String content,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService rService=new RawContentsService(client);
		rService.createFile(repo, path, ref, this.generateCommitMessage(), content);
	}
	
	/*
	 * Get A file from Repositroy with raw data service
	 */
	/*public String getFileFromRepositroyWithRawDataService(Repository repo, String path, String refString,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawDataService dService=new RawDataService(client);
		logger.info("Try to get file using raw data service");
		Reference ref=dService.getReference(repo,refString);
		String commitSHA=ref.getObject().getSha();
		Commit commit=dService.getCommit(repo, commitSHA);
		String treeSHA=commit.getTree().getSha();
		List<TreeEntry> treeArray=dService.getTree(repo, treeSHA).getTree();
		String sha=findBlobOrTreeFromRepository(treeArray,repo,path,refString,dService,TreeEntry.TYPE_BLOB);
		if(sha==null) return null;
		return dService.getRawBlobAsString(repo, sha, UTF8ENCODING);
	}*/
	
	
	
	/*
	 * Search a repository for file or tree
	 */
	/*private String findBlobOrTreeFromRepository(List<TreeEntry> baseTree,Repository repo, String path, String refString, DataService dService, String type) throws Exception
	{
		String []pathList={path};
		if(path.contains("/"))
		{
			pathList=path.split("/");
		}
		List<TreeEntry> forTreeArray=baseTree;
		String sha=null;
		int i=1;
		for(i=0;i<pathList.length;i++)
		{
			boolean findFlag=false;
			if(pathList[i].isEmpty()) continue;
			for(TreeEntry entry:forTreeArray)
			{
				
				if(entry.getPath().equals(pathList[i]))
				{
					if(i!=pathList.length-1)
					{
						if(entry.getType().equals(TreeEntry.TYPE_TREE))
						{
							forTreeArray=dService.getTree(repo, entry.getSha()).getTree();
						}
						else continue;
					}
					else
					{
						
						if(entry.getType().equals(type))
						{
						
							sha=entry.getSha();
						}
						else continue;
					}
					findFlag=true;
					break;
				}
			}
			if(!findFlag)
			{
				return null;
			}
		}
		
		return sha;
	}
	
	
	/*
	 * Modify a file with raw content service with sha input.Recommended Method
	 */
	
	public void modifyFileInRepository(Repository repo, String path, String refString, String content,String sha,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService cService=new RawContentsService(client);
		cService.updateFile(repo, path, refString, sha, generateCommitMessage(), content);
	}
	
	/*
	 * Delete a file with sha. Recommended Method
	 */
	public void deleteFileInRepository(Repository repo, String path, String refString,String sha,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService cService=new RawContentsService(client);
		cService.deleteFile(repo, path, refString, sha, generateCommitMessage());
	}
	
	/*
	 * Modify a file with raw data service
	 */
	/*public void modifyFileInRepositoryWithoutSHA(Repository repo, String path, String refString, String content,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawDataService dService=new RawDataService(client);
		RawContentsService cService=new RawContentsService(client);
		Reference ref=dService.getReference(repo,refString);
		String commitSHA=ref.getObject().getSha();
		Commit commit=dService.getCommit(repo, commitSHA);
		String treeSHA=commit.getTree().getSha();
		List<TreeEntry> treeArray=dService.getTree(repo, treeSHA).getTree();
		String sha=findBlobOrTreeFromRepository(treeArray,repo,path,refString,dService,TreeEntry.TYPE_BLOB);
		if(sha==null) throw new Exception("404 File Not Found");
		cService.updateFile(repo, path, refString, sha, generateCommitMessage(), content);
	}
	
	/*
	 * Delete a file
	 */
	/*public void deleteFileInRepositoryWithoutSHA(Repository repo, String path, String refString, String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawDataService dService=new RawDataService(client);
		RawContentsService cService=new RawContentsService(client);
		Reference ref=dService.getReference(repo,refString);
		String commitSHA=ref.getObject().getSha();
		Commit commit=dService.getCommit(repo, commitSHA);
		String treeSHA=commit.getTree().getSha();
		List<TreeEntry> treeArray=dService.getTree(repo, treeSHA).getTree();
		String sha=findBlobOrTreeFromRepository(treeArray,repo,path,refString,dService,TreeEntry.TYPE_BLOB);
		if(sha==null) throw new Exception("404 File Not Found");
		cService.deleteFile(repo, path, refString, sha, generateCommitMessage());
	}*/
	
	/*
	 * Get A File From Repository. Try raw content service first. Then try raw data service.
	 */
	/*public String getFileFromRepository(Repository repo, String path, String refString,String accessToken) throws Exception
	{
		try
		{
			return this.getFileFromRepositoryWithRawContentService(repo, path, refString, accessToken);
		}
		catch(Exception ex)
		{
			logger.info("Try to get file using raw content service failed. May be too large. Try Raw data service");
		}
		return this.getFileFromRepositroyWithRawDataService(repo, path, refString, accessToken);
	}*/
	
	/*
	 * Write a commit with a tree
	 */
	private void commitFileToRepository(File commitDirectory, Repository repo,String refString,String accessToken) throws Exception
	{
		DataService dService=new DataService();
		dService.getClient().setOAuth2Token(accessToken);
		Reference ref=dService.getReference(repo, refString);
		String lastCommitSHA=ref.getObject().getSha();
		List<TreeEntry> treeArray=new ArrayList<TreeEntry>();
		for(File f:commitDirectory.listFiles())
			buildTreeRecusive(f,repo,dService,treeArray);
		Tree tree=dService.createTree(repo, treeArray);
		logger.info("Try to make up commit");
		Commit parentCommit=new Commit();
		parentCommit.setSha(lastCommitSHA);
		List<Commit> parentCommitList=new ArrayList<Commit>();
		parentCommitList.add(parentCommit);
		Commit commit=new Commit();
		commit.setTree(tree);
		commit.setParents(parentCommitList);
		commit.setMessage(generateCommitMessage());
		commit=dService.createCommit(repo, commit);
		logger.info("Try to edit reference");
		ref.setRef(refString);
		TypedResource res=new TypedResource();
		res.setSha(commit.getSha());
		res.setType(TypedResource.TYPE_COMMIT);
		res.setUrl(commit.getUrl());
		ref.setObject(res);
		dService.editReference(repo, ref);
	}
	
	
	
	
	/*
	 * Build A Tree According To a File Without basetree. For init cms.
	 */
	private void buildTreeRecusive(File f,Repository repo,DataService dService,List<TreeEntry> treeArray) throws Exception
	{
		if(f.isFile())
		{
			Blob blob=new Blob();
			blob.setEncoding(GitHubConstants.UTF8ENCODING);
			String content=FileUtils.dumpFileIntoString(f,GitHubConstants.UTF8ENCODING);
			if(content==null) return;
			blob.setContent(content);
			String sha=dService.createBlob(repo, blob);
			String filename=f.getName();
			TreeEntry entry=new TreeEntry();
			entry.setPath(filename);
			entry.setSha(sha);
			entry.setType(TreeEntry.TYPE_BLOB);
			entry.setMode(TreeEntry.MODE_BLOB);
			treeArray.add(entry);
		}
		else if(f.isDirectory())
		{
			List<TreeEntry> subTreeArray=new ArrayList<TreeEntry>();
			File subFiles[]=f.listFiles();
			for(File subFile:subFiles)
			{
				buildTreeRecusive(subFile,repo,dService,subTreeArray);
			}
			Tree tree=dService.createTree(repo, subTreeArray);
			TreeEntry entry=new TreeEntry();
			entry.setPath(f.getName());
			entry.setSha(tree.getSha());
			entry.setType(TreeEntry.TYPE_TREE);
			entry.setMode(TreeEntry.MODE_DIRECTORY);
			treeArray.add(entry);
		}
	}
	
	/*
	 * Check Whether The Account is able to host Page
	 */
	public boolean isAccountReadyForPage(User u,List<Repository> repos)
	{
		String userName=u.getLogin().toLowerCase();
		for(Repository repo:repos)
		{
			if(repo.getName().equals(userName+GitHubConstants.PAGEPOSTFIX))
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 *  Make account able to host page.
	 */
	@CacheEvict(value = "github",key="#accessToken + 'repository'")
	public void initAccountPage(User u,String accessToken) throws Exception
	{
		createRepository(u.getLogin()+GitHubConstants.PAGEPOSTFIX,accessToken);
	}
	
	/*
	 * Check the repository is account page.
	 */
	
	public boolean isAccountPage(Repository repo)
	{
		return repo.getName().equals(repo.getOwner().getLogin().toLowerCase()+GitHubConstants.PAGEPOSTFIX);
	}
	
	public String getProperRefString(Repository repo)
	{
		 	String refString;
	        if(isAccountPage(repo))
	        {
	        	refString=GitHubConstants.MASTERREF;
	        }
	        else
	        {
	        	refString=GitHubConstants.PAGEREF;
	        }
	        return refString;
	}
	
	/*
	 * Check CMS init or not.
	 */
	public boolean isRepositoryPageCMSInit(Repository repo,String accessToken) throws Exception
	{
		
		
        String refString=getProperRefString(repo);
        String data=this.getRawFileFromRepository(repo, GitHubConstants.PAGEENTRYFILE, refString, accessToken);
        if(data==null)
        	return false;
        else
        	return true;
	}
	
	/*
	 * Set up page function before setting up cms.
	 */
	
	private void setupRepositoryPage(Repository repo,String accessToken) throws Exception
	{
		List<Reference> refList=getRepositoryRefs(repo,accessToken);
		if(refList.size()==0)
		{
			logger.info("Found a empty repository when checking whether repository is inited.");
			String repoName=repo.generateId();
			logger.warn("Try to swipe the empty repository");
			this.deleteRepository(repo, accessToken);
			logger.warn("Try to rebuild the repository");
			repo=this.createRepository(repoName, accessToken);
		}
		String refString=getProperRefString(repo);
		boolean foundRef=false;
		for(Reference r:refList)
		{
			
			if(r.getRef().contains(refString))
			{
				foundRef=true;
				logger.info("Ref check passed when setup page");
				break;
			}
		}
		if(!foundRef)
		{
			logger.info("Try to create branch {}",refString);
			createRepositoryBranch(repo,refString,accessToken);
		}
	}
	
	/*
	 * Setup CMS
	 */
	
	public void setupRepositoryPageCMS(Repository repo, Settings setting,String accessToken) throws Exception
	{
		setupRepositoryPage(repo,accessToken);
		String template=setting.getTemplate();
		File templateFile=FileUtils.downloadFileFromInternet(template);
		File outputDir=FileUtils.createTempDir();
		ZipUtils.unzip(templateFile, outputDir);
		this.commitFileToRepository(outputDir, repo, this.getProperRefString(repo), accessToken);
		this.editSettings(repo, setting, accessToken);
	} 
	
	/*
	 * Write a article
	 */
	public void addNewArticleEntry(Repository repo,ArticleEntry entry,String accessToken) throws Exception
	{
		String refString=this.getProperRefString(repo);
		RepositoryContents contents=this.getFileFromRepository(repo, GitHubConstants.ARTICLESETFILE, refString, accessToken);
		String aSetJson=new String(Base64.decodeBase64(contents.getContent()),GitHubConstants.UTF8ENCODING);
		Gson gson=GsonUtils.createGson();
		ArticleSet aset=gson.fromJson(aSetJson, ArticleSet.class);
		int id=-1;
		if(!aset.getIds().isEmpty())
		{
			id=aset.getIds().get(aset.getIds().size()-1)+1;
		}
		else
		{
			id=1;
		}
		while(true)
		{
			if(this.getRawFileFromRepository(repo, GitHubConstants.ARTICLEDIR+id, refString, accessToken)!=null)
			{
				logger.info("Found a lost article {}",id);
				id++;
			}
			else
			{
				break;
			}
		}
		
		aset.getIds().add(id);
		entry.setId(id);
		String entryJson=gson.toJson(entry);
		String articleSetJson=gson.toJson(aset);
		this.createFileInRepository(repo, GitHubConstants.ARTICLEDIR+id,refString, entryJson, accessToken);
		this.modifyFileInRepository(repo, GitHubConstants.ARTICLESETFILE, refString, articleSetJson, contents.getSha(), accessToken);
	}
	
	/*
	 * Edit A Article. 
	 */
	public void editArticleEntry(Repository repo,ArticleEntry entry,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		
		RepositoryContents contents=this.getFileFromRepository(repo, GitHubConstants.ARTICLEDIR+entry.getId(), refString, accessToken);
		Gson gson=GsonUtils.createGson();
		String entryJson=gson.toJson(entry);
		this.modifyFileInRepository(repo, GitHubConstants.ARTICLEDIR+entry.getId(), refString, entryJson, contents.getSha(), accessToken);
	}
	
	/*
	 * Remove A Article. 
	 */
	public void removeArticleEntry(Repository repo,ArticleEntry entry,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		RepositoryContents contents=this.getFileFromRepository(repo, GitHubConstants.ARTICLEDIR+entry.getId(), refString, accessToken);
		Gson gson=GsonUtils.createGson();
		RepositoryContents setContents=this.getFileFromRepository(repo, GitHubConstants.ARTICLESETFILE, refString, accessToken);
		String aSetJson=new String(Base64.decodeBase64(setContents.getContent()),GitHubConstants.UTF8ENCODING);
		ArticleSet aset=gson.fromJson(aSetJson, ArticleSet.class);
		aset.getIds().remove(entry.getId());
		aSetJson=gson.toJson(aset);
		this.deleteFileInRepository(repo, GitHubConstants.ARTICLEDIR+entry.getId(), refString, contents.getSha(), accessToken);
		this.modifyFileInRepository(repo, GitHubConstants.ARTICLESETFILE, refString, aSetJson, setContents.getSha(), accessToken);
	}
	
	/*
	 * Get A Article
	 */
	public ArticleEntry getArticleEntry(Repository repo,long id,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		String json=this.getRawFileFromRepository(repo, GitHubConstants.ARTICLEDIR+id, refString, accessToken);
		Gson gson=GsonUtils.createGson();
		return gson.fromJson(json, ArticleEntry.class);
	}
	
	/*
	 * Edit A Setting
	 */
	public void editSettings(Repository repo,Settings settings,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		RepositoryContents contents=this.getFileFromRepository(repo, GitHubConstants.SETTINGSFILE, refString, accessToken);
		Gson gson=GsonUtils.createGson();
		String settingsJson=gson.toJson(settings);
		this.modifyFileInRepository(repo, GitHubConstants.SETTINGSFILE, refString, settingsJson, contents.getSha(), accessToken);
	}
	
	/*
	 * Get Setting
	 */
	public Settings getSettings(Repository repo,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		String json=this.getRawFileFromRepository(repo, GitHubConstants.SETTINGSFILE,refString, accessToken);
		Gson gson=GsonUtils.createGson();
		return gson.fromJson(json, Settings.class);
	}

	/*
	 * Get ArticleSet
	 */
	public ArticleSet getArticleSet(Repository repo,String accessToken) throws Exception
	{
		String refString=getProperRefString(repo);
		String json=this.getRawFileFromRepository(repo, GitHubConstants.ARTICLESETFILE,refString, accessToken);
		Gson gson=GsonUtils.createGson();
		return gson.fromJson(json, ArticleSet.class);
	}
	
	
	/*
	 * Get Template From Repository
	 */
	@Cacheable(value = "template")
	public List<Template> getTemplateListFromRepository() throws Exception
	{
		URL uri=new URL(templateRepository);
		InputStream stream=(uri.openStream());
		SAXReader saxReader = new SAXReader();
		try
		{
			Document document = saxReader.read(stream);
			Element element=document.getRootElement();
			List<Template> result=new ArrayList<Template>();
			Iterator<?> tempIt=element.elementIterator(Template.TEMPLATETAG);
			while(tempIt.hasNext())
			{
				Element templateElement=(Element)tempIt.next();
				Iterator<?> it=templateElement.elementIterator();
				Template template=new Template();
				while(it.hasNext())
				{
					Element t=(Element)it.next();
					if(t.getName().equals(Template.NAMETAG))
					{
						template.setName(t.getTextTrim());
					}
					else if(t.getName().equals(Template.URLTAG))
					{
						template.setUrl(t.getTextTrim());
					}
					else if(t.getName().equals(Template.VERSIONTAG))
					{
						template.setVersion(t.getTextTrim());
					}
					else if(t.getName().equals(Template.IMAGETAG))
					{
						template.setImage(t.getTextTrim());
					}
					else if(t.getName().equals(Template.AUTHORTAG))
					{
						template.setAuthor(t.getTextTrim());
					}
					else if(t.getName().equals(Template.DESCRIPTIONTAG))
					{
						template.setDescription(t.getTextTrim());
					}
					else if(t.getName().equals(Template.DATETAG))
					{
						template.setDate(t.getTextTrim());
					}
					else
					{
						throw new Exception("Unable to parse template file");
					}
					
				}
				result.add(template);
			}
			return result;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				stream.close();
			}
			catch(Exception exx)
			{
				
			}
		}
	}
	
	/*
	 * Star Repository
	 */
	public void bonusGitHubPager(String accessToken) 
	{
		try
		{
			UserService uService=new UserService();
			uService.getClient().setOAuth2Token(accessToken);
			if(!uService.isFollowing(GitHubConstants.AUTHORUSER))
				uService.follow(GitHubConstants.AUTHORUSER);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void main(String args[])
	{
		String accessToken="0a65f880b9dc9b28781e0afdb8faf48d6c22373d";
		try
		{
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void setTemplateRepository(String templateRepository) {
		this.templateRepository = templateRepository;
	}
}

package com.wind.github;

import java.io.BufferedInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.TypedResource;

import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;


import org.eclipse.egit.github.core.User;

import org.eclipse.egit.github.core.client.GitHubClient;

import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wind.utils.Base64Coder;
import com.wind.utils.FileUtils;

public class PageManager {
	private static String PAGEPOSTFIX=".github.io";
	private static String PAGEENTRYFILE=".githubpager";
	private static String MASTERREF="refs/heads/master";
	private static String PAGEREF="refs/heads/gh-pages";
	private static String UTF8ENCODING="utf-8";
	private static String COMMITMESSAGE="GitHubPager updated in ";
	private static Logger logger=LoggerFactory.getLogger(PageManager.class);
	
	
	/*
	 * Create a auto generate commit message
	 */
	private String generateCommitMessage()
	{
		return COMMITMESSAGE+new Date().toString();
	}
	/*
	 * 
	 * Get User Basic Info.
	 */
	public User getBasicUserInfo(String accessToken) throws Exception
	{
		UserService uService=new UserService();
		uService.getClient().setOAuth2Token(accessToken);
		return uService.getUser();
	}
	
	/*
	 * Get Branch of Repository. You can check whether the repository is empty
	 */
	public List<Reference> getRepositoryRefs(Repository repo,String accessToken) throws Exception
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
	public List<Repository> getUserRepositories(String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		return rService.getRepositories();
	}
	
	/*
	 * Delete repository
	 */
	public void deleteRepository(Repository repo,String accessToken) throws Exception
	{
		GitHubClient client=new GitHubClient();
		client.setOAuth2Token(accessToken);
		logger.warn("Try to delete repo {}",repo.generateId());
		client.delete("/repos/"+repo.generateId());
	}
	
	/*
	 * Create Repository
	 */
	public Repository createRepository(String repoName,String accessToken) throws Exception
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
	public void createRepositoryBranch(Repository repo,String refString,String accessToken) throws Exception
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
	 * Get A file from repository with raw content service
	 */
	public String getFileFromRepositoryWithRawDataService(Repository repo, String path, String ref,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService rService=new RawContentsService(client);
		return rService.getRawFileAsString(repo, path, ref, UTF8ENCODING);
	}
	/*
	 * Get A file from Repositroy with raw data service
	 */
	public String getFileFromRepositroyWithDataService(Repository repo, String path, String refString,String accessToken) throws Exception
	{
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawDataService dService=new RawDataService(client);
		Reference ref=dService.getReference(repo,refString);
		String commitSHA=ref.getObject().getSha();
		Commit commit=dService.getCommit(repo, commitSHA);
		String treeSHA=commit.getTree().getSha();
		List<TreeEntry> treeArray=dService.getTree(repo, treeSHA).getTree();
		String sha=findBlobOrTreeFromRepository(treeArray,repo,path,refString,dService,TreeEntry.TYPE_BLOB,accessToken);
		return dService.getRawBlobAsString(repo, sha, UTF8ENCODING);
	}
	
	/*
	 * Search a repository for file or tree
	 */
	private String findBlobOrTreeFromRepository(List<TreeEntry> baseTree,Repository repo, String path, String refString, DataService dService, String type, String accessToken) throws Exception
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
	 * Modify a file 
	 */
	void modifyFileInRepository(Repository repo, String path, String refString, String content,String accessToken) throws Exception
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
		String sha=findBlobOrTreeFromRepository(treeArray,repo,path,refString,dService,TreeEntry.TYPE_BLOB,accessToken);
		if(sha==null) throw new Exception("404 File Not Found");
		cService.updateFile(repo, path, refString, sha, generateCommitMessage(), content);
	}
	
	/*
	 * Get A File From Repository
	 */
	String getFileFromRepository(Repository repo, String path, String refString,String accessToken)
	{
		return null;
	}
	
	/*
	 * Write a commit with a tree
	 */
	private void commitTreeToRepository(List<TreeEntry> treeArray, Repository repo,String refString,String accessToken) throws Exception
	{
		DataService dService=new DataService();
		dService.getClient().setOAuth2Token(accessToken);
		Reference ref=dService.getReference(repo, refString);
		String lastCommitSHA=ref.getObject().getSha();
		/*List<TreeEntry> treeArray=new ArrayList<TreeEntry>();
		if(base.isDirectory())
		{
			File fileList[]=base.listFiles();
			for(File f:fileList)
			{
				buildUpTree(f,repo,dService,treeArray);
			}
		}
		else
		{
			buildUpTree(base,repo,dService,treeArray);
		}*/
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
	private void buildTreeByFile(File f,Repository repo,DataService dService,List<TreeEntry> treeArray) throws Exception
	{
		if(f.isFile())
		{
			Blob blob=new Blob();
			blob.setEncoding(UTF8ENCODING);
			String content=FileUtils.dumpFileIntoString(f,UTF8ENCODING);
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
				buildTreeByFile(subFile,repo,dService,subTreeArray);
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
			if(repo.getName().equals(userName+PAGEPOSTFIX))
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 *  Make account able to host page.
	 */
	
	public void initAccountPage(User u,String accessToken) throws Exception
	{
		createRepository(u.getLogin()+PAGEPOSTFIX,accessToken);
	}
	
	/*
	 * Check the repository is account page.
	 */
	
	public boolean isAccountPage(User u,Repository repo)
	{
		return repo.getName().equals(u.getLogin().toLowerCase()+PAGEPOSTFIX);
	}
	
	/*
	 * Check CMS init or not.
	 */
	public boolean isRepositoryPageCMSInit(Repository repo,User u,String accessToken) throws Exception
	{
		
		RawGitHubClient client=new RawGitHubClient();
		client.setOAuth2Token(accessToken);
		RawContentsService rService=new RawContentsService(client);
        String refString;
        if(isAccountPage(u,repo))
        {
        	refString=MASTERREF;
        }
        else
        {
        	refString=PAGEREF;
        }
        String data=rService.getRawFileAsString(repo,PAGEENTRYFILE,refString,UTF8ENCODING);
        if(data==null)
        	return false;
        else
        	return true;
	}
	
	/*
	 * Set up page function before setting up cms.
	 */
	public void setupRepositoryPage(Repository repo,User u,String accessToken) throws Exception
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
		String refString=null;
		boolean isMain=isAccountPage(u,repo);
		if(isMain)
		{
			refString=MASTERREF;
		}
		else
		{
			refString=PAGEREF;
		}
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
	public void setupRepositoryPageCMS(Repository repo, User u, Map<String,String> params,String accessToken) throws Exception
	{
		setupRepositoryPage(repo,u,accessToken);
		
	} 
	
	

	
	public static void main(String args[])
	{
		String accessToken="0a65f880b9dc9b28781e0afdb8faf48d6c22373d";
		try
		{
			
			
			PageManager p=new PageManager();
			User u=p.getBasicUserInfo(accessToken);
			List<Repository> repoList=p.getUserRepositories(accessToken);
			if(!p.isAccountReadyForPage(u, repoList))
			{
				p.initAccountPage(u, accessToken);
				logger.info("Init Account Page");
				repoList=p.getUserRepositories(accessToken);
			}
			Repository repo=repoList.get(0);
			//p.createSingleFileByContentsService(repo,"test1", MASTERREF, new File("c:/test1.txt"), accessToken);
			
			//System.out.println(p.getFileFromRepositoryWithRawDataService(repo, "README.md", MASTERREF, accessToken));
			System.out.println(p.getFileFromRepositroyWithDataService(repo, "README.md", MASTERREF, accessToken));
			//p.modifyFileInRepository(repo, "README.md", MASTERREF, "testModify",accessToken);
			/*p.isRepositoryPageCMSInit(repo, u, accessToken);*/
			//p.createRepositoryBranch(repo, PAGEREF, accessToken);
			//p.setupRepositoryPage(repo,u,accessToken);
			//p.commitFiles(new File("c:/test"), repo, PAGEREF, accessToken);
			//p.deleteRepository(repo, accessToken);
			 
			/*p.createProjectPageBranch(repo, accessToken);
			
			/*List<File> array=new ArrayList<File>();
			array.add(new File("c:/eng000.txt"));
			p.createProjectPageBranch(repo, accessToken);
			p.commitFiles(array, repo, "master", accessToken);
			p.initAccountPage(u, accessToken);
			//p.initAccount(u, accessToken);*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

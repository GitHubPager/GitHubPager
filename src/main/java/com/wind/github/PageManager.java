package com.wind.github;

import java.io.IOException;import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.TypedResource;

import org.eclipse.egit.github.core.User;

import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageManager {
	private static String PAGEPOSTFIX=".github.io";
	private static String PAGEENTRYFILE=".githubpager";
	private static String ACCOUNTPAGEBRANCH="master";
	private static String PROJECTPAGEBRANCH="gh-pages";
	private static Logger logger=LoggerFactory.getLogger(PageManager.class);
	public User getBasicUserInfo(String accessToken) throws Exception
	{
		UserService uService=new UserService();
		uService.getClient().setOAuth2Token(accessToken);
		return uService.getUser();
	}
	public List<Repository> getUserRepositories(String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		return rService.getRepositories();
		
	}
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
	public void initAccount(User u,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		Repository r=new Repository();
		r.setName(u.getLogin()+PAGEPOSTFIX);
		rService.createRepository(r);
		
	}
	public Repository getRepositoryById(String repoName,User u,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		Repository repo=new Repository();
		repo.setName(repoName);
		repo.setOwner(u);
		repo=rService.getRepository(repo);
		return repo;
	}
	public boolean isAccountRepository(User u,Repository repo)
	{
		return repo.getName().equals(u.getLogin().toLowerCase()+PAGEPOSTFIX);
	}
	public boolean isRepositoryPageInit(String repoName,User u,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		ContentsServiceEx cService=new ContentsServiceEx();
		rService.getClient().setOAuth2Token(accessToken);
		cService.getClient().setOAuth2Token(accessToken);
		Repository repo=new Repository();
		repo.setName(repoName);
		repo.setOwner(u);
		repo=rService.getRepository(repo);
		String branch;
		if(isAccountRepository(u,repo))
		{
			branch=ACCOUNTPAGEBRANCH;
		}
		else
		{
			branch=PROJECTPAGEBRANCH;
		}
		try
		{
			cService.getContents(repo,PAGEENTRYFILE ,branch);
			return true;
		}
		catch(IOException e)
		{
			if(e.getMessage().contains("404"))
			{
			logger.info("Uninit repo of {}",u.getLogin());
			return false;
			}
			else throw e;
		}
	}
	/*public boolean isRepositoryPageInit(String repoName,User u,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		CommitService cService=new CommitService();
		rService.getClient().setOAuth2Token(accessToken);
		cService.getClient().setOAuth2Token(accessToken);
		Repository repo=new Repository();
		repo.setName(repoName);
		repo.setOwner(u);
		repo=rService.getRepository(repo);
		List<RepositoryCommit> clist=cService.getCommits(repo, "", "");
		for(RepositoryCommit c:clist)
		{
			System.out.println(c.getCommit().getCommitter().getDate());
		}
		if(clist.size()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}*/
	
	public void initRepository(Repository repo, String branch ,String accessToken) throws Exception
	{
		
	}
	
}

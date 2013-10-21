package com.wind.github;

import java.util.List;

import org.eclipse.egit.github.core.Repository;

import org.eclipse.egit.github.core.User;

import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageManager {
	private static String PAGEPOSTFIX=".github.io";
	private static String PAGEENTRYFILE=".githubpager";
	private static String MAINPAGEBRANCH="master";
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
	public boolean isAccountReadyForPage(String userName,List<Repository> repos)
	{
		userName=userName.toLowerCase();
		
		for(Repository repo:repos)
		{
			
			if(repo.getName().equals(userName+PAGEPOSTFIX))
			{
				return true;
			}
		}
		return false;
	} 
	public void initAccount(String domainName,String accessToken) throws Exception
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		Repository r=new Repository();
		r.setName(domainName+PAGEPOSTFIX);
		rService.createRepository(r);
		
	}
	public Repository getRepositoryById(int repoId)
	{
		return null;
	}
	public boolean isPageRepository(String userName,Repository repo)
	{
		return repo.getName().equals(userName+PAGEPOSTFIX);
	}
	public boolean isRepositoryPageInit(Repository repo,String accessToken)
	{
	
			
		
		return false;
	}
	
}

package com.wind.github;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
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
	public User getBasicUserInfo(String accessToken)
	{
		UserService uService=new UserService();
		uService.getClient().setOAuth2Token(accessToken);
		try
		{
			return uService.getUser();
		}
		catch(Exception e)
		{
			logger.error("Failed to get user info",e);
			return null;
		}
	}
	public List<Repository> getUserRepositories(String accessToken)
	{
		RepositoryService rService=new RepositoryService();
		rService.getClient().setOAuth2Token(accessToken);
		try
		{
			
			 return rService.getRepositories();
		}
		catch(Exception e)
		{
			logger.error("Failed to get repo info",e);
			return null;
		}
	}
	public boolean isAccountReadyForPage(String userName,String companyName,List<Repository> repos)
	{
		if(companyName==null || companyName.isEmpty()) 
			companyName="!@#$%^"; //Avoid Mismatch
		for(Repository repo:repos)
		{
			if(repo.getName().equals(userName+PAGEPOSTFIX)||repo.getName().equals(companyName+PAGEPOSTFIX))
			{
				return true;
			}
		}
		return false;
	}
	public boolean isRepositoryPageInit(Repository repo,String accessToken)
	{
		ContentsServiceEx cService=new ContentsServiceEx();
		cService.getClient().setOAuth2Token(accessToken);
		try
		{
			List<RepositoryContents> contentArray=cService.getContents(repo, PAGEENTRYFILE,PROJECTPAGEBRANCH);
			
		}
		catch(Exception e)
		{
			
		}
		return false;
	}
	
}

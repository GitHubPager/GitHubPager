package com.wind.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wind.github.PageManager;
import com.wind.github.page.ArticleEntry;
import com.wind.github.page.ArticleSet;
import com.wind.github.page.Settings;
import com.wind.github.page.Template;

public class PanelController extends MultiActionController{
	PageManager pageManager;
	
	String listViewPage;
	String initAccountViewPage;
	String initAccountURL;
	String setupURL;
	String setupViewPage;
	String addPostViewPage;
	String verifyMailUrl;
	String logoutPage;
	String manageRepositoryPage;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		List<Repository> repoArray=pageManager.getUserRepositories(accessToken);
		if(repoArray.size()==0||!pageManager.isAccountReadyForPage(u, repoArray))
		{
			res.sendRedirect(initAccountURL);
			return null;
		}
		ModelAndView view=new ModelAndView();
		view.setViewName(listViewPage);
		view.addObject("repository", repoArray);
		view.addObject("userInfo", u);
		return view;
    }
	public ModelAndView commitInitAccount(HttpServletRequest req, 
    HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		if(!pageManager.checkVerifedEmail(accessToken))
		{
			res.sendRedirect(verifyMailUrl);
			return null;
		}
		pageManager.initAccountPage(u, accessToken);
		res.sendRedirect(req.getRequestURI());
    	return null;
    }
	public ModelAndView initAccount(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
	{
		
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);	
		ModelAndView view=new ModelAndView();
		view.setViewName(initAccountViewPage);
		view.addObject("userInfo", u);
		return view;
	}
	
	public ModelAndView manageRepository(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		if(pageManager.isRepositoryPageCMSInit(repo, accessToken))
		{
			int pid=0;
			String page=req.getParameter("page");
			if(page!=null&&!page.isEmpty()) pid=Integer.valueOf(page);
			ArticleSet aset=pageManager.getArticleSet(repo, accessToken);
			int setSize=aset.getIds().size();
			List<Integer> ids=aset.getIds();
			List<ArticleEntry> entrys=new ArrayList<ArticleEntry>();
			int cid=0;
			if(pid*WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE<setSize)
			{
				cid=pid*WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE;
			}
			int added=0;
			while(true)
			{
				if(cid<setSize&&added<WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE)
				{
					ArticleEntry entry=pageManager.getArticleEntry(repo, ids.get(cid), accessToken);
					entrys.add(entry);
					added++;
				}
				else
				{
					break;
				}
			}
			
			ModelAndView view=new ModelAndView();
			view.setViewName(manageRepositoryPage);
			view.addObject("repository", repo);
			view.addObject("userInfo", u);
			return view;
		}
		else
		{
			res.sendRedirect(setupURL+"&repositoryName="+repoName);
			return null;
		}
		
		
    }
	public ModelAndView logout(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		s.invalidate();
		ModelAndView view=new ModelAndView();
		view.setViewName(logoutPage);
		return view;
    }
	public ModelAndView setup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		List<Template> templateList=pageManager.getTemplateListFromRepository();
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.addObject(templateList);
		view.setViewName(setupViewPage);
		return view;
    }
	public ModelAndView commitSetup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
	    final String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		String template=req.getParameter("template");
		String title=req.getParameter("title");
		String description=req.getParameter("description");
		String domain=req.getParameter("domain");
		final Settings setting=new Settings();
		setting.setDescription(description);
		setting.setDomain(domain);
		setting.setTitle(title);
		setting.setRepository(repoName);
		setting.setTemplate(template);
		final Repository repo=pageManager.getStubRepository(u, repoName);
		Callable<Integer> task=new Callable<Integer>()
		{
			@Override
			public Integer call(){
				try
				{
					pageManager.setupRepositoryPageCMS(repo, setting, accessToken);
				}
				catch(Exception e)
				{
					logger.error("Unable to setup repository page cms",e);
					return WebConstants.BACKGROUNDWORKFAILED;
				}
				return WebConstants.BACKGROUNDWORKSUCCESS;
			}	
		};
		GitHubWorkerPool.arrangeWorker(task,accessToken);
		res.sendRedirect(req.getRequestURI());
		return null;
    }
	public ModelAndView addPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.setViewName(addPostViewPage);
		return view;
    }
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}



	public void setlistViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	
	public void setListViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	public void setInitAccountURL(String initAccountURL) {
		this.initAccountURL = initAccountURL;
	}
	public void setSetupURL(String setupURL) {
		this.setupURL = setupURL;
	}
	public void setSetupViewPage(String setupViewPage) {
		this.setupViewPage = setupViewPage;
	}
	private User getUserInfoViaSession(HttpSession s,String accessToken) throws Exception
	{
		User u=(User)(s.getAttribute(WebConstants.USERINFOCODE));
		if(u==null)
		{
			u=pageManager.getBasicUserInfo(accessToken);
			s.setAttribute(WebConstants.USERINFOCODE, u);
		}
		return u;
	}
	
	public void setAddPostViewPage(String addPostViewPage) { 
		this.addPostViewPage = addPostViewPage;
	}
	public void setVerifyMailUrl(String verifyMailUrl) {
		this.verifyMailUrl = verifyMailUrl;
	}
	public void setInitAccountViewPage(String initAccountViewPage) {
		this.initAccountViewPage = initAccountViewPage;
	}
	public void setLogoutPage(String logoutPage) {
		this.logoutPage = logoutPage;
	}
	public void setManageRepositoryPage(String manageRepositoryPage) {
		this.manageRepositoryPage = manageRepositoryPage;
	}

}

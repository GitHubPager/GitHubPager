package com.wind.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wind.github.PageManager;

public class PanelController extends MultiActionController{
	PageManager pageManager;
	
	String listViewPage;
	String initAccountPageURL;
	String setupURL;
	String setupViewPage;
	String addPostViewPage;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		List<Repository> repoArray=pageManager.getUserRepositories(accessToken);
		if(repoArray.size()==0||!pageManager.isAccountReadyForPage(u, repoArray))
		{
			res.sendRedirect(initAccountPageURL);
			return null;
		}
		ModelAndView view=new ModelAndView();
		view.setViewName(listViewPage);
		view.addObject("repository", repoArray);
		view.addObject("userInfo", u);
		return view;
    }
	public ModelAndView initAccountPage(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
	{
		
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);	
		
		pageManager.initAccountPage(u, accessToken);
		res.sendRedirect(req.getRequestURI());
		return null;
		
	}
	
	public ModelAndView edit(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repoName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		if(pageManager.isRepositoryPageCMSInit(repo, accessToken))
		{
			
		}
		else
		{
			res.sendRedirect(setupURL+"&repoName="+repoName);
		}
		
		return null;
    }
	public ModelAndView logout(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		s.invalidate();
		res.sendRedirect(req.getRequestURI());
		return null;
    }
	public ModelAndView setup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repoName");
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.setViewName(setupViewPage);
		return view;
    }
	public ModelAndView addPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repoName");
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
	public void setInitAccountPageURL(String initAccountPageURL) {
		this.initAccountPageURL = initAccountPageURL;
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

}
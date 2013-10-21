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

public class ManageController extends MultiActionController{
	PageManager pageManager;
	
	String listViewPage;
	String initAccountPage;
	String beforeSetupPage;
	String beforeSetupViewPage;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		List<Repository> repoArray=pageManager.getUserRepositories(accessToken);
		if(repoArray.size()==0||!pageManager.isAccountReadyForPage(u, repoArray))
		{
			res.sendRedirect(initAccountPage);
			return null;
		}
		ModelAndView view=new ModelAndView();
		view.setViewName(listViewPage);
		view.addObject("repository", repoArray);
		view.addObject("userInfo", u);
		return view;
    }
	public ModelAndView initAccount(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
	{
		
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);	
		
		pageManager.initAccount(u, accessToken);
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
		if(pageManager.isRepositoryPageInit(repoName, u, accessToken))
		{
			
		}
		else
		{
			res.sendRedirect(beforeSetupPage+"&repoName="+repoName);
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
	public ModelAndView beforeSetup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repoName");
		Repository repo=pageManager.getRepositoryById(repoName, u,accessToken);
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repo);
		view.addObject("userInfo",u);
		view.setViewName(beforeSetupViewPage);
		return view;
    }
	public ModelAndView setup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		return null;
    }
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}



	public void setlistViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	public void setinitAccountPage(String initAccountPage) {
		this.initAccountPage = initAccountPage;
	}
	public void setListViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	public void setInitAccountPage(String initAccountPage) {
		this.initAccountPage = initAccountPage;
	}
	public void setbeforeSetupPage(String beforeSetupPage) {
		this.beforeSetupPage = beforeSetupPage;
	}
	public void setbeforeSetupViewPage(String beforeSetupViewPage) {
		this.beforeSetupViewPage = beforeSetupViewPage;
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

}

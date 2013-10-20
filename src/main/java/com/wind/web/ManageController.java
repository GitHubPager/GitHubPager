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
	String errorPage;
	String listViewPage;
	String initAccountPage;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=pageManager.getBasicUserInfo(accessToken);
		if(u==null)
		{
			return new ModelAndView(errorPage,WebConstants.ERRORCODE,WebConstants.GETUSERINFOFAILED);
		}
		List<Repository> repoArray=pageManager.getUserRepositories(accessToken);
		if(repoArray==null)
		{
			return new ModelAndView(errorPage,WebConstants.ERRORCODE,WebConstants.GETUSERREPOFAILED);
		}
		if(repoArray.size()==0||!pageManager.isAccountReadyForPage(u.getLogin(), repoArray))
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
		User u=pageManager.getBasicUserInfo(accessToken);	
		String domainName=u.getLogin();
		if(pageManager.initAccount(domainName, accessToken))
		{
			res.sendRedirect(req.getRequestURI());
			return null;
		}
		else
		{
			return new ModelAndView(errorPage,WebConstants.ERRORCODE,WebConstants.INITACCOUNTFAILED);
		}
		
	}
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}


	public void setlistViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	public void setinitAccountPage(String initAccountPage) {
		this.initAccountPage = initAccountPage;
	}
	

}

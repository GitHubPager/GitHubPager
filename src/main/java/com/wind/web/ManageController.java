package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.egit.github.core.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wind.github.PageManager;

public class ManageController extends MultiActionController{
	PageManager pageManager;
	String errorPage;
	String afterInitPage;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		
    }
	public ModelAndView init(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
	{
		HttpSession s=req.getSession();
		if(s.getAttribute("WebConstants.USERINFOCODE")==null)
		{
			String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
			User u=pageManager.getBasicUserInfo(accessToken);
			if(u==null)
			{
				return new ModelAndView(errorPage,WebConstants.ERRORCODE,WebConstants.INITGETUSERINFOFAILED);
			}
			s.setAttribute(WebConstants.USERINFOCODE, u);
		}
		res.sendRedirect(afterInitPage);
		return null;
	}
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public void setAfterInitPage(String afterInitPage) {
		this.afterInitPage = afterInitPage;
	}
	

}

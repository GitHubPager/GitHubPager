package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wind.github.PageManager;

public class MailStatusInterceptor extends HandlerInterceptorAdapter{
	PageManager pageManager;
	String verifyMailUrl;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, 
            Object handler) throws Exception 
    {
		HttpSession s=request.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		if(!checkVerifedStatus(s,accessToken))
		{
			response.sendRedirect(verifyMailUrl);
			return false;
		}
		return true;
    }
	
	
	
	private boolean checkVerifedStatus(HttpSession s,String accessToken) throws Exception
	{
		Object obj=s.getAttribute(WebConstants.VERIFIEDFLAG);
		if(obj!=null) return true;
		else
		{
			if(pageManager.checkVerifedEmail(accessToken))
			{
				s.setAttribute(WebConstants.VERIFIEDFLAG, new Object());
				return true;
			}
		}
		return false;
	}
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	

	public void setVerifyMailUrl(String verifyMailUrl) {
		this.verifyMailUrl = verifyMailUrl;
	}

}

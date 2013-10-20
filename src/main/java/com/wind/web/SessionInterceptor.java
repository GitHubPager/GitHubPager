package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, 
            Object handler) throws Exception 
    {
		
		HttpSession s=request.getSession(false);
		if(checkAccessToken(s))
		{
			return true;
		}
		response.sendRedirect(errorRedirectPage);
		return false;
	}
	/*private boolean checkInit(HttpServletRequest request,HttpSession s)
	{
		String action=request.getParameter("action");
		if(action!=null&&!action.equals("init"))
		{
			if(s.getAttribute(WebConstants.INITFLAG)==null)
			{
				return false;
			}
		}
		return true;
	}*/
	private boolean checkAccessToken(HttpSession s)
	{
		if(s!=null)
		{
			String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
			if(accessToken!=null)
			{
				return true;
			}
		}
		return false;
	}
	private String errorRedirectPage;

	public void setErrorRedirectPage(String errorRedirectPage) {
		this.errorRedirectPage = errorRedirectPage;
	}
}

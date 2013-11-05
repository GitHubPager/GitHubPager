package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GitHubWorkerStatusInterceptor  extends HandlerInterceptorAdapter{
	String busyStatusUrl;
	String gotResultUrl;
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, 
            Object handler) throws Exception 
     {
		HttpSession s=request.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		if(GitHubWorkerPool.isWorking(accessToken))
		{
			response.sendRedirect(busyStatusUrl);
			return false;
		}
		else if(GitHubWorkerPool.hasResult(accessToken))
		{
			response.sendRedirect(gotResultUrl);
			return false;
		}
		return true;
     }
	public String getBusyStatusUrl() {
		return busyStatusUrl;
	}
	public void setBusyStatusUrl(String busyStatusUrl) {
		this.busyStatusUrl = busyStatusUrl;
	}
	public String getGotResultUrl() {
		return gotResultUrl;
	}
	public void setGotResultUrl(String gotResultUrl) {
		this.gotResultUrl = gotResultUrl;
	}
}

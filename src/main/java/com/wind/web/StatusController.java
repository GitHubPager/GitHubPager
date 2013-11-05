package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class StatusController   implements Controller{
	String busyViewPage;
	String resultViewPage;
	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		String action=arg0.getParameter("s");
		if(action==null)
		{
			arg1.sendRedirect("/");
			return null;
		}
		else if(action.equals("result"))
		{
			HttpSession s=arg0.getSession();
			String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
			int result=GitHubWorkerPool.getResult(accessToken);
			ModelAndView view=new ModelAndView();	
			view.setViewName(resultViewPage);
			view.addObject("result", result);
			return view;
		}
		else if(action.equals("busy"))
		{
			ModelAndView view=new ModelAndView();	
			view.setViewName(busyViewPage);
			return view;
		}
		return null;
		
	}
	public void setBusyViewPage(String busyViewPage) {
		this.busyViewPage = busyViewPage;
	}
	public void setResultViewPage(String resultViewPage) {
		this.resultViewPage = resultViewPage;
	}
	
}

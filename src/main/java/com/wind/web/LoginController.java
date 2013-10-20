package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.wind.github.OAuth;

public class LoginController  implements Controller{
	private OAuth oauth;
	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		String stateCode=oauth.generateStateCode();
		HttpSession s=arg0.getSession();
		s.setAttribute("stateCode", stateCode);
		arg1.sendRedirect(oauth.getOAuthRequestURL(stateCode));
		return null;
	}
	public void setOauth(OAuth oauth) {
		this.oauth = oauth;
	}
}

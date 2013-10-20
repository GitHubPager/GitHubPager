package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.wind.github.OAuth;

public class OAuthController implements Controller{

	private String successPage;
	private String errorPage;
	private OAuth oauth;
	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		String stateCode=arg0.getParameter(WebConstants.STATECODE);
		String code=arg0.getParameter(WebConstants.CALLBACKCODE);
		if(code==null || stateCode==null)
		{
			return new ModelAndView(errorPage,"errorCode","Please login again");
		}
		HttpSession s=arg0.getSession();
		String previousStateCode=(String)(s.getAttribute(WebConstants.STATECODE));
		if(previousStateCode==null || !previousStateCode.equals(stateCode))
		{
			return new ModelAndView(errorPage,"errorCode","No sync state");
		}
		String accessToken=oauth.getAccessToken(code);
		if(accessToken==null||accessToken.isEmpty())
		{
			return new ModelAndView(errorPage,"errorCode","Get AccessToken Failed");
		}
		s.setAttribute(WebConstants.ACCESSTOKEN, accessToken);
		arg1.sendRedirect(successPage);
		return null;
	}

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public void setOauth(OAuth oauth) {
		this.oauth = oauth;
	}
}

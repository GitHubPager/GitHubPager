package com.wind.github;



import java.util.HashMap;

import java.util.Map;
import java.util.Random;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuth {
	private static Logger logger=LoggerFactory.getLogger(OAuth.class);
	private String authUrl;
	
	private String accessTokenUrl;
	private String clientId;
	private String clientSecret;
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getOAuthRequestURL(String stateCode)
	{
		return String.format("%s?scope=repo,user&client_id=%s&state=%s", this.authUrl ,this.clientId,stateCode);
	}
	public String getAccessToken(String returnCode)
	{
		
		try
		{
			OAuthClient client=new OAuthClient("github.com");
			Map<String,String> params=new HashMap<String,String>();
			params.put("client_id", this.clientId);
			params.put("client_secret", this.clientSecret);
			params.put("code", returnCode);
			Map<String,String> result=client.post(this.accessTokenUrl, params, Map.class);
			return result.get("access_token");
		}
		catch(Exception e)
		{
			logger.error("Error when getting accessToken",e);
			return null;
		}
		
		
	}
	
	public String generateStateCode()
	{
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < 32; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();  
	}
	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	

}

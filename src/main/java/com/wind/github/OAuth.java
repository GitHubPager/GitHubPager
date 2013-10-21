package com.wind.github;



import java.util.HashMap;


import java.util.Map;
import java.util.Random;



public class OAuth {
	
	private static String AUTHURL="https://github.com/login/oauth/authorize";
	private static String ACCESSTOKENURL="/login/oauth/access_token";
	private String clientId;
	private String clientSecret;
	
	public String getOAuthRequestURL(String stateCode)
	{
		return String.format("%s?scope=repo,user&client_id=%s&state=%s", AUTHURL ,this.clientId,stateCode);
	}
	public String getAccessToken(String returnCode) throws Exception
	{
			OAuthClient client=new OAuthClient("github.com");
			Map<String,String> params=new HashMap<String,String>();
			params.put("client_id", this.clientId);
			params.put("client_secret", this.clientSecret);
			params.put("code", returnCode);
			Map<String,String> result=client.post(ACCESSTOKENURL, params, Map.class);
			return result.get("access_token");
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
	

}

package com.wind.github;

import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;

/*
 * For getting raw client;
 */
public class RawGitHubClient extends GitHubClient{
	String accept="application/vnd.github.v3.raw";
	public RawGitHubClient()
  	{
  		super();
  	}
	public void setAccept(String accept)
	{
		this.accept=accept;
	}
  	protected HttpURLConnection configureRequest(final HttpURLConnection request) {
  		HttpURLConnection parentRequest=super.configureRequest(request);
  		parentRequest.setRequestProperty(HEADER_ACCEPT,
				accept); 
  		return parentRequest;
	}
}

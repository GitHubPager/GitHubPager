package com.wind.github;

import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;

/*
 * For getting raw client;
 */
public class RawGitHubClient extends GitHubClient{
	public RawGitHubClient()
  	{
  		super();
  	}
  	protected HttpURLConnection configureRequest(final HttpURLConnection request) {
  		HttpURLConnection parentRequest=super.configureRequest(request);
  		parentRequest.setRequestProperty(HEADER_ACCEPT,
				"application/vnd.github.v3.raw"); 
  		return parentRequest;
	}
}

package com.wind.github;

import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;

public class OAuthClient extends GitHubClient{
	@Override
	protected
	HttpURLConnection configureRequest(final HttpURLConnection request)
	{
		super.configureRequest(request);
		request.setRequestProperty(HEADER_ACCEPT,"application/json");
		return request;
	}
	public OAuthClient(String hostname)
	{
		super(hostname);
	}
	@Override
	protected String configureUri(final String uri) 
	{
		return uri;
	}
}

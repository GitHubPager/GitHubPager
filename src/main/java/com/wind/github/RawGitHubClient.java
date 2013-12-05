package com.wind.github;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;

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
  	public void delete(final String uri, final Object params)
			throws IOException {
		HttpURLConnection request = createDelete(uri);
		if (params != null)
			sendParams(request, params);
		final int code = request.getResponseCode();
		updateRateLimits(request);
		if (code!=200)
			throw new RequestException(parseError(getStream(request)), code);
	}
}

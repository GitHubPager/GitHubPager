package com.wind.github;

import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.service.ContentsService;

public class ContentsServiceEx extends ContentsService{
	@Override
	public GitHubRequest createRequest()
	{
		GitHubRequest request=new GitHubRequest();
		request.setResponseContentType("application/vnd.github.beta.raw");
		return request;
	}
}

package com.wind.github;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_BLOBS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_GIT;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.egit.github.core.IRepositoryIdProvider;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.service.DataService;

import com.wind.utils.FileUtils;

public class RawDataService extends DataService{
	public RawDataService()
  	{
  		super();
  	}
  	public RawDataService(GitHubClient client)
  	{
  		super(client);
  	}
  	public InputStream getRawBlobAsStream(IRepositoryIdProvider repository, String sha)
			throws IOException {
		final String id = getId(repository);
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		if (sha.length() == 0)
			throw new IllegalArgumentException("SHA-1 cannot be empty"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder();
		uri.append(SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(SEGMENT_GIT);
		uri.append(SEGMENT_BLOBS);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setUri(uri);
		return client.getStream(request);
	}
  	 public String getRawBlobAsString(IRepositoryIdProvider repository, String sha,String encoding) throws Exception
     {
     	InputStream rawStream=null;
 		try
 		{
 			rawStream=getRawBlobAsStream(repository, sha);
 			String result=FileUtils.dumpInputStreamIntoString(rawStream, encoding);
 			return result;
 		}
 		catch(Exception e)
        {
         		throw e;
        }
 		finally
 		{
 			try
 			{
 				rawStream.close();
 			}
 			catch(Exception ex)
 			{
 				
 			}
 		}
     }
}

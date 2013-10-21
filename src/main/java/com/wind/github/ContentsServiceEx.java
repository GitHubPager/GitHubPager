package com.wind.github;

import java.io.BufferedInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.wind.utils.Base64Coder;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.service.ContentsService;

public class ContentsServiceEx extends ContentsService{
	
	public ContentsServiceEx()
	{
		super();
	}
	@Override
	protected GitHubRequest createRequest()
	{
		GitHubRequest request=new GitHubRequest();
		request.setResponseContentType("application/vnd.github.beta.raw");
		return request;
	}
	/*public void createFile(Repository repo, String path, String ref, String commitMessage, File f) throws IOException
	{
		
		ByteArrayOutputStream byteStream=null;
		BufferedInputStream fileStream=null;
		try
		{
			byteStream=new ByteArrayOutputStream();
			fileStream=new BufferedInputStream(new FileInputStream(f));
			int data=-1;
			while((data=fileStream.read())!=-1)
			{
				byteStream.write(data);
			}
		}
		catch(IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				fileStream.close();
			}
			catch(IOException ex)
			{
				
			}
		}
		byte[] data=byteStream.toByteArray();
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("path",path);
		params.put("message", commitMessage);
		params.put("branch", ref);
		params.put("content", Base64Coder.encode(data));
		String uri=String.format("/repos/%s/%s/contents/%s", repo.getOwner().getLogin(),repo.getName(),path);
		client.put(uri, params, Map.class);
		
	}*/
}

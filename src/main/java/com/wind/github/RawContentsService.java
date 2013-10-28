package com.wind.github;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_CONTENTS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;

import com.wind.utils.Base64Coder;
import com.wind.utils.FileUtils;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.service.ContentsService;

public class RawContentsService extends ContentsService{
        
      	public RawContentsService()
      	{
      		super();
      	}
      	public RawContentsService(GitHubClient client)
      	{
      		super(client);
      	}
        public void createFile(Repository repo, String path, String ref, String commitMessage, String content) throws IOException
        {
                Map<String,Object> params=new HashMap<String,Object>();
                params.put("path",path);
                params.put("message", commitMessage);
                params.put("branch", ref);
                params.put("content", (Base64Coder.encodeString(content)));
                String uri=String.format("/repos/%s/contents/%s", repo.generateId(),path);
                client.put(uri, params, Map.class);
        }
        public void updateFile(Repository repo, String path, String ref, String lastSHA ,String commitMessage, String content) throws Exception
        {
        	 Map<String,Object> params=new HashMap<String,Object>();
             params.put("path",path);
             params.put("message", commitMessage);
             params.put("branch", ref);
             params.put("content", (Base64Coder.encodeString(content)));
             params.put("sha", lastSHA);
             String uri=String.format("/repos/%s/contents/%s", repo.generateId(),path);
             client.put(uri, params, null);
        }
        public void deleteFile(Repository repo, String path, String ref, String lastSHA ,String commitMessage) throws Exception
        {
        	 Map<String,Object> params=new HashMap<String,Object>();
        	 params.put("path",path);
             params.put("message", commitMessage);
             params.put("branch", ref);
             params.put("sha", lastSHA);
             String uri=String.format("/repos/%s/contents/%s", repo.generateId(),path);
             client.delete(uri, params);
        }
        public void createFile(Repository repo, String path, String ref, String commitMessage, File f) throws IOException
        {
                byte data[]=FileUtils.dumpFileIntoByteArray(f);
                Map<String,Object> params=new HashMap<String,Object>();
                params.put("path",path);
                params.put("message", commitMessage);
                params.put("branch", ref);
                params.put("content", String.valueOf(Base64Coder.encode(data)));
                String uri=String.format("/repos/%s/contents/%s", repo.generateId(),path);
                client.put(uri, params, null);
        }
        public InputStream getRawFileAsStream(Repository repo, String path, String ref) throws Exception
        {
        	String id = repo.generateId();
    		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
    		uri.append('/').append(id);
    		uri.append(SEGMENT_CONTENTS);
    		if (path != null && path.length() > 0) {
    			if (path.charAt(0) != '/')
    				uri.append('/');
    			uri.append(path);
    		}
    		GitHubRequest request = createRequest();
    		request.setUri(uri);
    		if (ref != null && ref.length() > 0)
    			request.setParams(Collections.singletonMap("ref", ref));
    		return client.getStream(request);
        }
        public String getRawFileAsString(Repository repo, String path, String ref,String encoding) throws Exception
        {
        	InputStream rawStream=null;
    		try
    		{
    			rawStream=getRawFileAsStream(repo, path, ref);
    			String result=FileUtils.dumpInputStreamIntoString(rawStream, encoding);
    			return result;
    		}
    		catch(Exception e)
            {
            		if(e.getMessage().contains("404"))
            		{
    	                return null;
            		}
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

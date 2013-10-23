package com.wind.github;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.wind.utils.Base64Coder;
import com.wind.utils.FileUtils;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.service.ContentsService;

public class ContentsServiceEx extends ContentsService{
        
        @Override
        protected GitHubRequest createRequest()
        {
                GitHubRequest request=new GitHubRequest();
                //request.setResponseContentType("application/vnd.github.beta.raw");
                return request;
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
                client.put(uri, params, Map.class);
        }
}

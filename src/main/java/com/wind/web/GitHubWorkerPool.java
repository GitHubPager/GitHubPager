package com.wind.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GitHubWorkerPool {
	private static Map<String,Future> workers=Collections.synchronizedMap(new HashMap<String,Future>());
	private static ExecutorService service = Executors.newCachedThreadPool();
	public static void arrangeWorker(Runnable r,String accessToken) throws Exception
	{
		if(workers.containsKey(accessToken)) throw new Exception("Submit task failed");
		Future f=service.submit(r);
		workers.put(accessToken, f);
	}
	public static boolean isWorking(String accessToken)
	{
		Future f=workers.get(accessToken);
		if(f==null)
			return false;
		else
		{
			if(f.isDone())
			{
				workers.remove(accessToken);
				return false;
			}
			else
			{
				return true;
			}
		}
	}
}

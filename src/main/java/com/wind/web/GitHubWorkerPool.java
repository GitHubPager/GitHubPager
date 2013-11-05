package com.wind.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GitHubWorkerPool {
	private static Map<String,Future<Integer> > workers=Collections.synchronizedMap(new HashMap<String,Future<Integer> >());
	private static Map<String,Integer> workersStatus=Collections.synchronizedMap(new HashMap<String,Integer>());
	private static ExecutorService service = Executors.newCachedThreadPool();
	public static void arrangeWorker(Callable<Integer> r,String accessToken) throws Exception
	{
		if(workers.containsKey(accessToken)) throw new Exception("Submit task failed");
		Future<Integer> f=service.submit(r);
		workers.put(accessToken, f);
	}
	public static int getResult(String accessToken)
	{
		int result=workersStatus.remove(accessToken);
		return result;
	}
	public static boolean hasResult(String accessToken)
	{
		Integer result=workersStatus.get(accessToken);
		if(result!=null)
			return true;
		else 
			return false;
	}
	public static boolean isWorking(String accessToken)
	{
		Future<Integer> f=workers.get(accessToken);
		if(f==null)
			return false;
		else
		{
			if(f.isDone())
			{
				try
				{
					int result=f.get();
					workersStatus.put(accessToken, result);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
					workersStatus.put(accessToken,WebConstants.BACKGROUNDWORKFAILED);
				}
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

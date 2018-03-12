package com.supconit.kqfx.web.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPollUtil {
	
	private final static ExecutorService pool;
	
	static{
		pool = Executors.newCachedThreadPool();
	}
	
	
	public static ExecutorService getPool(){
		return pool;
	}
	

}

package com.zs.easy.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zs
 *
 */
public class ThreadPoolUtil {
	private ExecutorService pool ;

	public ThreadPoolUtil() {
		super();
		initThreadPool(1);
	}
	
	public ThreadPoolUtil(int threadLength) {
		super();
		initThreadPool(threadLength);
	}

	public synchronized void poolExecute(Runnable runnable){
		if(pool != null){
			pool.execute(runnable);
		}
	}
	
	public synchronized void poolClose(){
		if(pool != null){
			pool.shutdownNow();
		}
		pool = null;
	}
	
	private void initThreadPool(int threadLength) {
		if(threadLength < 1) {
			threadLength = 1;
		}
		if(pool != null) {
			poolClose();
		}
		pool = Executors.newFixedThreadPool(threadLength);
	}
}

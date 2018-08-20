package com.zs.easy.common.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zs.easy.common.constants.EasyConstants;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author zs
 *
 */
public class UIHandler extends Handler {
	private ArrayBlockingQueue<Runnable> tasks = new ArrayBlockingQueue<Runnable>(10000);
	private static ThreadLocal<UIHandler> uiHandlerLocal = new ThreadLocal<UIHandler>();
	public UIHandler() {
		super();
	}
	/**
	 * 异步任务添加，异步任务的线程变量中会自动存上UIHandler的一个实例，随后在该线程中可以通过UIHandler.get()取到
	 * @param task
	 * @param uiHandler
	 */
	public static void addAsynTask(final Runnable task, final UIHandler uiHandler){
		new Thread(new Runnable() {
			@Override
			public void run() {
				uiHandler.putToThread();
				task.run();
			}
		}).start();
	}
	/**
	 * 为使使用简单，UI操作实际执行完后该方法才会返回
	 * @param uiTask
	 */
	public synchronized void addUITask(Runnable uiTask){
		tasks.add(uiTask);
		Message msg = new Message();
		msg.what = 0;
		this.sendMessage(msg);
		while(true){
			if(tasks.size()==0){
				break;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
		}
		
	}
	@Override
	public void handleMessage(Message msg) {
		if(msg.what == 0){
			Runnable task = tasks.peek();
			while(task!=null){
				task.run();
				tasks.poll();
				task = tasks.peek();
			}
		}
	}
	
//	public void commit(){
//		Message msg = new Message();
//		msg.what = 0;
//		this.sendMessage(msg);
//	}
	
	private void putToThread(){
		uiHandlerLocal.set(this);
	}
	
	public static UIHandler get(){
		UIHandler uiH =  uiHandlerLocal.get();
		if(uiH == null){
			Log.w(EasyConstants.TAG, "No uiHandler instance in current non-ui-thread! Maybe you should add it by invoking UIHandler.putToThread().");
		}
		return uiH;
	}
	
}

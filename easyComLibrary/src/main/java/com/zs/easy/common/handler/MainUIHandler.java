package com.zs.easy.common.handler;

import android.os.Handler;
import android.os.Looper;

public class MainUIHandler extends Handler {
	private static MainUIHandler uiHandler;
	
	private MainUIHandler(Looper looper){
		super(looper);
	}
	
	public static synchronized MainUIHandler handler(){
		if(uiHandler == null){
			uiHandler = new MainUIHandler(Looper.getMainLooper());
		}
		return uiHandler;
	}
}

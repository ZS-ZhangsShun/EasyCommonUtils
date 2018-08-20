package com.zs.easy.common.utils;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class PermissionUtil {
	public static void checkInMainThread(){
		if(!isInMainThread()){
			throw new RuntimeException("you should call the method in main ui thread");
		}
	}
	
	public static void checkNotInMainThread(){
		if(isInMainThread()){
			throw new RuntimeException("you should call the method not in main ui thread");
		}
	}
	
	public static boolean isInMainThread(){
		return Looper.getMainLooper() == Looper.myLooper();
	}


	/**
	 * 获取指定路径下读写权限
	 * @param path
	 * @throws IOException
	 */
	public static void getFileWriteAndReadPermission(String path) throws IOException {
		if(!TextUtils.isEmpty(path)){
			do {
				Runtime.getRuntime().exec("chmod 777 " + path);
				Log.e("starTimes:" , "getFileWriteAndReadPermission -- " + path);
				path = path.substring(path.indexOf("/"),path.lastIndexOf("/"));
			} while(path.length() > 1);
		}
	}
}

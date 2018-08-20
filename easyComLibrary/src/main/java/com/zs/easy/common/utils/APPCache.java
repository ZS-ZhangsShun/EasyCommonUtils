package com.zs.easy.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 *只是用来存储和用户界面有关的配置信息，service层的缓存信息存储到另外一个缓存的文件下
 *
 *
 */
public class APPCache {
	private static final String CACHE_FILE_NAME = "CACHE_SP_FILE";
	private static SharedPreferences mSharedPreferences;

	private volatile static APPCache instance;
	private void APPCache(){};
	public static APPCache  getInstance(){
		if (instance == null){
			synchronized (APPCache.class){
				if (instance == null){
					instance = new APPCache();
				}
			}
		}
		return instance;
	}
	
	public void putBoolean(Context context, String key, boolean value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public  boolean getBoolean(Context context, String key, boolean defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * 向SharedPreferences中存储一个字符串
	 * @param context
	 * @param key
	 * @param value
	 */
	public  void putString(Context context, String key, String value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putString(key, value).commit();
	}

	/**
	 * 从SharedPreferences中取一个字符串
	 * @param context
	 * @param key
	 * @param defValue 缺省值
	 */
	public  String getString(Context context, String key, String defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getString(key, defValue);
	}
	/**
	 * 向SharedPreferences中存储一个字符串
	 * @param context
	 * @param key
	 * @param value
	 */
	public  void putInt(Context context, String key, Integer value) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putInt(key, value).commit();
	}

	/**
	 * 从SharedPreferences中取一个字符串
	 * @param context
	 * @param key
	 * @param defValue 缺省值
	 */
	public  Integer getInt(Context context, String key, Integer defValue) {
		if(mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getInt(key, defValue);
	}
}

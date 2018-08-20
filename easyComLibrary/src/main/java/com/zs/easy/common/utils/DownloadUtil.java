package com.zs.easy.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownloadUtil {
	private Context mContext;
	private Map<String, String> pkgNamPIdMap;
	private final String TAG = this.getClass().getSimpleName();
	private static DownloadUtil apkOperaterUtils;

	private Map<String, Integer> loadProgressMap = new HashMap<String, Integer>();

	private DownloadUtil(Context mContext) {
		this.mContext = mContext;
		pkgNamPIdMap = new HashMap<String, String>();
	}

	public static DownloadUtil getUtilInstance(Context mContext) {
		if (apkOperaterUtils == null){
			apkOperaterUtils = new DownloadUtil(mContext);
		}
		return apkOperaterUtils;
	}
	
	/**
	 * 下载App的方法
	 * 使用指定回调函数,以包名为Key,下载progress为value记录app下载状态
	 * 下载成功/失败后从map中移除此条记录
	 * @param url			下载地址
	 * @param target		本地存储路径(例:/mnt/sdcard/example/example.apk)
	 */
	public void downLoadApk(final String url, final String target) {
		downLoadApk(url, target, null, null);
	}

	/**
	 * 下载App的方法
	 * @param url			下载地址
	 * @param target		本地存储路径(例:/mnt/sdcard/example/example.apk)
	 * @param callback		回调函数
	 */
	public void downLoadApk(final String url, final String target, final IFileDownloadCallBack callback) {
		downLoadApk(url, target, null, callback);
	}

	/**
	 * 应用商店下载App的方法
	 * 使用指定回调函数,以包名为Key,下载progress为value记录app下载状态
	 * 下载成功/失败后从map中移除此条记录
	 * @param url			下载地址
	 * @param target		本地存储路径(例:/mnt/sdcard/example/example.apk)
	 * @param packName		包名
	 */
	public void appShopDownLoadApk(final String url, final String target, final String packName,String preId) {
		if(packName != null && packName != ""){
			loadProgressMap.put(packName, 0);
			pkgNamPIdMap.put(packName, preId);
		}
		downLoadApk(url, target, packName, null);
	}

	/**
	 * 下载App的方法
	 * 使用指定回调函数,以包名为Key,下载progress为value记录app下载状态
	 * 下载成功/失败后从map中移除此条记录
	 * @param url			下载地址
	 * @param target		本地存储路径(例:/mnt/sdcard/starapp/starAppShop/example.apk)
	 * @param packName		包名
	 */
	public void downLoadApk(final String url, final String target, final String packName, IFileDownloadCallBack callBack) {
		if (callBack == null) {
			if (packName != null || packName != "") {
				callBack = new FileDownloadCallBackImpl() {
					@Override
					public void onSuccess(final File file) {
//						Runnable installAndDelFileRunnable = new Runnable() {
//							@Override
//							public void run() {
								try {
									CoreAPPUtil.getUtilInstance(mContext).updateApk(target);
								} catch (Exception e) {
									loadProgressMap.remove(packName);
									LogUtil.e(EasyConstants.TAG,this.getClass().getName()+"---downLoadApk error:"+e.toString());
								}
//							}
//						};
//						threadpoolUtil.poolExecute(installAndDelFileRunnable);
					}

					@Override
					public void onLoading(int progress) {
						loadProgressMap.put(packName, progress / 2);
					}

					@Override
					public void onFailure(Exception e) {
						loadProgressMap.remove(packName);
					}
				};
			}
		}
		Runnable downloadRunnable = new DownloadRunnable(url, target, callBack);
		EasyVariable.threadPoolUtil.poolExecute(downloadRunnable);
	}

	public Map<String, Integer> getLoadProgressMap() {
		return loadProgressMap;
	}
	
	public Map<String, String> getPkgNamPIdMap() {
		return pkgNamPIdMap;
	}

	/**
	 * 通过包名获取记录中此apk的下载状态
	 * @param packName
	 * @return	下载progress,如没有返回null
	 */
	public Integer getLoadProgress(String packName) {
		return loadProgressMap.get(packName);
	}

	private class DownloadRunnable implements Runnable {
		private String downloadUrl;
		private String targetPath;
		private IFileDownloadCallBack callBack;

		public DownloadRunnable(String downloadUrl, String targetPath, IFileDownloadCallBack callBack) {
			this.downloadUrl = downloadUrl;
			this.targetPath = targetPath;
			this.callBack = callBack;
		}

		@Override
		public void run() {
			DownloadFileUtil.getUtilInstance().downLoadFile(downloadUrl, targetPath, callBack);
		}
	}
	
	/**
	 * 启动升级starbox的apk并把升级的apk路径传过去
	 * @param packageName
	 * @throws NameNotFoundException
	 */
	public void startApk(String packageName,String url) throws NameNotFoundException {
		PackageManager packageManager = mContext.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(packageName);
		intent.putExtra("url", url);
		intent.putExtra("packageName", mContext.getPackageName());
		mContext.startActivity(intent);
	}
}

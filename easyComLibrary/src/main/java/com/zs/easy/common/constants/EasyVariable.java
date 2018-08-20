package com.zs.easy.common.constants;

import android.content.Context;
import android.content.SharedPreferences;

import com.zs.easy.common.utils.ThreadPoolUtil;
import com.zs.easy.common.handler.UIHandler;

/**
 * 核心变量
 * @author zhangshun
 *
 */
public class EasyVariable {

	/**
	 * Application的上下文
	 */
	public static Context mContext;
	public static SharedPreferences spCommon;
	public static ThreadPoolUtil threadPoolUtil;
	public static UIHandler uiHandler;

	public static void initCoreVariable(Context context, String spTag){
		if (context != null){
			EasyVariable.mContext = context;
		}

		if (spTag != null){
			spCommon = mContext.getSharedPreferences(spTag, Context.MODE_PRIVATE);
		}

		if (threadPoolUtil == null) {
			threadPoolUtil = new ThreadPoolUtil(10);
		}
		if (uiHandler == null) {
			uiHandler = new UIHandler();
		}
	}

	public void onDestroy() {
		if (uiHandler != null) {
			uiHandler = null;
		}
		if (threadPoolUtil != null) {
			threadPoolUtil.poolClose();
		}
	}
	
}

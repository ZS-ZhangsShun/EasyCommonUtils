package com.zs.easy.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 功能： 用来退出应用的时候，关闭所有的Activity
 * 
 */
public class ActivityManageUtil {

	/**
	 * 存放activity的列表
	 */
	public static ConcurrentHashMap<Class<?>, Activity> activities = new ConcurrentHashMap<>();

	/**
	 * 添加Activity
	 *
	 * @param activity
	 */
	public static void addActivity(Activity activity, Class<?> clz) {
		activities.put(clz, activity);
	}

	/**
	 * 判断一个Activity 是否存在
	 *
	 * @param clz
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static <T extends Activity> boolean isActivityExist(Class<T> clz) {
		boolean res;
		Activity activity = getActivity(clz);
		if (activity == null) {
			res = false;
		} else {
			if (activity.isFinishing() || activity.isDestroyed()) {
				res = false;
			} else {
				res = true;
			}
		}

		return res;
	}

	/**
	 * 判断一个Activity 是否存在
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static <T extends Activity> boolean isActivityExist(String activityName) {
		boolean res;
		Activity activity = null;
		if (activities != null && activities.size() > 0) {
			Set<Map.Entry<Class<?>, Activity>> sets = activities.entrySet();
			for (Map.Entry<Class<?>, Activity> s : sets) {
				if((s.getKey().getName().equals(activityName))){
					activity = activities.get(s.getKey());
					break;
				}
			}
		}
		if (activity == null) {
			res = false;
		} else {
			if (activity.isFinishing() || activity.isDestroyed()) {
				res = false;
			} else {
				res = true;
			}
		}

		return res;
	}

	/**
	 * 获得指定activity实例
	 *
	 * @param clazz Activity 的类对象
	 * @return
	 */
	public static <T extends Activity> T getActivity(Class<T> clazz) {
		return (T) activities.get(clazz);
	}

	/**
	 * 移除activity,代替finish
	 *
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		if (activities.containsValue(activity)) {
			activities.remove(activity.getClass());
		}
	}

	/**
	 * 移除所有的Activity
	 */
	public static void removeAllActivity() {
		if (activities != null && activities.size() > 0) {
			Set<Map.Entry<Class<?>, Activity>> sets = activities.entrySet();
			for (Map.Entry<Class<?>, Activity> s : sets) {
				if (!s.getValue().isFinishing()) {
					s.getValue().finish();
				}
			}
		}
		activities.clear();
	}
	
	/**
	 * 除指定的Activity外都销毁
	 * @param activityName 例如：com.star.starbox.display.demo.home.HomeActivity
	 */
	public static void removeAllActivityButOne(String activityName) {
		if (activities != null && activities.size() > 0) {
			Set<Map.Entry<Class<?>, Activity>> sets = activities.entrySet();
			for (Map.Entry<Class<?>, Activity> s : sets) {
				if((s.getValue().equals(activityName))){
					continue;
				}
				if (!s.getValue().isFinishing()) {
					s.getValue().finish();
					activities.remove(s.getValue());
				}
			}
		}
	}

	/**
	 * 通过包名和activity的名称来启动某个Activity
	 * @param pkg
	 * @param activityName
	 */
	public static void startActivity(Context context, String pkg, String activityName){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cn = new ComponentName(pkg, activityName);
		intent.setComponent(cn);
		context.startActivity(intent);
	}
}

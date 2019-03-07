package com.zs.easy.common.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by zhangshun on 2019/2/27.
 */
public class ApplicationUtil {
    /**
     * 去桌面
     *
     * @param activity
     */
    public static void goHome(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }
}

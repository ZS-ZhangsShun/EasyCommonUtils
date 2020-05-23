package com.zs.easy.common.utils;

import android.app.Activity;
import android.os.SystemClock;

import com.zs.easy.common.view.ConfigDialog;

public class ConfigUtil {
    private volatile static ConfigUtil instance;

    public static ConfigUtil getInstence() {
        if (instance == null) {
            synchronized (ConfigUtil.class) {
                if (instance == null) {
                    instance = new ConfigUtil();
                }
            }
        }
        return instance;
    }

    private final int COUNTS = 5;// 点击次数
    private final long DURATION = 1000;// 规定有效时间
    long[] mHits = new long[COUNTS];

    /**
     * 连续点击多次 弹出隐藏对话框
     */
    public void multyClickToShowConfigDialog(Activity activity) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            ConfigDialog dialog = new ConfigDialog(activity);
            dialog.show();
        }
    }
}

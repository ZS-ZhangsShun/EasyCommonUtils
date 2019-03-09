package com.zs.easy.common.utils;

import android.content.Context;

import com.zs.easy.common.constants.EasyVariable;

/**
 * Created by zs on 2019/3/9.
 */
public class DensityUtil {

    public static float getDensity() {
        float mDensity = EasyVariable.mContext.getResources().getDisplayMetrics().density;
        return mDensity;
    }

    public static float getDensity(Context context) {
        float mDensity = context.getResources().getDisplayMetrics().density;
        return mDensity;
    }

    public static int dp2px(int dp) {
        int px = (int) (getDensity() * dp + 0.5);
        return px;
    }
}

package com.zs.easy.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;

/**
 * Created by zhangshun on 2018/1/27.
 */

public class StyleUtil {

    /**
     * 设置状态栏透明的风格
     */
    public static void setStatusTransparentStyle(Window window, boolean isTextBlack) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //android 6.0及以上支持改变状态栏字体颜色
            if (isTextBlack) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            //android 5.0及以上支持改变状态栏为透明状态
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置黑白风格 取消黑白风格 -- 设置颜色矩阵的饱和度偏移量为默认值  为 0
     * 调用之后当前Activity可立刻生效
     *
     * @param activity
     */
    public static void setGrayStyle(Activity activity) {
        Boolean isGray = EasyVariable.spCommon.getBoolean(EasyConstants.SP_GRAY_STYLE, false);
        if (isGray) {
            //设置灰度风格
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            activity.getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        } else {
            cancelGrayStyle(activity);
        }
    }

    /**
     * 取消黑白风格 -- 设置颜色矩阵的饱和度偏移量为默认值  1
     *
     * @param activity
     */
    public static void cancelGrayStyle(Activity activity) {
        //设置灰度风格
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(1);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        activity.getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    /**
     * 设置黑白风格 取消黑白风格 -- 设置颜色矩阵的饱和度偏移量为默认值  为 0
     * 调用之后当前Activity可立刻生效
     *
     * @param activity
     */
    public static void setGrayStyle(Activity activity, boolean isGray) {
        if (isGray) {
            //设置灰度风格
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            activity.getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        } else {
            cancelGrayStyle(activity);
        }
    }
}

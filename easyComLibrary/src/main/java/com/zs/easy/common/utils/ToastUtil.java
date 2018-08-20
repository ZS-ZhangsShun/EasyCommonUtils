package com.zs.easy.common.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Context mContext;

    /**
     * 初始化 传入Context对象
     */

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 弹出时间较长的吐司
     *
     * @param content
     */
    public static void showLongToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 弹出自定义封装的时间较长的吐司
     *
     * @param content
     */
    public static void showCustomerLongToast(String content) {
        CustomerToastView.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 弹出时间较短的吐司
     *
     * @param content
     */
    public static void showShortToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出自定义的时间较短的吐司
     *
     * @param content
     */
    public static void showCstomerShortToast(String content) {
        CustomerToastView.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }
}

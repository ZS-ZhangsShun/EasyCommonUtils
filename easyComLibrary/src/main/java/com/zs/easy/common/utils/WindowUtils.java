package com.zs.easy.common.utils;


import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zs.easy.common.R;
import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;

/**
 * 弹窗辅助类
 *
 * @ClassName WindowUtils
 *
 *
 */
public class WindowUtils {

    private static final String LOG_TAG = EasyConstants.TAG + " -- WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;

    public static Boolean isWaitingPopWindowShown = false;
    private static View view;

    /**
     * 显示弹出框
     */
    public static void showWaitingPopupWindow(String content) {
        if (isWaitingPopWindowShown) {
            LogUtil.i(LOG_TAG, "return cause already shown");
            return;
        }
        isWaitingPopWindowShown = true;
        LogUtil.i(LOG_TAG, "showPopupWindow");

        // 获取WindowManager
        mWindowManager = (WindowManager) EasyVariable.mContext.getSystemService(Context.WINDOW_SERVICE);

        mView = setUpView(content);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        // 设置flag

        int flags = WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = 400;
        params.height = 300;
        params.gravity = Gravity.CENTER;
        params.y = -10;

        mWindowManager.addView(mView, params);
        LogUtil.i(LOG_TAG, "add view");
    }

    /**
     * 隐藏弹出框
     */
    public static void hideWaitingPopupWindow() {
        LogUtil.i(LOG_TAG, "hide " + isWaitingPopWindowShown + ", " + mView);
        if (isWaitingPopWindowShown && null != mView) {
            LogUtil.i(LOG_TAG, "hidePopupWindow");
            mWindowManager.removeView(mView);
            isWaitingPopWindowShown = false;
        }

    }

    public static void updatePopWindowContent(String content){
        ((TextView)view.findViewById(R.id.content)).setText(content);
    }

    private static View setUpView(String content) {
        LogUtil.i(LOG_TAG, "setUp view");

        view = LayoutInflater.from(EasyVariable.mContext).inflate(R.layout.dialog_popup_window_progress,null);
        ((TextView)view.findViewById(R.id.content)).setText(content);
        return view;
    }
}
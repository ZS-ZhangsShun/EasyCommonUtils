package com.zs.easy.common.utils;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.handler.MainUIHandler;

/**
 * 打印调试数据 - 对外提供的方法类
 */

public class DebugManager {
    public static boolean isShowFloatWindow = false;
    public static boolean isShowToast = false;

    /***
     * 初始化调试模式
     */
    public static void initDebugMode(boolean isShowFloatWindow, boolean isShowToast) {
        DebugManager.isShowFloatWindow = isShowFloatWindow;
        DebugManager.isShowToast = isShowToast;
    }

    /**
     * 添加调试数据
     * 1. 打印到本地文件
     * 2. 实时打印log
     * 3. 将数据显示在debug弹框中
     */
    public static void addDebugData(final String debugData) {
        LogUtil.writeLogtoDefaultPath(EasyConstants.TAG, debugData);
        LogUtil.d(EasyConstants.TAG, debugData);
        if (isShowFloatWindow) {
            MainUIHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    DebugDialogUtil.getInstence().addDebugData(debugData);
                }
            });
        }
    }

    /**
     * 添加调试数据
     * 1. 打印到本地文件
     * 2. 实时打印log
     * 3. 将数据显示在debug弹框中
     * 4. 同时展示toast
     */
    public static void addDebugData(final String debugData, final String toastContent) {
        LogUtil.writeLogtoDefaultPath(EasyConstants.TAG, debugData);
        LogUtil.d(EasyConstants.TAG, debugData);
        MainUIHandler.handler().post(new Runnable() {
            @Override
            public void run() {
                if (isShowToast) {
                    ToastUtil.showShortToast(toastContent);
                }
                if (isShowFloatWindow) {
                    DebugDialogUtil.getInstence().addDebugData(debugData);
                }
            }
        });
    }
}

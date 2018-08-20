package com.zs.easy.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.handler.MainUIHandler;

public class ToastAndLogUtil {
    public static void TL(final String content) {
        LogUtil.i(EasyConstants.TAG, content);
        MainUIHandler.handler().post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(content);
            }
        });
    }
}

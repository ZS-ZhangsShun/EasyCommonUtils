package com.zs.easy.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zs.easy.common.R;
import com.zs.easy.common.constants.EasyVariable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 展示debug弹框
 */

public class DebugDialogUtil {

    private volatile static DebugDialogUtil instance;
    private Dialog dialog;
    private TextView tv;

    public static DebugDialogUtil getInstence() {
        if (instance == null) {
            synchronized (DebugDialogUtil.class) {
                if (instance == null) {
                    instance = new DebugDialogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 显示调试对话框
     *
     * @param context
     */
    public void showDebugDialog(Context context) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context, R.style.dialogNoBg);

        View view = View.inflate(context, R.layout.debug_info_item, null);
        tv = view.findViewById(R.id.debug_dialog_content);

        tv.setText(getFixedContent());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(600, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(view, layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().setGravity(Gravity.RIGHT);
        dialog.show();
    }

    public void hideDebugDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 添加数据，同时刷新列表
     *
     * @param content
     */
    public void addDebugData(String content) {
        String text = tv.getText().toString();
        if (text.length() > 900) {
            text = text.substring(text.length() - 900);
        }
        text += "\n";
        text += convertTimestampToString() + " " + content;
        tv.setText(text);
    }

    /**
     * 获取年月日
     */
    public static String convertTimestampToString() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        String time = format.format(date);
        return time;
    }

    private String getFixedContent() {
        String fixedContent = "\n" + "\n"
                + "================================================="
                + "\n"
                + "App log path： " + LogUtil.getDefaultLogPath() + File.separator + LogUtil.getDefaultFileName()
                + "\n"
                + "versionName ： " + CoreAPPUtil.getUtilInstance(EasyVariable.mContext).getVersionName() + "  ---  versionCode: " + CoreAPPUtil.getUtilInstance(EasyVariable.mContext).getVersionName()
                + "\n"
                + "================================================";
        return fixedContent;

    }

}

package com.zs.easy.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.zs.easy.common.R;
import com.zs.easy.common.utils.StyleUtil;

/**
 * 全屏 底部弹出的对话框 标题栏浸润、黑色字体
 */
public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        StyleUtil.setStatusTransparentStyle(getWindow(), true);
        setFullScreenAndBottomStyle();
    }

    private void setFullScreenAndBottomStyle() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //这里的height设置为MATCH_PARENT后 5.0的手机顶部状态栏会变成黑色 与Activity的效果不一致
        //但是不设置 在6.0以上的手机 底部虚拟菜单就会隐约不容易看到
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    public BaseDialog(@NonNull Context context, int style, boolean isFullScreen) {
        super(context, style);
        if (isFullScreen) {
            setFullScreenAndBottomStyle();
        }
    }
}

package com.zs.easy.common.interfaces;

import android.app.AlertDialog;

/**
 * 带有单选按钮的弹窗口接口
 * 
 * @Description:
 * @author:
 * @see:
 * @since:
 * @copyright © ciyun.cn
 * @Date:
 */
public interface DialogRadioInterface {
	void onFirstRadioListener(AlertDialog dialog);
	void onSecondRadioListener(AlertDialog dialog);
}

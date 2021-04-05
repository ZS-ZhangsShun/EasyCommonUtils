package com.zs.easy.common.interfaces;

import android.app.AlertDialog;

public interface DialogInterface {
//	确定点击事件
	void onDialogListener(AlertDialog dialog);
//	取消点击 
	void onDialogCancelListener(AlertDialog dialog);
	
}

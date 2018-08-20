package com.zs.easy.common.interfaces;

import android.app.AlertDialog;

public  interface DialogInterface {
//	确定点击事件
	public  void onDialogListener(AlertDialog dialog);
//	取消点击 
	public void onDialogCancelListener(AlertDialog dialog);
	
}

package com.zs.easy.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zs.easy.common.R;
import com.zs.easy.common.interfaces.DialogInterface;
import com.zs.easy.common.interfaces.DialogRadioInterface;

import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;

import com.zs.easy.common.utils.AnimUtil;

/**
 * 功能： 添加自定义弹出框工具类
 *
 * @author LiuQingJie
 */
public class HaloDialog {

    private static CustomProgressDialog progressDialog;
    static AlertDialog dialog = null;
    private static int dotNumber = 0;

    private static android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    /**
     * 底部含有左右按钮的弹出框（例如确定和取消，升级和暂不升级等）
     * 默认是：左边取消，右边确定的模式，若是需要右边取消，左边确定，注意listener的实现
     *
     * @param context    上下文
     * @param title      弹出框标题
     * @param msg        弹出框内容
     * @param listener   确认，取消按钮的监听器，需实现它的两个方法
     * @param leftBtn    底部左侧按钮显示文字
     * @param rightBtn   底部右侧按钮显示文字
     * @param cancelable 是否设置为阻塞模式：true为阻塞，false为非阻塞
     */
    public static void showInfoDialog(Context context, String title,
                                      String msg, String leftBtn, String rightBtn,
                                      final DialogInterface listener, boolean cancelable) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = View.inflate(context, R.layout.dialog_info, null);
        TextView leftTV = (TextView) view.findViewById(R.id.tv_left);
        TextView rightTV = (TextView) view.findViewById(R.id.tv_right);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView title1 = (TextView) view.findViewById(R.id.title);
        dialog.setCancelable(cancelable);
        leftTV.setText(leftBtn);
        rightTV.setText(rightBtn);
        content.setText(msg);
        title1.setText(title);
        Window window = dialog.getWindow();
        window.setContentView(view);
        leftTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    dialog.dismiss();
                } else {
                    listener.onDialogCancelListener(dialog);
                }

            }
        });
        rightTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    dialog.dismiss();
                } else {
                    listener.onDialogListener(dialog);
                }

            }
        });

    }

    /**
     * 含有单选按钮的弹出框
     *
     * @param context        上下文
     * @param title          标题
     * @param msg            内容
     * @param dialogListener 监听器
     */
    public static void showRadioDialog(final Context context, String title,
                                       String[] msg, final DialogRadioInterface dialogListener, final int selectedPosition) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        View view = View.inflate(context, R.layout.dialog_more_line, null);
        TextView title1 = (TextView) view.findViewById(R.id.title);
        final RadioButton firstRadion = (RadioButton) view
                .findViewById(R.id.radio_first);
        final RadioButton secondRadion = (RadioButton) view
                .findViewById(R.id.radio_second);
        firstRadion.setText(msg[0]);
        secondRadion.setText(msg[1]);
        title1.setText(title);

        dialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface dialog) {
                if (selectedPosition == 1) {
                    firstRadion.setChecked(true);
                    firstRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked, 0);
                    secondRadion.setButtonDrawable(null);
                    secondRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_out, 0);
                } else if (selectedPosition == 2) {
                    secondRadion.setChecked(true);
                    secondRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked, 0);
                    firstRadion.setButtonDrawable(null);
                    firstRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_out, 0);
                }
            }
        });
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(view);
        firstRadion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRadion.setButtonDrawable(null);
                secondRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.check_out, 0);
                firstRadion.setButtonDrawable(null);
                firstRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.checked, 0);

                if (dialogListener == null) {
                    dialog.dismiss();
                } else {
                    dialogListener.onFirstRadioListener(dialog);
                }
            }
        });
        secondRadion.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                firstRadion.setButtonDrawable(null);
                firstRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.check_out, 0);
                secondRadion.setButtonDrawable(null);
                secondRadion.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.checked, 0);

                if (dialogListener == null) {
                    dialog.dismiss();
                } else {
                    dialogListener.onSecondRadioListener(dialog);

                }
            }
        });

    }

    /**
     * @param context   上下文
     * @param cotent    内容信息
     * @param is_cancel 是否可以取消
     * @param listener  按键监听器
     */
    public static void showProgressDialog(Context context, String cotent,
                                          boolean is_cancel, OnCancelListener listener) {
        progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.setMessage(cotent);
        progressDialog.setCancelable(is_cancel);
        progressDialog.setCanceledOnTouchOutside(false);
        if (listener != null) {
            progressDialog.setOnCancelListener(listener);
        }
        progressDialog.show();

    }

    /**
     * @param context   上下文
     * @param cotent    内容信息
     * @param is_cancel 是否可以取消
     * @param listener  按键监听器
     */
    public static void showProgressDialogForOnlyText(Context context, String cotent,
                                                     boolean is_cancel, OnCancelListener listener) {
        progressDialog = CustomProgressDialog.createDialogForOnlyText(context);
        progressDialog.setMessage(cotent);
        progressDialog.setCancelable(is_cancel);
        progressDialog.setCanceledOnTouchOutside(false);
        if (listener != null) {
            progressDialog.setOnCancelListener(listener);
        }
        progressDialog.show();
        startUpdateTextInLoop(cotent);

    }

    private static void startUpdateTextInLoop(final String content) {
        try {
            if (dotNumber == 0) {
                dotNumber = 1;
                if (progressDialog != null) {
                    progressDialog.setMessage(content + ".");
                }
            } else if (dotNumber == 1) {
                dotNumber = 2;
                if (progressDialog != null) {
                    progressDialog.setMessage(content + "..");
                }
            } else if (dotNumber == 2) {
                dotNumber = 3;
                if (progressDialog != null) {
                    progressDialog.setMessage(content + "..");
                }
            } else if (dotNumber == 3) {
                dotNumber = 0;
                if (progressDialog != null) {
                    progressDialog.setMessage(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startUpdateTextInLoop(content);
            }
        }, 300);
    }

    /**
     * 更新等待框的显示内容
     *
     * @param cotent
     */
    public static void updateProgressDialog(String cotent) {
        if (progressDialog != null) {
            progressDialog.setMessage(cotent);
        }
    }

    public static void showWaitDialog(Context context, boolean is_cancel,
                                      String content) {
        showProgressDialog(context, content, is_cancel, new OnCancelListener() {
            @Override
            public void onCancel(android.content.DialogInterface arg0) {

            }
        });
    }

    public static void showWaitDialogOnlyText(Context context, boolean is_cancel, String content) {
        showProgressDialogForOnlyText(context, content, is_cancel, new OnCancelListener() {
            @Override
            public void onCancel(android.content.DialogInterface arg0) {

            }
        });
    }

    public static void dismissWaitDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public static void dismissOnlyTextDialog(String content) {
        if (progressDialog != null && progressDialog.isShowing()) {
            TextView textView = progressDialog.getText2();
            textView.setText(content);
            mHandler.removeCallbacksAndMessages(null);
            AnimUtil.FlipAnimatorXViewShow(progressDialog.getText(), textView, 200);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }, 1500);
        }
    }

    /**
     * @param context  上下文
     * @param msg      内容信息
     * @param leftBtn  取消
     * @param rightBtn 确定
     * @param listener 按键监听器
     */
    public static void showInfo2Dialog(Context context, String msg, String leftBtn, String rightBtn,
                                       final DialogInterface listener, boolean cancelable) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = View.inflate(context, R.layout.dialog_new_info, null);
        TextView leftTV = (TextView) view.findViewById(R.id.tv_left);
        TextView rightTV = (TextView) view.findViewById(R.id.tv_right);
        TextView content = (TextView) view.findViewById(R.id.content);
        dialog.setCancelable(cancelable);
        leftTV.setText(leftBtn);
        rightTV.setText(rightBtn);
        content.setText(msg);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.width = 530; // 宽度
        window.setAttributes(lp);
        window.setContentView(view);
        leftTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    dialog.dismiss();
                } else {
                    listener.onDialogCancelListener(dialog);
                }

            }
        });
        rightTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    dialog.dismiss();
                } else {
                    listener.onDialogListener(dialog);
                }

            }
        });

    }

    /**
     * 底部含有左右按钮的弹出框（例如确定和取消，升级和暂不升级等）
     * 默认是：左边取消，右边确定的模式，若是需要右边取消，左边确定，注意listener的实现
     *
     * @param context    上下文
     * @param title      弹出框标题
     * @param msg        弹出框内容
     * @param listener   确认，取消按钮的监听器，需实现它的两个方法
     * @param singleBtn  底部左侧按钮显示文字
     * @param cancelable 是否设置为阻塞模式：true为阻塞，false为非阻塞
     */
    public static void showSingleBtnInfoDialog(Context context, String title,
                                               String msg, String singleBtn,
                                               final DialogInterface listener, boolean cancelable) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = View.inflate(context, R.layout.dialog_single_btn_info, null);
        TextView singleTV = (TextView) view.findViewById(R.id.tv_single_btn);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView title1 = (TextView) view.findViewById(R.id.title);
        dialog.setCancelable(cancelable);
        singleTV.setText(singleBtn);
        content.setText(msg);
        title1.setText(title);
        Window window = dialog.getWindow();
        window.setContentView(view);
        singleTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    dialog.dismiss();
                } else {
                    listener.onDialogCancelListener(dialog);
                }

            }
        });
    }


    /**
     * 列表弹出框
     *
     * @param context
     * @param title
     * @param msg
     * @param listener
     * @param cancelable
     */
    public static void showListViewDialog(Context context, String title,
                                          String[] msg, final OnItemClickListener listener, boolean cancelable) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        View view = View.inflate(context, R.layout.dialog_listview, null);
        TextView title1 = (TextView) view.findViewById(R.id.title);
        ListView _List = (ListView) view.findViewById(R.id.id_listview);
        ListViewAdapter adapter = new ListViewAdapter(context, msg);
        _List.setAdapter(adapter);
        dialog.setCancelable(cancelable);
        title1.setText(title);
        Window window = dialog.getWindow();
        window.setContentView(view);
        _List.setOnItemClickListener(listener);

    }

    public static void dismissListViewDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static boolean isWaitDialogShowing() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return true;
        }
        return false;
    }

}

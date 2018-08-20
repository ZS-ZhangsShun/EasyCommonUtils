package com.zs.easy.common.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.zs.easy.common.R;

/**
 * Created by zhangshun on 2017/10/30.
 */

public class CustomerToastView {
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    Toast toast;
    Context mContext;
    TextView toastTextField;

    public CustomerToastView(Context context) {
        mContext = context;
        toast = new Toast(mContext);
//        toast.setGravity(Gravity.BOTTOM, 0, 260);// 位置会比原来的Toast偏上一些
        View toastRoot = LayoutInflater.from(mContext).inflate(R.layout.toast_view, null);
        toastTextField = (TextView) toastRoot.findViewById(R.id.toast_text);
        toast.setView(toastRoot);
    }

    public void setDuration(int d) {
        toast.setDuration(d);
    }

    public void setText(String t) {
        toastTextField.setText(t);
    }

    public static CustomerToastView makeText(Context context, String text, int duration) {
        CustomerToastView toastCustom = new CustomerToastView(context);
        toastCustom.setText(text);
        toastCustom.setDuration(duration);
        return toastCustom;
    }

    public void show() {
        toast.show();
    }
}

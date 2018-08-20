package com.zs.easy.common.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by zhangshun on 2016/7/15.
 */
public class TextWatchUtil {
    /**
     * 对输入框文字进行监听 有输入时则展示清空文字的按钮
     */
    public static void setOnTextChangedListenerForDel(final EditText et, final ImageView delIV) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    delIV.setVisibility(View.VISIBLE);
                } else {
                    delIV.setVisibility(View.INVISIBLE);
                }
            }
        });

        delIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }

    /**
     * 对输入框文字进行监听 不能输入空格
     */
    public static void setOnTextChangedListenerForSpace(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.contains(" ")) {
                    str = str.replace(" ", "");
                    et.setText(str);
                    et.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 对输入框文字进行监听 不能输入空格 有输入时则展示清空文字的按钮
     */
    public static void setOnTextChangedListenerForDelAndSpace(final EditText et, final ImageView delIV) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.contains(" ")) {
                    str = str.replace(" ", "");
                    et.setText(str);
                    et.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    delIV.setVisibility(View.VISIBLE);
                } else {
                    delIV.setVisibility(View.INVISIBLE);
                }
            }
        });


        delIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }

    /**
     * 对输入框文字进行监听 针对金额的控制 小数点后2位
     */
    public static void setOnTextChangedListenerForMoneyAndDel(final EditText et, final ImageView delIV) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        et.setText(s);
                        et.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et.setText(s);
                    et.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et.setText(s.subSequence(0, 1));
                        et.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    delIV.setVisibility(View.VISIBLE);
                } else {
                    delIV.setVisibility(View.INVISIBLE);
                }
            }
        });

        delIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }
}

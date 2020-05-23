package com.zs.easy.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.zs.easy.common.R;
import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.utils.DebugDialogUtil;
import com.zs.easy.common.utils.DebugManager;
import com.zs.easy.common.utils.LogUtil;
import com.zs.easy.common.utils.ToastUtil;

import java.io.File;


/**
 * @Date: 2019/2/11  17:04
 */
public class ConfigDialog extends BaseDialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText common_config_server_url_et;
    private TextView common_config_cancel_tv, common_config_confim_tv;
    private Spinner common_config_content_type_spinner;
    private Switch common_config_switch;
    private Switch common_config_local_data_switch;
    private Switch common_config_show_toast_switch;
    private Switch common_config_save_log_switch;
    private Switch common_open_debug_switch;
    private LinearLayout common_config_ll, common_config_shiyan_ll;
    private Button common_config_clear_btn;
    private Context mContext;
    private View mRootView;
    private LinearLayout config_pwd_ll;
    private EditText config_pwd_et;

    /**
     * 记录当前实验室开启状态
     */
    private boolean isChecked = false;
    private boolean isShowToastLog = false;
    private boolean isSaveToastLog = false;
    private boolean isShowDebugWindow = false;
    private boolean isNoNeedLogin = false;

    public ConfigDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.easy_config_dialog, null);
        setContentView(mRootView);

        initView(mRootView);
    }

    private void initView(View v) {
        config_pwd_ll = v.findViewById(R.id.config_pwd_ll);
        config_pwd_et = v.findViewById(R.id.config_pwd_et);
        common_config_server_url_et = v.findViewById(R.id.common_config_server_url_et);
        common_config_cancel_tv = v.findViewById(R.id.common_config_cancel_tv);
        common_config_confim_tv = v.findViewById(R.id.common_config_confim_tv);
        common_config_content_type_spinner = v.findViewById(R.id.common_config_content_type_spinner);
        common_config_switch = v.findViewById(R.id.common_config_switch);
        common_config_local_data_switch = v.findViewById(R.id.common_config_local_data_switch);
        common_config_show_toast_switch = v.findViewById(R.id.common_config_show_toast_switch);
        common_config_save_log_switch = v.findViewById(R.id.common_config_save_log_switch);
        common_open_debug_switch = v.findViewById(R.id.common_open_debug_switch);
        common_config_ll = v.findViewById(R.id.common_config_ll);
        common_config_shiyan_ll = v.findViewById(R.id.common_config_shiyan_ll);
        common_config_clear_btn = v.findViewById(R.id.common_config_clear_btn);

        common_config_cancel_tv.setOnClickListener(this);
        common_config_confim_tv.setOnClickListener(this);
        common_config_clear_btn.setOnClickListener(this);
        common_config_switch.setOnCheckedChangeListener(this);
        common_config_local_data_switch.setOnCheckedChangeListener(this);
        common_config_show_toast_switch.setOnCheckedChangeListener(this);
        common_config_save_log_switch.setOnCheckedChangeListener(this);
        common_open_debug_switch.setOnCheckedChangeListener(this);
        config_pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (config_pwd_et.getText().toString().equals(EasyConstants.PWD)) {
                    config_pwd_ll.setVisibility(View.GONE);
                    config_pwd_et.setText("");
                }
            }
        });

        if (EasyConstants.IS_DEBUG) {
            common_config_ll.setVisibility(View.VISIBLE);
            common_config_switch.setChecked(true);
        } else {
            common_config_ll.setVisibility(View.GONE);
        }
        if (DebugManager.isShowToast) {
            common_config_show_toast_switch.setChecked(true);
        }
        if (DebugManager.isShowFloatWindow) {
            common_open_debug_switch.setChecked(true);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.common_config_cancel_tv) {
            dismiss();
        } else if (id == R.id.common_config_confim_tv) {
            EasyConstants.IS_DEBUG = isChecked;
            EasyVariable.spCommon.edit().putBoolean(EasyConstants.SP_IS_DEBUG_KEY, EasyConstants.IS_DEBUG).apply();

            LogUtil.setLogable(isChecked);
            if (isChecked) {
                try {
                    DebugManager.isShowToast = isShowToastLog;
                    DebugManager.isShowFloatWindow = isShowDebugWindow;
                    EasyVariable.spCommon.edit().putBoolean(EasyConstants.SP_IS_SHOW_TOAST_LOG, DebugManager.isShowToast).apply();
                    EasyVariable.spCommon.edit().putBoolean(EasyConstants.SP_IS_SHOW_DEBUG_WINDOW, DebugManager.isShowFloatWindow).apply();
                    if (isShowDebugWindow) {
                        DebugDialogUtil.getInstence().showDebugDialog(EasyVariable.mContext);
                    } else {
                        DebugDialogUtil.getInstence().hideDebugDialog();
                    }
                    dismiss();
                    ToastUtil.showShortToast("设置成功，配置已生效");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                DebugDialogUtil.getInstence().hideDebugDialog();
                DebugManager.isShowToast = false;
                DebugManager.isShowFloatWindow = false;
                dismiss();
            }
        } else if (id == R.id.common_config_clear_btn) {
            EasyVariable.spCommon.edit().clear().apply();
            LogUtil.deleteLogFile(LogUtil.getDefaultLogPath() + File.separator + LogUtil.getDefaultFileName());
            ToastUtil.showLongToast("清除成功");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //开启实验室模式则不是正式
        LogUtil.i("isChecked =" + isChecked);
        if (buttonView.getId() == R.id.common_config_switch) {
            this.isChecked = isChecked;
            if (isChecked) {
                common_config_ll.setVisibility(View.VISIBLE);
            } else {
                common_config_ll.setVisibility(View.GONE);
            }
        } else if (buttonView.getId() == R.id.common_config_show_toast_switch) {
            this.isShowToastLog = isChecked;
        } else if (buttonView.getId() == R.id.common_config_save_log_switch) {
            this.isSaveToastLog = isChecked;
        } else if (buttonView.getId() == R.id.common_open_debug_switch) {
            this.isShowDebugWindow = isChecked;
        }
    }
}

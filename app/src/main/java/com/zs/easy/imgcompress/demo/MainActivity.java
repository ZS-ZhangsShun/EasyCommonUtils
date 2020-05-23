package com.zs.easy.imgcompress.demo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zs.easy.common.EasyCommon;
import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.handler.MainUIHandler;
import com.zs.easy.common.utils.ConfigUtil;
import com.zs.easy.common.utils.DebugDialogUtil;
import com.zs.easy.common.utils.DebugManager;
import com.zs.easy.common.utils.LogUtil;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EasyCommon.init(this, "EASY_TAG", true);
        EasyConstants.TAG = "sf901";
        EasyConstants.isShowToast = true;
        initConfigParam();

        handler.sendEmptyMessage(0);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigUtil.getInstence().multyClickToShowConfigDialog(MainActivity.this);
            }
        });

    }
    int i;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (i <= 100) {
                DebugManager.addDebugData("大量日志测试 DebugManager.addDebugData大量日志测试 DebugManager.addDebugData" + i++);
                handler.sendEmptyMessageDelayed(0,300);
            }
        }
    };

    private void initConfigParam() {
        EasyConstants.IS_DEBUG = EasyVariable.spCommon.getBoolean(EasyConstants.SP_IS_DEBUG_KEY, EasyConstants.IS_DEBUG);
        LogUtil.i("application init ============== IS_DEBUG :" + EasyConstants.IS_DEBUG);
        LogUtil.setLogable(EasyConstants.IS_DEBUG);
        if (EasyConstants.IS_DEBUG) {
            //实验室模式下才生效
            try {
                DebugManager.isShowToast = EasyVariable.spCommon.getBoolean(EasyConstants.SP_IS_SHOW_TOAST_LOG, DebugManager.isShowToast);
                DebugManager.isShowFloatWindow = EasyVariable.spCommon.getBoolean(EasyConstants.SP_IS_SHOW_DEBUG_WINDOW, DebugManager.isShowFloatWindow);
                if (DebugManager.isShowFloatWindow) {
                    DebugDialogUtil.getInstence().showDebugDialog(EasyVariable.mContext);
                }
                //对于将日志打印到本地的开关必须是在日志开关打开的基础上才生效
                //实验模式下默认开启日志
            } catch (Exception e) {
                LogUtil.e("application init ============== e:" + e.toString());
            }

            LogUtil.i("application init ============== SP_IS_SHOW_TOAST_LOG :" + DebugManager.isShowToast);
        }
    }
}

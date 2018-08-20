package com.zs.easy.common;

import android.content.Context;

import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.utils.LogUtil;
import com.zs.easy.common.utils.ToastUtil;

/**
 * Created by zhangshun on 2018/8/20
 */

public class EasyCommon {
    public static boolean hasInit = false;

    /**
     * 初始化common组件
     * @param context
     * @param spTag
     * @param isPrintLog
     */
    public static void init(Context context, String spTag , boolean isPrintLog){
        if (!hasInit){
            //核心变量的初始化
            EasyVariable.initCoreVariable(context,spTag);
            //日志打印功能的开启
            LogUtil.init(isPrintLog);
            //吐丝的初始化
            ToastUtil.init(context);
            hasInit = true;
        }
    }
}

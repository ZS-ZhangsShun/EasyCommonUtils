package com.zs.easy.common.constants;

import android.os.Environment;

/**
 * Created by zhangshun on 2017/3/30.
 */

public class EasyConstants {
    public static final String SP_GRAY_STYLE = "SP_GRAY_STYLE";
    /**
     * 日志输入的tag
     */
    public static String TAG = "EasyLog";
    public static boolean isShowToast = true;

    /** 保存文件的主目录 */
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easyfile/";

}

package com.zs.easy.common.constants;
import android.os.Environment;

/**
 * Created by zhangshun on 2017/3/30.
 */

public class EasyConstants {
    public static final String SP_GRAY_STYLE = "SP_GRAY_STYLE";
    public static String PWD = "zs123";
    /**
     * 日志输入的tag
     */
    public static String TAG = "EasyLog";
    public static String HTTP_TAG = "HTTP_LOG";
    public static boolean isShowToast = true;

    /**
     * 保存文件的主目录
     */
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easyfile/";
    public static final String SP_IS_DEBUG_KEY = "SP_IS_DEBUG_KEY";
    public static final String SP_IS_SHOW_TOAST_LOG = "SP_IS_SHOW_TOAST_LOG";
    public static final String SP_IS_SHOW_DEBUG_WINDOW = "SP_IS_SHOW_DEBUG_WINDOW";
    public static boolean IS_DEBUG = false;
    /**
     * 网络相关
     */
    public static int CONNECT_TIME_OUT = 7;
    public static int READ_TIME_OUT = 7;
    public static int READ_TIME_OUT_FOR_FILE = 60;
    public static String BASE_URL = "";

    /**
     * 是否把日志写到文件里
     */
    public static boolean IS_WRITE_LOG_TO_FILE = true;
}

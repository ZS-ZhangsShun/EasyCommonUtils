package com.zs.easy.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.zs.easy.common.handler.MainUIHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 功能：
 * 用来收集线程异常崩溃的日志
 * 保存成一个以崩溃时候的日期为名字的txt文档
 * 放在初始化的时候所传的path路径下面
 *
 * @author LiuQingJie
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE = new CrashHandler();
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();
    /**
     * 用于日期格式化，作为日志文件的一部分
     */
    private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    /**
     * 保存crash的文件
     */
    private String path = null;
    /**
     * 每条异常日志是否保存到单独的txt文件
     */
    private boolean isSaveInFile = false;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param path    保存crash的目录
     */
    public void init(Context context, String path, boolean isSaveLogToAloneTxtFile) {
        this.path = path;
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 如果以后需要将日志上传到服务器端，并且不需要在本地端保存的话，可以单独写一个业务逻辑在此进行区分处理
        this.isSaveInFile = isSaveLogToAloneTxtFile;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "error : " + e.toString());
            }
            // 退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null || mContext == null || path == null) {
            return false;
        }
        // 使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(mContext,R.string.program_exception, Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "an error occured when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtil.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtil.e(TAG, "an error occured when collect crash info");
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 错误信息类
     */
    private void saveCrashInfo2File(Throwable ex) {
        ex.printStackTrace();
        final StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            LogUtil.forceWriteLogtoDefaultPath("捕获到异常：", sb.toString());
            // 保存日志到单独的txt文件
            if (!isSaveInFile) {
                return;
            }
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = "crash" + formatter.format(new Date()) + ".txt";
                FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            MainUIHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    if (DebugManager.isShowToast) {
                        ToastUtil.showShortToast(sb.toString());
                    }
                    if (DebugManager.isShowFloatWindow) {
                        DebugDialogUtil.getInstence().addDebugData(sb.toString());
                    }
                }
            });
        } catch (Exception e) {
            LogUtil.e(TAG, "an error occured while writing file...");
        }
    }
}

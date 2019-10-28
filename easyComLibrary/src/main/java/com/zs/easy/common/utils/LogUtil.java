package com.zs.easy.common.utils;

import android.util.Log;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogUtil {
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static String MYLOGFILEName = "log.txt";// 本类输出的日志文件名称

    private static boolean logable;
    private static int logLevel;

    public static void init(boolean isPrintLog) {
        /**
         * 设置是否打印日志和日志等级
         */
        setLogable(isPrintLog);
        setLogLevel(LogPriority.VERBOSE);
    }

    public static enum LogPriority {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    public static void d(String tag, String msg) {
        log(LogPriority.DEBUG, tag, msg, null);
        writeLogToText(msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(LogPriority.DEBUG, tag, msg, tr);
        writeLogToText(msg + tr.toString());
    }

    public static void e(String tag, String msg) {
        log(LogPriority.ERROR, tag, msg, null);
        writeLogToText(msg);
    }

    public static void e(String msg) {
        log(LogPriority.ERROR, EasyConstants.TAG, msg, null);
        writeLogToText(msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(LogPriority.ERROR, tag, msg, tr);
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        tr.printStackTrace(printWriter);
        Throwable cause = tr.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        writeLogToText(msg + "\n" + result);
    }

    public static void i(String tag, String msg) {
        log(LogPriority.INFO, tag, msg, null);
        writeLogToText(msg);
    }

    public static void i(String msg) {
        log(LogPriority.INFO, EasyConstants.TAG, msg, null);
        writeLogToText(msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(LogPriority.INFO, tag, msg, tr);
        writeLogToText(msg + "\n" + tr.toString());
    }

    public static void v(String tag, String msg) {
        log(LogPriority.VERBOSE, tag, msg, null);
        writeLogToText(msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(LogPriority.VERBOSE, tag, msg, tr);
        writeLogToText(msg + "\n" + tr.toString());
    }

    public static void w(String tag, String msg) {
        log(LogPriority.WARN, tag, msg, null);
        writeLogToText(msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(LogPriority.WARN, tag, msg, tr);
        writeLogToText(msg + "\n" + tr.toString());
    }


    /**
     * 获取是否可打印日志
     *
     * @return
     */
    public static boolean isLogable() {
        return logable;
    }

    /**
     * 设置是否打印日志
     *
     * @param logable
     */
    public static void setLogable(boolean logable) {
        LogUtil.logable = logable;
    }

    /**
     * 设置日志等级
     *
     * @param level
     */
    public static void setLogLevel(LogPriority level) {
        LogUtil.logLevel = level.ordinal();
    }


    private static void log(LogPriority priority, String tag, String msg, Throwable tr) {
        int level = priority.ordinal();
        if (logable && logLevel <= level) {
            if (tr != null) {
                msg = msg + "\n" + Log.getStackTraceString(tr);
            }
            Log.println(level + 2, tag, msg);
        }
    }

    //将日志写到文件中
    private static void writeLogToText(String log) {
        try {
            /*if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File dir = new File(StarConstants.FILE_PATH + "log");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String fileName = "log.txt";
				FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName,true);
				StringBuffer sb = new StringBuffer();
				sb.append(DateUtil.convertTimestampToFullString(System.currentTimeMillis()) +":"+ log + "\n");
				fos.write(sb.toString().getBytes());
				fos.close();
			}*/
        } catch (Exception e) {
            LogUtil.e(EasyConstants.TAG, "an error occured while writing log file...");
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    public static void writeLogtoFile(String tag, String text) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        Log.i(tag, text);
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

        // 取得日志存放目录
        String path = getLogPath();
        if (path != null && !"".equals(path)) {
            try {
                // 打开文件
                File file = new File(path + File.separator + needWriteFiel + MYLOGFILEName);
                FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开日志文件并写入日志 自定义文件名 默认在sd卡根目录
     *
     * @return
     **/
    public static void writeLogtoFile(String tag, String text, String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        Log.i(tag, text);
        Date nowtime = new Date();
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

        // 取得日志存放目录
        String path = getLogPath();
        if (path != null && !"".equals(path)) {
            try {
                // 打开文件
                File file = new File(path + File.separator + fileName);
                FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开日志文件并写入日志 自定义文件名 存在Android data 包名 Cache下面
     * 如果sd卡不可用就存在 data  data  包名  Cache下面
     *
     * @return
     **/
    public static void writeLogtoCache(String tag, String text, String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        Log.i(tag, text);
        Date nowtime = new Date();
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

        // 取得日志存放目录
        String path = DeviceUtil.getCachePath(EasyVariable.mContext);
        if (path != null && !"".equals(path)) {
            try {
                // 打开文件
                File file = new File(path + File.separator + fileName);
                if (!file.exists()) {
                    file.mkdir();
                }
                FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开日志文件并写入日志 自定义文件
     *
     * @return
     **/
    public static void writeLogtoFile(String tag, String text, File file) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        Log.i(tag, text);
        Date nowtime = new Date();
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

        if (file != null && file.exists()) {
            try {
                // 打开文件
                FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getLogPath() {
        String path = "";
        // 获取扩展SD卡设备状太
        String sDStateString = android.os.Environment.getExternalStorageState();

        // 拥有可读可写权限
        if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            // 获取扩展存储设备的文件目录
            File SDFile = android.os.Environment.getExternalStorageDirectory();
            path = SDFile.getAbsolutePath();
        }
        return path;
    }
}
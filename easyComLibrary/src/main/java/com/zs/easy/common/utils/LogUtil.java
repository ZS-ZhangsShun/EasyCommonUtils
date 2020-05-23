package com.zs.easy.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LogUtil {
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static String MYLOGFILEName = "log.txt";// 本类输出的日志文件名称

    private static String defaultLogPath = getSDCardPath();// 本类输出的日志文件名称
    private static String defaultFileName = "EasyLog.txt";// 本类输出的日志文件名称
    /**
     * 日志超过最大长度后保留原始日志的比例
     * 比如保留前百分之80
     */
    private static float saveLogRate = 0.8f;
    /**
     * 日志文件最大长度 比如 2M
     */
    private static float maxLogLength = 2 * 1024 * 1024;

    private static boolean logable = true;
    private static int logLevel;

    public static String getDefaultFileName() {
        return defaultFileName;
    }

    public static String getDefaultLogPath() {
        return defaultLogPath;
    }

    public static void setDefaultLogPath(String defaultLogPath) {
        LogUtil.defaultLogPath = defaultLogPath;
    }

    public static void setDefaultFileName(String defaultFileName) {
        LogUtil.defaultFileName = defaultFileName;
    }

    public static void setSaveLogRate(float saveLogRate) {
        LogUtil.saveLogRate = saveLogRate;
    }

    public static void setMaxLogLength(float maxLogLength) {
        LogUtil.maxLogLength = maxLogLength;
    }

    public static float getSaveLogRate() {
        return saveLogRate;
    }

    public static float getMaxLogLength() {
        return maxLogLength;
    }

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
				File dir = new File(EasyConstants.FILE_PATH + "log");
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
    public static void writeLogtoFile(final String tag, final String text) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteFiel = logfile.format(nowtime);
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                String path = getSDCardPath();
                if (path != null && !"".equals(path)) {
                    try {
                        // 打开文件
                        String pathName = path + File.separator + needWriteFiel + MYLOGFILEName;
                        deleteSomeLog(pathName);
                        File file = new File(pathName);
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
        });

    }

    /**
     * 打开日志文件并写入日志 自定义文件名 默认在sd卡根目录
     *
     * @return
     **/
    public static void writeLogtoFile(final String tag, final String text, final String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                String path = getSDCardPath();
                if (path != null && !"".equals(path)) {
                    try {
                        deleteSomeLog(path + File.separator + fileName);
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
        });

    }

    /**
     * 打开日志文件并写入日志 自定义文件名 存在Android data 包名 Cache下面
     * 如果sd卡不可用就存在 data  data  包名  Cache下面
     *
     * @return
     **/
    public static void writeLogtoCache(final String tag, final String text, final String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                String path = getCachePath(EasyVariable.mContext);
                if (path != null && !"".equals(path)) {
                    try {
                        String pathName = path + File.separator + fileName;
                        deleteSomeLog(pathName);
                        // 打开文件
                        File file = new File(pathName);
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
        });

    }

    /**
     * 打开日志文件并写入日志 忽略开关必须写入
     *
     * @return
     **/
    public static synchronized void forceWriteLogtoDefaultPath(final String tag, final String text) {// 新建或打印到日志文件
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                if (defaultLogPath != null && !"".equals(defaultLogPath)) {
                    deleteSomeLog(defaultLogPath + File.separator + defaultFileName);
                    try {
                        // 打开文件
                        File file = new File(defaultLogPath + File.separator + defaultFileName);
                        FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                        BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                        bufWriter.write(needWriteMessage);
                        bufWriter.newLine();
                        bufWriter.close();
                        filerWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 打开日志文件并写入日志 日志路径是默认路径 可以通过set方法设置
     *
     * @return
     **/
    public static synchronized void writeLogtoDefaultPath(final String tag, final String text) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }

        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                if (defaultLogPath != null && !"".equals(defaultLogPath)) {
                    deleteSomeLog(defaultLogPath + File.separator + defaultFileName);
                    try {
                        // 打开文件
                        File file = new File(defaultLogPath + File.separator + defaultFileName);
                        FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                        BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                        bufWriter.write(needWriteMessage);
                        bufWriter.newLine();
                        bufWriter.close();
                        filerWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 打开日志文件并写入日志 日志路径是默认路径 可以通过set方法设置
     *
     * @return
     **/
    public static synchronized void printAndwriteLogtoDefaultPath(final String tag, final String text) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                if (defaultLogPath != null && !"".equals(defaultLogPath)) {
                    deleteSomeLog(defaultLogPath + File.separator + defaultFileName);
                    try {
                        // 打开文件
                        File file = new File(defaultLogPath + File.separator + defaultFileName);
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
        });
    }

    /**
     * 打开日志文件并写入日志 自定义文件
     *
     * @return
     **/
    public static void writeLogtoFile(final String tag, final String text, final File file) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    public static String getSDCardPath() {
        String path = "";
        // 获取扩展SD卡设备状太
        String sDStateString = Environment.getExternalStorageState();

        // 拥有可读可写权限
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            // 获取扩展存储设备的文件目录
            File SDFile = Environment.getExternalStorageDirectory();
            path = SDFile.getAbsolutePath();
        }
        return path;
    }

    /**
     * 获取app缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath();
        }
        Log.i(EasyConstants.TAG, "getCachePath: .........." + cachePath);
        return cachePath;
    }

    /**
     * 打开日志文件并写入日志 自定义文件 带日志的
     *
     * @return
     **/
    public static void printAndWriteLogtoFile(final String tag, final String text, final File file) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    /**
     * 打开日志文件并写入日志 自定义文件名 存在Android data 包名 Cache下面
     * 如果sd卡不可用就存在 data  data  包名  Cache下面
     *
     * @return
     **/
    public static void printAndWriteLogtoCache(final String tag, final String text, final String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                String path = DeviceUtil.getCachePath(EasyVariable.mContext);
                if (path != null && !"".equals(path)) {
                    try {
                        String pathName = path + File.separator + fileName;
                        deleteSomeLog(pathName);
                        // 打开文件
                        File file = new File(pathName);
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
        });

    }

    /**
     * 打开日志文件并写入日志 自定义文件名 默认在sd卡根目录
     *
     * @return
     **/
    public static void printAndWriteLogtoFile(final String tag, final String text, final String fileName) {// 新建或打印到日志文件
        if (!logable) {
            return;
        }
        EasyVariable.singleThreadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, text);
                Date nowtime = new Date();
                String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag + "    " + text;

                // 取得日志存放目录
                String path = getSDCardPath();
                if (path != null && !"".equals(path)) {
                    try {
                        String pathName = path + File.separator + fileName;
                        deleteSomeLog(pathName);
                        // 打开文件
                        File file = new File(pathName);
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
        });

    }

    /**
     * 根据日志文件大小，判断是否要删除本地日志信息
     */
    public static boolean deleteLogFile(String fileName) {
        if (fileName != null && !"".equals(fileName)) {
            // 打开文件
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    return file.delete();
                }
            } catch (Exception e) {
                i("deleteLog e = " + e.toString());
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除部分日志
     */
    private static void deleteSomeLog(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                if (file.length() > maxLogLength) {
                    i("当前日志文件大小：" + GBMBKBUtil.getSize(file.length()));
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    String lineContent = null;
                    ArrayList<String> contentList = new ArrayList<>();
                    //第一步把原始日志读取到list
                    while ((lineContent = br.readLine()) != null) {
                        contentList.add(lineContent + "\r\n");
                    }
                    fis.close();
                    isr.close();
                    br.close();
                    //第二步把list中的日志内容安装保存比例 写入到一个新文件里面
                    String fileSeparator = File.separator;
                    File tempFile = new File(fileName.substring(0, fileName.lastIndexOf(fileSeparator) + 1) + "easyTempLog.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFile); //定义一个   
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                    for (int j = (int) (contentList.size() * (1 - saveLogRate)); j < contentList.size(); j++) {
                        bw.write(contentList.get(j));
                    }
                    bw.flush();
                    bw.close();
                    //第三步删除原始日志文件
                    boolean deleteOk = deleteLogFile(fileName);
                    //第四步重命名这个临时文件
                    i("deleteOk?：" + deleteOk);
                    boolean renameOk = tempFile.renameTo(file);
                    i("remameOK?：" + renameOk);
                    i("日志优化后文件大小：" + GBMBKBUtil.getSize(file.length()));
                }
            }
        } catch (Exception e) {
            i("deleteSomeLog e = " + e.toString());
            e.printStackTrace();
        }
    }
}
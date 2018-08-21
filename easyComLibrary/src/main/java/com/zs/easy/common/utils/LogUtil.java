package com.zs.easy.common.utils;

import android.util.Log;

import com.zs.easy.common.constants.EasyConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


public class LogUtil {
	private static boolean logable;
	private static int logLevel;

	public static void init(boolean isPrintLog){
		/**
		 * 设置是否打印日志和日志等级
		 */
		setLogable(isPrintLog);
		setLogLevel(LogPriority.VERBOSE);
	}

	public static enum LogPriority {
		VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
	}
	
	public static void d(String tag,String msg){
		log(LogPriority.DEBUG, tag, msg, null);
		writeLogToText(msg);
	}
	
	public static void d(String tag,String msg,Throwable tr){
		log(LogPriority.DEBUG, tag, msg, tr);
		writeLogToText(msg + tr.toString());
	}
	
	public static void e(String tag,String msg){
		log(LogPriority.ERROR, tag, msg, null);
		writeLogToText(msg);
	}

	public static void e(String msg){
		log(LogPriority.ERROR, EasyConstants.TAG, msg, null);
		writeLogToText(msg);
	}
	
	public static void e(String tag,String msg,Throwable tr){
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
		writeLogToText(msg + "\n"+ result);
	}
	
	public static void i(String tag,String msg){
		log(LogPriority.INFO, tag, msg, null);
		writeLogToText(msg);
	}

	public static void i(String msg){
		log(LogPriority.INFO, EasyConstants.TAG, msg, null);
		writeLogToText(msg);
	}
	
	public static void i(String tag,String msg,Throwable tr){
		log(LogPriority.INFO, tag, msg, tr);
		writeLogToText(msg +  "\n" + tr.toString());
	}
	
	public static void v(String tag,String msg){
		log(LogPriority.VERBOSE, tag, msg, null);
		writeLogToText(msg);
	}
	public static void v(String tag,String msg,Throwable tr){
		log(LogPriority.VERBOSE, tag, msg, tr);
		writeLogToText(msg +  "\n" + tr.toString());
	}
	
	public static void w(String tag,String msg){
		log(LogPriority.WARN, tag, msg, null);
		writeLogToText(msg);
	}
	
	public static void w(String tag,String msg,Throwable tr){
		log(LogPriority.WARN, tag, msg, tr);
		writeLogToText(msg +  "\n" + tr.toString());
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

	
	private static void log(LogPriority priority,String tag,String msg,Throwable tr){
		int level = priority.ordinal();
		if (logable && logLevel <= level) {
			if(tr != null){
				msg = msg + "\n" + Log.getStackTraceString(tr);
			}
			Log.println(level + 2, tag, msg);
		} 
	}

	//将日志写到文件中
	public static void writeLogToText(String log){
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
}
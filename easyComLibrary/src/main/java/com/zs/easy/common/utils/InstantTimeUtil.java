package com.zs.easy.common.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class InstantTimeUtil {

    public static String getYearMonthDayTime(String time) {
        if (!"".equals(time)) {
            //格式 2018-04-17T05:30:06.000Z
            //截取成2018-04-17 05:30:06 转换为8时区的long类型
            String use_time = time.substring(0, 10) + " " + time.substring(11, 19);
//            LogUtil.i(StarConstants.TAG, use_time);
            long timeL = getTime(use_time);
            //服务器是 0 时区的时间  这里加上8小时 变成 8时区
            timeL += 8 * 3600 * 1000;
            //换算成201908.26 格式
            String finalTime = convertTimestampToYearMonDayString(timeL);
//            LogUtil.i(StarConstants.TAG, "finalMonDay = " + finalTime);
            return finalTime;
        } else {
            return "";
        }
    }

    public static String getFinalTime(String time) {
        if (!"".equals(time)) {
            //格式 2018-04-17T05:30:06.000Z
            //截取成2018-04-17 05:30:06 转换为8时区的long类型
            long timeL = getTime(time.substring(0, 10) + " " + time.substring(11, 19));
            //服务器是 0 时区的时间  这里加上8小时 变成 8时区
            timeL += 8 * 3600 * 1000;
            //换算成05:30:06 格式
            String finalTime = convertTimestampToString(timeL);
            return finalTime;
        } else {
            return "";
        }
    }

    public static String convertTimestampToStringByPattern(long timestamp, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Long time = Long.valueOf(timestamp);
        Date date = new Date();
        date.setTime(time.longValue());
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToStringByPatternAndZone(long timestamp, String pattern, String zone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone(zone));
        Long time = Long.valueOf(timestamp);
        Date date = new Date();
        date.setTime(time.longValue());
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Long time = Long.valueOf(timestamp);
        Date date = new Date();
        date.setTime(time.longValue());
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToYearMonDayString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Long time = Long.valueOf(timestamp);
        Date date = new Date();
        date.setTime(time.longValue());
        String d = format.format(date);
        return d;
    }

    /**
     * 将字符串转为时间戳
     */
    public static long getTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date date = new Date();
        try {
            date = sdf.parse(user_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getFullTimeFromInstant(String time) {
        if (!TextUtils.isEmpty(time)) {
            //格式 2018-04-17T05:30:06.000Z
            //截取成2018-04-17 05:30:06 转换为8时区的long类型
            long timeL = getTime(time.substring(0, 10) + " " + time.substring(11, 19));
            //服务器是 0 时区的时间  这里加上8小时 变成 8时区
            timeL += 8 * 3600 * 1000;
            //换算成05:30:06 格式
            String finalTime = DateUtil.convertTimestampToFullString(timeL);
            return finalTime;
        } else {
            return "";
        }
    }
}

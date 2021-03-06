package com.zs.easy.common.utils;

import android.content.Context;

import com.zs.easy.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时区走系统设置
 *
 * @author zs
 */
public class DateUtil {

    public final static long anHour = 3600L * 1000L;
    private final static long anMinute = 60L * 1000L;
    private final static long anSecond = 1000L;

    /**
     * smallerMill与largerMill相差多少分钟（smallerMill <= largerMill）
     *
     * @param smallerMill
     * @param largerMill
     * @return
     */
    public static int getDiffMinutes(long smallerMill, long largerMill) {
        return (int) ((largerMill - smallerMill) / 1000 / 60);
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        // now.set(Calendar.HOUR_OF_DAY, 0);
        // now.set(Calendar.MINUTE, 0);
        // now.set(Calendar.SECOND, 0);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        // now.set(Calendar.HOUR_OF_DAY, 0);
        // now.set(Calendar.MINUTE, 0);
        // now.set(Calendar.SECOND, 0);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到某天的时间 格式为yyyyMMdd
     *
     * @param d
     * @param day 可以为 负数
     * @return
     */
    public static String getYearMonthDayStringAt(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return date2StrYearMonthDayForFile(now.getTime());
    }

    /**
     * 得到一天的23：59：59
     *
     * @param d
     * @return
     */
    public static Date getLastTimeOfDay(Date d) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        return now.getTime();
    }

    public static Date getFirstTimeOfDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date convertTimestampToDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String d = format.format(timestamp);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertStringLongTimeToStringDate(String timestamp) {
        try {
            Date date = convertTimestampToDate(Long.parseLong(timestamp));
            return date2yyyyMMdd(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param timestamp
     * @return mm:ss
     */
    public static String convertTimestampTommss(long timestamp) {
        int minutes = (int) (timestamp / anMinute);
        int seconds = (int) (timestamp / anSecond) % 60;
        String second;
        if ((seconds > 0 || seconds == 0) && (seconds < 10)) {
            second = "0" + seconds;
        } else {
            second = "" + seconds;
        }
        String minute;
        if ((minutes > 0 || minutes == 0) && (minutes < 10)) {
            minute = "0" + minutes;
        } else {
            minute = "" + minutes;
        }

        String d = minute + ":" + second;
        return d;
    }

    public static String convertTimestampToStringByPattern(long timestamp, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());
        Date date = new Date();
        date.setTime(timestamp);
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToFullString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        date.setTime(timestamp);
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToHHmmssString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        date.setTime(timestamp);
        String d = format.format(date);
        return d;
    }

    public static String convertTimestampToString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        Date date = new Date();
        date.setTime(timestamp);
        String d = format.format(date);
        return d;
    }

    public static int convertDateToTimestamp(Date date) {
        return (int) (date.getTime() / 1000);
    }

    public static boolean isTimeBetween(int mid, int before, int after) {
        if (before > after) {
            int temp = before;
            before = after;
            after = temp;
        }
        if (mid >= before && mid <= after) {
            return true;
        }
        return false;
    }

    public static String data2HHmm(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String string = df.format(date);
        return string;
    }

    public static String data2MonthMMdd(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd", Locale.getDefault());
        String string = df.format(date);
        return string;
    }

    public static boolean date1BeforeDate2(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return cal1.before(cal2) || cal1.equals(cal2);
    }

    public static Date str2Date(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            // return new Date();
        }
    }

    public static int minDistance(Date date1, Date date2) {
        long long1 = date1.getTime();
        long long2 = date2.getTime();
        if (long1 == long2)
            return 0;
        long dis = 0;
        if (long1 > long2) {
            dis = long1 - long2;
        } else {
            dis = long2 - long1;
        }
        // TODO 进位？
        int secondDis = (int) (dis / 1000);
        return secondDis / 60;
    }

    public static int subDate(Date a, Date b, TimeZone tz) {
        return (int) ((a.getTime() + tz.getRawOffset()) / (24 * anHour) - (b.getTime() + tz.getRawOffset()) / (24 * anHour));
    }

    public static String getDateStr(int index, Calendar c, Context context) {
//		String dateStr = String.format("%02d", c.get(Calendar.DAY_OF_MONTH))+"/"+String.format("%02d", c.get(Calendar.MONTH)+1);
        String dateStr = "";
        if (index == 0) {
            //中文
            dateStr = getMonthStr(c, context) + c.get(Calendar.DAY_OF_MONTH) + "日";
        } else {
            //英文,法文
            dateStr = c.get(Calendar.DAY_OF_MONTH) + "/" + getMonthStr(c, context);
        }
        return dateStr;
    }

    public static String getMonthStr(Calendar c, Context context) {
        switch (c.get(Calendar.MONTH)) {
            case 0:
                return context.getResources().getString(R.string.month_1);
            case 1:
                return context.getResources().getString(R.string.month_2);
            case 2:
                return context.getResources().getString(R.string.month_3);
            case 3:
                return context.getResources().getString(R.string.month_4);
            case 4:
                return context.getResources().getString(R.string.month_5);
            case 5:
                return context.getResources().getString(R.string.month_6);
            case 6:
                return context.getResources().getString(R.string.month_7);
            case 7:
                return context.getResources().getString(R.string.month_8);
            case 8:
                return context.getResources().getString(R.string.month_9);
            case 9:
                return context.getResources().getString(R.string.month_10);
            case 10:
                return context.getResources().getString(R.string.month_11);
            case 11:
                return context.getResources().getString(R.string.month_12);
        }
        return "";
    }

    public static String getWeekStr(Calendar c, Context context) {
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return context.getResources().getString(R.string.week_1);
            case Calendar.TUESDAY:
                return context.getResources().getString(R.string.week_2);
            case Calendar.WEDNESDAY:
                return context.getResources().getString(R.string.week_3);
            case Calendar.THURSDAY:
                return context.getResources().getString(R.string.week_4);
            case Calendar.FRIDAY:
                return context.getResources().getString(R.string.week_5);
            case Calendar.SATURDAY:
                return context.getResources().getString(R.string.week_6);
            case Calendar.SUNDAY:
                return context.getResources().getString(R.string.week_7);
        }
        return "";
    }

    public static String getDayString(Calendar c, Date now, Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String format = "%s\t%2d %3s %4s";
        if (subDate(c.getTime(), now, c.getTimeZone()) == 0) {
            //如果是英文
            return String.format(format, "TODAY", c.get(Calendar.DAY_OF_MONTH), DateUtil.getMonthStr(c, context), c.get(Calendar.YEAR));
        }
        return String.format(format, getWeekStr(c, context), c.get(Calendar.DAY_OF_MONTH), DateUtil.getMonthStr(c, context), c.get(Calendar.YEAR));
    }

    /**
     *  判断当前日期是星期几
     *
     * @param time
     * @return
     */
    public static String getWeekStr(Date time, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return getWeekStr(calendar, context);
    }

    public static String getTodayString() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);
        return " TODAY" + " " + new Date().toString().substring(4, 7) + "." + day;
    }
    
    /*public static Calendar getNearCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNowDate());
        int minute = calendar.get(Calendar.MINUTE);
        if (minute > 30) {
            minute = 30;
        } else {
            minute = 0;
        }
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }*/

    /**
     * Date转换成String
     *
     * @param date
     * @return yyyy.MM.dd格式
     */
    public static String date2yyyyMMdd(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String string = df.format(date);
        return string;
    }

    /**
     * Date转换成String
     *
     * @param date
     * @return yyyy-MM-dd格式
     */
    public static String date2StrYearMonthDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String string = df.format(date);
        return string;
    }

    /**
     * Date转换成String
     *
     * @param date
     * @return yyyyMMdd格式
     */
    public static String date2StrYearMonthDayForFile(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String string = df.format(date);
        return string;
    }

    /**
     * 根据毫秒数转化为时分秒   00:00:00
     *
     * @param ms
     * @return HH:mm:ss
     */
    public static String getHHmmssTime(long ms) {
        int hour = (int) (ms / (60 * 60 * 1000));
        String hours = hour >= 10 ? hour + "" : "0" + hour;

        long lessMin = ms - 60 * 60 * 1000 * hour;
        int min = (int) (lessMin / (60 * 1000));
        String mins = min >= 10 ? min + "" : "0" + min;

        long lessSecond = lessMin - min * 60 * 1000;
        int second = (int) (lessSecond / 1000);
        String seconds = second >= 10 ? second + "" : "0" + second;

        return hours + ":" + mins + ":" + seconds;
    }

    /**
     * 根据传入的 String格式的时间 "2019-03-03 10:30:25" 得到 与当前时间的差值
     * 然后转换为 00:00:00 格式
     *
     * @return
     */
    public static String stringToLessForHHmmss(String dates) {
        Date endTimeD = str2Date(dates);
        long lessTimeL = endTimeD.getTime() - System.currentTimeMillis();
        if (lessTimeL < 0) {
            lessTimeL = -lessTimeL;
        }
        return getHHmmssTime(lessTimeL);
    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int beginSecond, int endHour, int endMin, int endSecond) {
        boolean result = false;
        final long currentTimeMillis = System.currentTimeMillis();
        long beginTime, endTime;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(Calendar.HOUR_OF_DAY, beginHour);
        calendar.set(Calendar.MINUTE, beginMin);
        calendar.set(Calendar.SECOND, beginSecond);
        beginTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, endMin);
        calendar.set(Calendar.SECOND, endSecond);
        endTime = calendar.getTimeInMillis();

        LogUtil.i("currentTimeMillis = " + currentTimeMillis);
        LogUtil.i("beginTime = " + beginTime);
        LogUtil.i("endTime = " + endTime);

        // 普通情况(比如 8:00 - 14:00)
        result = beginTime <= currentTimeMillis && currentTimeMillis <= endTime;
        return result;
    }
}

package com.zs.easy.common.utils;

import java.text.DecimalFormat;

/**
 * Created by zhangshun on 2019/5/15.
 */
public class DecimalFormatUtil {

    /**
     * @param target     要转换的数
     * @param digigCount 保留几位小数
     * @return
     */
    public static String formatForString(float target, int digigCount) {
        try {
            String pattern = ".";
            for (int i = 0; i < digigCount; i++) {
                pattern += "0";
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target + "";
    }

    /**
     * @param target     要转换的数
     * @param digigCount 保留几位小数
     * @return
     */
    public static float formatForFloat(float target, int digigCount) {
        try {
            String pattern = ".";
            for (int i = 0; i < digigCount; i++) {
                pattern += "0";
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return Float.parseFloat(decimalFormat.format(target));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * @param target     要转换的数
     * @param digigCount 保留几位小数
     * @return
     */
    public static Double formatForDouble(double target, int digigCount) {
        try {
            String pattern = ".";
            for (int i = 0; i < digigCount; i++) {
                pattern += "0";
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return Double.parseDouble(decimalFormat.format(target));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * @param target     要转换的数
     * @param digigCount 保留几位小数
     * @return
     */
    public static String formatForString(Double target, int digigCount) {
        try {
            String pattern = ".";
            for (int i = 0; i < digigCount; i++) {
                pattern += "0";
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target + "";
    }

}

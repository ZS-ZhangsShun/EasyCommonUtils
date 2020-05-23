package com.zs.easy.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.zs.easy.common.constants.EasyVariable;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zs on 2019/1/31.
 */
public class DeviceUtil {

    public static boolean isEmulator() {
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canResolveIntent = intent.resolveActivity(EasyVariable.mContext.getPackageManager()) != null;

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) EasyVariable.mContext.getSystemService(Context.TELEPHONY_SERVICE))
                .getNetworkOperatorName().toLowerCase().equals("android")
                || !canResolveIntent;
    }


    /**
     * 获取app缓存路径
     * @param context
     * @return
     */
    public static String getCachePath( Context context ){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath() ;
        }else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath() ;
        }
        return cachePath ;
    }
    /**
     * 获取设备唯一标示
     */
    public static String getDeviceId() {
        String deviceId = Build.MODEL.trim() + getIMEI().trim() + getMac().trim();
        deviceId = deviceId.replace(" ", "");
        deviceId = deviceId.replace(":", "");
        return deviceId;
    }

    /**
     * 获取手机IMEI
     *
     * @return
     */
    public static final String getIMEI() {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) EasyVariable.mContext.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机Mac
     */
    public static String getMac() {
        String macAddress = "";
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 获取app缓存路径
     *
     * @param context
     * @return
     */
    public static String getSDPath(Context context) {
        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        LogUtil.i("getSDPath: .........." + cachePath);
        return cachePath;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String getDeviceInfo() {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("\n\nAndroid 版本： " + Build.VERSION.RELEASE + " -- " + Build.VERSION.SDK_INT);
            sb.append("\n主板Build.BOARD： " + Build.BOARD);
            sb.append("\n系统启动程序版本号Build.BOOTLOADER： " + Build.BOOTLOADER);
            sb.append("\n系统定制商Build.BRAND： " + Build.BRAND);
            sb.append("\ncpu指令集Build.CPU_ABI： " + Build.CPU_ABI);
            sb.append("\ncpu指令集2Build.CPU_ABI2: " + Build.CPU_ABI2);
            sb.append("\n设置参数Build.DEVICE： " + Build.DEVICE);
            sb.append("\n显示屏参数Build.DISPLAY：" + Build.DISPLAY);
            sb.append("\n无线电固件版本Build.getRadioVersion()：" + Build.getRadioVersion());
            sb.append("\n硬件识别码Build.FINGERPRINT： " + Build.FINGERPRINT);
            sb.append("\n硬件名称Build.HARDWARE： " + Build.HARDWARE);
            sb.append("\nHOST: " + Build.HOST);
            sb.append("\nBuild.ID：" + Build.ID);
            sb.append("\n硬件制造商Build.MANUFACTURER： " + Build.MANUFACTURER);
            sb.append("\n型号Build.MODEL： " + Build.MODEL);
            sb.append("\n硬件序列号Build.SERIAL： " + Build.SERIAL);
            sb.append("\n制造商uild.PRODUCT： " + Build.PRODUCT);
            sb.append("\n描述Build的标签Build.TAGS： " + Build.TAGS);
            sb.append("\nTIMEBuild.TIME: " + Build.TIME);
            sb.append("\nbuilder类型Build.TYPE：" + Build.TYPE);
            sb.append("\nUSER: " + Build.USER);
            sb.append("\nMAC1: " + getMac());
            sb.append("\ngetMacForSTB: " + getMacForSTB());
            sb.append("\ngetMacForSTB: " + getMacForSTB());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacForSTB() {
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();

                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress()) {
                        continue;
                    }
                    if (ip.isSiteLocalAddress()) {
                        mac = ni.getHardwareAddress();
                    } else if (!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                String s = "00" + Integer.toHexString(mac[i]) + ":";
                sb.append(s.substring(s.length() - 3));
            }
            return sb.substring(0, sb.length() - 1);
        } else {
            return "0";
        }
    }
}

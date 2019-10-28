package com.zs.easy.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zs.easy.common.R;
import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.handler.MainUIHandler;
import com.zs.easy.common.http.ZSHttpsUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

public class CoreAPPUtil {
    private Context mContext;
    private PackageManager packageManager;
    private static CoreAPPUtil apkOperaterUtils;

    private CoreAPPUtil(Context mContext) {
        this.mContext = mContext;
        packageManager = mContext.getPackageManager();
    }

    public static CoreAPPUtil getUtilInstance(Context mContext) {
        if (apkOperaterUtils == null) {
            apkOperaterUtils = new CoreAPPUtil(mContext);
        }
        return apkOperaterUtils;
    }

    /**
     * 静默安装Apk
     *
     * @param apkUri 格式:"file:///mnt/sdcard/starapp/starAppShop/viva.android.tv.apk"
     * @throws Exception
     */
    public void installSilent(Uri apkUri) throws Exception {
        if (apkUri != null) {
            Class<?> activityTherad = Class.forName("android.app.ActivityThread");
            Class<?> paramTypes[] = getParamTypes(activityTherad, "getPackageManager");
            Method method = activityTherad.getMethod("getPackageManager", paramTypes);
            Object PackageManagerService = method.invoke(activityTherad);
            Class<?> pmService = PackageManagerService.getClass();
            Class<?> paramTypes1[] = getParamTypes(pmService, "installPackage");
            method = pmService.getMethod("installPackage", paramTypes1);
            method.invoke(PackageManagerService, apkUri, null, 0, null);
        }
    }

    /**
     * 启动App
     *
     * @param packageName
     * @throws NameNotFoundException
     */
    public void startApk(String packageName) throws NameNotFoundException {
        Intent intent = new Intent();
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        intent = packageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
    }

    /**
     * 静默卸载App
     *
     * @param packageName
     * @throws Exception
     */
//	public void unInstallApk(String packageName) throws Exception {
//		PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
//		if (packageInfo != null) {
//			Class<?> activityTherad = Class.forName("android.app.ActivityThread");
//			Class<?> paramTypes[] = getParamTypes(activityTherad, "getPackageManager");
//			Method method = activityTherad.getMethod("getPackageManager", paramTypes);
//			Object PackageManagerService = method.invoke(activityTherad);
//			Class<?> pmService = PackageManagerService.getClass();
//			Class<?> paramTypes1[] = getParamTypes(pmService, "deletePackageAsUser");
//			method = pmService.getMethod("deletePackageAsUser", paramTypes1);
//			method.invoke(PackageManagerService, packageName, null,0,0);
//		}
//	}
    public void unInstallApk(String packageName) {
        String[] args = {"pm", "uninstall", packageName};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        ;
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 安装新版本apk的方法
     * 适配7.0
     */
    public void installApk(File file, String pkgName) {
        //安装应用程序
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, pkgName + ".fileProvider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    /**
     * 调用adb静默升级apk
     *
     * @param command
     * @throws Exception
     */
    public void updateApk(final String command) {
        try {
            Runtime.getRuntime().exec("pm install -r " + command);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
        }
    }

    /**
     * 获取本应用当前的版本号的方法
     * 使用versionName进行版本判断
     *
     * @return versionName, 如果发生异常, 返回空字符串 ""
     */
    public String getVersionName() {
        PackageInfo packageInfo = queryPackageInfo(mContext.getPackageName());
        if (packageInfo == null) {
            return "";
        }
        return packageInfo.versionName;
    }

    /**
     * 获取当前应用versionCode
     *
     * @return
     */
    public int getVersionCode() {
        PackageInfo packageInfo = queryPackageInfo(mContext.getPackageName());
        if (packageInfo == null) {
            return -1;
        }
        return packageInfo.versionCode;
    }

    private Class<?>[] getParamTypes(Class<?> cls, String mName) {
        Class<?> cs[] = null;
        Method[] mtd = cls.getMethods();
        for (int i = 0; i < mtd.length; i++) {
            if (!mtd[i].getName().equals(mName)) {
                continue;
            }
            cs = mtd[i].getParameterTypes();
        }
        return cs;
    }

    /**
     * 删除指定目录下的所有文件(不含文件夹遍历)
     * 排除 com.star.starbox.display.apk（当前应用）
     *
     * @param deleteFloderPath 要删除文件的目录
     */
    public void deleteFloderFile(String deleteFloderPath) {
        if (deleteFloderPath != null && deleteFloderPath != "") {
            File file = new File(deleteFloderPath);
            if (file.isDirectory()) {// 处理目录
                String currentPkgName = mContext.getApplicationInfo().packageName;
                File files[] = file.listFiles();
                for (File targetFile : files) {
                    String fileName = targetFile.getName();
                    if (targetFile.isFile() && !(currentPkgName + ".apk").equals(fileName)) {
                        targetFile.delete();
                    }
                }
            }
        }
    }

    /**
     * 强制停止后台运行的应用
     *
     * @param pkgName 包名
     */
    public void forceStopAPK(String pkgName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        Method forceStopPackage;
        try {
            forceStopPackage = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, pkgName);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 杀死除本应用和系统应用之外的所有后台程序
     */
    public void killAllBackgroundApp() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获取系统中所有正在运行的进程
        List<RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        String currentPkgName = mContext.getApplicationInfo().packageName;
        for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            String[] pkgNameList = appProcessInfo.pkgList;//一个进程中所有运行的应用的包名
            for (String pkgName : pkgNameList) {
                PackageInfo packageInfo = queryPackageInfo(pkgName);
                if (packageInfo != null) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !currentPkgName.equals(pkgName)) {
                        forceStopAPK(pkgName);
                    }
                }
            }
        }
    }

    /**
     * 根据包名查询PackageInfo（应用信息）
     *
     * @param pkgName
     * @return PackageInfo 没有返回null
     */
    public PackageInfo queryPackageInfo(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            Log.e(this.getClass().getName(), e.toString());
        }
        return packageInfo;
    }

    /**
     * 验证下载完成的包是否真的是升级包（排除错误：如果后台配置高版本号，其实传递的是低版本应用）
     */
    public boolean validateNewVersionApk(File file, int version) {
        String apkPath = Uri.fromFile(file).toString();
        apkPath = apkPath.replace("file://", "");
        try {
            PermissionUtil.getFileWriteAndReadPermission(apkPath);
        } catch (IOException e) {
            LogUtil.e(EasyConstants.TAG, e.toString());
        }
        PackageInfo pi = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        int serverVersion = -1;
        if (pi != null) {
            //解析成功了就把apk的版本赋值给serverVersion
            serverVersion = pi.versionCode;
            LogUtil.e(EasyConstants.TAG, "apk parse ok versionCode = " + pi.versionCode);
        } else {
            //解析失败了就默认相等来处理
            serverVersion = version;
            LogUtil.e(EasyConstants.TAG, "apk parse error");
        }
        if (serverVersion != version) {
            file.delete();
            LogUtil.e(EasyConstants.TAG, "UPConfig Error:==UP config version：" + version + "But update apk version is " + serverVersion);
            return false;
        }
        return true;
    }

    /**
     * 下载新版本
     */
    public void downloadAPK(final Activity activity, final String downLoadUrl, final String saveDir, final boolean canCancel, final boolean dismissWhenInstall, final String tipWhenDownFinish) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.show();
        View view = View.inflate(activity, R.layout.dialog_download_progress, null);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.download_progressBar);
        final TextView percentTv = (TextView) view.findViewById(R.id.download_percent_tv);
        final TextView percentTotalTv = (TextView) view.findViewById(R.id.download_percent_total);
        final TextView content = (TextView) view.findViewById(R.id.download_content);
        dialog.setCancelable(canCancel);

        Window window = dialog.getWindow();
        window.setContentView(view);
        //解决不居中显示的问题
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        // 设置宽度
        p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.95
        p.gravity = Gravity.CENTER;//设置位置
        //p.alpha = 0.8f;//设置透明度
        window.setAttributes(p);

        final String downLoadUrl2 = "http://122.70.142.80/app.znds.com/down/20190201/dsj3_3.2.1_dangbei.apk";
        EasyVariable.threadPoolUtil.poolExecute(new Runnable() {
            @Override
            public void run() {
                ZSHttpsUtil.downLoadFile(downLoadUrl, saveDir, new IFileDownloadCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(final int progress, final long total, final long current) {
                        MainUIHandler.handler().post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progress);
                                percentTv.setText(progress + "%");
                                percentTotalTv.setText(GBMBKBUtil.getSize(current) + "/" + GBMBKBUtil.getSize(total));
                            }
                        });
                    }

                    @Override
                    public void onLoading(final int progress) {
                    }

                    @Override
                    public void onSuccess(File file) {
                        if (tipWhenDownFinish != null && !tipWhenDownFinish.equals("")) {
                            MainUIHandler.handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    content.setText(tipWhenDownFinish);
                                }
                            });
                        }

                        if (dismissWhenInstall) {
                            dialog.dismiss();
                        }
                        //安装应用程序
                        CoreAPPUtil.getUtilInstance(activity).installApk(file, APPUtil.getPackageName(EasyVariable.mContext));
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        MainUIHandler.handler().post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                ToastAndLogUtil.TL("下载失败");
                                LogUtil.e("下载失败 " + e.toString());
//                        dialog.dismiss();
//                        activity.finish();
//                        System.exit(0);
                            }
                        });
                    }
                });


               /* DownloadFileUtil.getUtilInstance().downLoadFile(downLoadUrl2, ZSConstant.APK_DOWNLOAD_LOCATION, new IFileDownloadCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long l, long l1) {
                    }

                    @Override
                    public void onLoading(final int i) {
                        MainUIHandler.handler().post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(i);
                                percentTv.setText(i + "%");
                                percentTotalTv.setText(i + "/100");
                            }
                        });
                    }

                    @Override
                    public void onSuccess(File file) {
                        MainUIHandler.handler().post(new Runnable() {
                            @Override
                            public void run() {
                                content.setText("下载完成！");
                                progressBar.setProgress(100);
                                percentTv.setText("100%");
                                percentTotalTv.setText("100/100");
                            }
                        });

                        //安装应用程序
                        CoreAPPUtil.getUtilInstance(activity).installApk(Uri.fromFile(file));
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        MainUIHandler.handler().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastAndLogUtil.TL("下载失败");
                                LogUtil.e("下载失败 " + e.toString());
//                        dialog.dismiss();
//                        activity.finish();
//                        System.exit(0);
                            }
                        });
                    }
                });*/
            }
        });
    }
}

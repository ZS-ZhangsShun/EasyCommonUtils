package com.zs.easy.common.utils;

import android.util.Log;

import com.zs.easy.common.constants.EasyConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 应用下载Util
 *
 * @author liuq
 */
public class DownloadFileUtil {
    private final String TAG = this.getClass().getName();

    private static DownloadFileUtil dwonloadAppUtil;

    private DownloadFileUtil() {

    }

    public static DownloadFileUtil getUtilInstance() {
        if (dwonloadAppUtil == null) {
            dwonloadAppUtil = new DownloadFileUtil();
        }
        return dwonloadAppUtil;
    }

    /**
     * 下载文件的方法
     *
     * @param url      下载地址
     * @param target   本地存储路径(例:/mnt/sdcard/starapp/starAppShop/example.apk)
     * @param callBack 回调
     */
    public void downLoadFile(final String url, final String target, final IFileDownloadCallBack callBack) {
        File prefile = new File(target);
        prefile.delete();
        File file = creatNewFile(target);
        if (file != null && file.exists() && file.isFile()) {
            if (callBack != null) {
                callBack.onStart();
            }
            InputStream input = null;
            FileOutputStream output = null;
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                long fileSize = conn.getContentLength();
                double rate = (double) 100 / fileSize;  //最大进度转化为100
                input = conn.getInputStream();
                if (input != null) {
                    output = new FileOutputStream(file);
                    byte buf[] = new byte[1024];
                    long downLoadFileSize = 0;
                    int times = 0;
                    while (downLoadFileSize <= fileSize) {
                        int numread = input.read(buf);
                        if (numread == -1) {
                            break;
                        }
                        output.write(buf, 0, numread);
                        downLoadFileSize += numread;
                        if (times >= 50 && callBack != null) {
                            times = 0;
                            callBack.onLoading((int) (downLoadFileSize * rate));
                            callBack.onLoading((int) (downLoadFileSize * rate), fileSize, downLoadFileSize);
                        }
                        times++;
                    }
                    if (callBack != null) {
                        callBack.onSuccess(file);
                    }
                }
            } catch (Exception e) {
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                if (callBack != null) {
                    callBack.onFailure(e);
                }
                Log.e(TAG, e.toString());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                }
            }
        } else {
            callBack.onFailure(new Exception("target is not a file"));
        }
    }


    /**
     * 创建文件
     *
     * @param target 例"/mnt/sdcard/starapp/app.apk"
     * @return
     */
    public static File creatNewFile(String target) {
        File targetFile = new File(target);
        if (targetFile.exists() && targetFile.isFile()) {
            targetFile.delete();
            targetFile = new File(target);
            return targetFile;
        }
        String[] split = target.split("/");
        String path = "";
        for (int i = 0; i < split.length - 1; i++) {
            path += "/" + split[i];
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        targetFile = new File(target);
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            Log.e(EasyConstants.TAG, "Create file error", e);
        }
        return targetFile;
    }
}

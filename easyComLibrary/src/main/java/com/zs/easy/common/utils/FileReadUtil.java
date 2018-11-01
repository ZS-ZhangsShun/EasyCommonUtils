package com.zs.easy.common.utils;

import android.os.Environment;

import com.zs.easy.common.constants.EasyConstants;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by gongyunit on 2018/11/1.
 */
public class FileReadUtil {
    public static String loadFromSDFile(String fname) {
        fname = "/" + fname;
        String result = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + fname);
            int length = (int) f.length();
            byte[] buff = new byte[length];
            FileInputStream fin = new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result = new String(buff, "UTF-8");
        } catch (Exception e) {
            LogUtil.e(EasyConstants.TAG, e.toString());
        }
        return result;
    }
}

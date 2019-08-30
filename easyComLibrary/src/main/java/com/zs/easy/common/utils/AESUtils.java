package com.zs.easy.common.utils;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * describe: AES加密算法
 */

public class AESUtils {

    /*
     * 加密
     */
    public static String encrypt(String content, String password) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            // 创建AES秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密
            byte[] result = cipher.doFinal(content.getBytes("UTF-8"));
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            LogUtil.i(e.toString());
        }
        return "";
    }

    public static String decrypt(String content, String password) {
        try {
            byte[] result = Base64.decode(content, Base64.DEFAULT);
            // 创建AES秘钥
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            byte[] result2 = cipher.doFinal(result);
            return new String(result2);
        } catch (Exception e) {
            LogUtil.i(e.toString());
        }
        return "";
    }

}
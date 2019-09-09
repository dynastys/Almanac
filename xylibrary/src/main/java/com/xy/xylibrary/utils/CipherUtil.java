package com.xy.xylibrary.utils;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密
 * */
public class CipherUtil {
    private static final String DES = "DES";
    public static String defaultKey = "4942e826d55c12c9119afd6fb0bd79ae";

    /**
     * 加密 使用默认key
     *
     * @param data
     * @return
     */
    public static String desEncrypt(String data) throws Exception  {
        return CipherUtil.desEncrypt(data, defaultKey);
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String desEncrypt(String data, String key) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        System.out.println(data);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherData = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(cipherData, Base64.DEFAULT);
    }

    /**
     * 解密 使用默认key
     *
     * @param data
     * @return
     */
    public static String desDecrypt(String data) throws Exception {
        return CipherUtil.desDecrypt(data, defaultKey);
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String desDecrypt(String data, String key) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] tmpt = Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT);
        byte[] plainData = cipher.doFinal(tmpt);
        return new String(plainData);
    }
}
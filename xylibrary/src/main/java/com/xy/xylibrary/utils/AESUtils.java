package com.xy.xylibrary.utils;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "123456789asdfr0tghybgf2236788912";
    private String ivParameter = "abcdefghijklmnop";
    private static AESUtils instance = null;

    private AESUtils() {
    }

    public static AESUtils getInstance() {
        if (instance == null)
            instance = new AESUtils();
        return instance;
    }

    // 加密CBC
    public static String encrypt(String encData, String secretKey, String vector) throws Exception {

        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。（处于android.util包）
    }


    // 加密CBC
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。
    }

    /**
     * AES加密 ECB
     * @return 已经加密的内容
     */
    public String encrypt2(String sSrc) {
        byte[] raw = sSrc.getBytes();
//        //不足16字节，补齐内容为差值
//        int len = 16 - raw.length % 16;
//        for (int i = 0; i < len; i++) {
//            byte[] bytes = { (byte) len };
//            raw = ArrayUtils.concat(raw, bytes);
//        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Base64.encodeToString(cipher.doFinal(raw), Base64.DEFAULT).replace(" ","").trim();// 此处使用BASE64做转码。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * AES解密 ECB
     * @return 已经解密的内容
     */
    public String decrypt2(String sSrc) {
        byte[] raw = sSrc.getBytes();
        raw = ArrayUtils.noPadding(raw, -1);
        try {
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decryptData = cipher.doFinal(raw);
//            int len = 2 +  byteToInt(decryptData[4]) + 3;
//            return ArrayUtils.noPadding(decryptData, len);
            String originalString = new String(decryptData, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static int byteToInt(byte b) {
        return (b) & 0xff;
    }

    // 解密CBC
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    // 解密CBC
    public String decrypt(String sSrc, String key) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
}

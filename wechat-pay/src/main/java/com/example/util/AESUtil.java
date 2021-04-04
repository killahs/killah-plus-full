package com.example.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: AES工具类
 */
@Slf4j
public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * AES加密
     *
     * @param data
     * @param password
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, String password){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
            SecretKeySpec key = new SecretKeySpec((password).toLowerCase().getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return base64Encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param base64Data
     * @param password
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
            SecretKeySpec key = new SecretKeySpec(MD5(password).toLowerCase().getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = base64Decode(base64Data);
            byte[] doFinal = cipher.doFinal(decode);
            return new String(doFinal, "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * MD5
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Base64 解码
     *
     * @param encoded
     * @return
     */
    public static byte[] base64Decode(String encoded) {
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encoded);
    }

    /**
     * Base64 编码
     */
    public static String base64Encode(byte[] data) {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

}
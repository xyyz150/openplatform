package com.github.opf.utils;

import com.github.opf.Constants;
import com.github.opf.MessageFormat;
import com.github.opf.config.SystemParameterNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by xyyz150
 */
public class OpfUtils {
    protected static Logger logger = LoggerFactory.getLogger(OpfUtils.class);

    /**
     * 10分钟有效期
     */
    private final static long MAX_EXPIRE = 10 * 60 * 1000;

    public static String getBody(HttpServletRequest request) throws Exception {
        ServletInputStream stream = null;
        try {
            stream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Constants.UTF8));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } catch (Exception e) {
            logger.error("获取body错误：" + e.toString(), e);
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String sign(Map<String, String> params, String body, String secretKey) throws IOException {
        // 1. 第一步，确保参数已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 2. 第二步，把所有参数名和参数值拼接在一起(包含body体)
        StringBuilder sb = new StringBuilder(secretKey); // 前面加上secretKey
        for (String key : keys) {
            if (!"sign".equals(key)) {
                String value = params.get(key);
                if (StringUtils.hasText(key) && StringUtils.hasText((value))) {
                    sb.append(key).append(value);
                }
            }
        }
        sb.append(body); // 拼接body体
        sb.append(secretKey);// 最后加上secretKey

        // 3. 第三步，使用加密算法进行加密
        byte[] bytes = encryptMD5(sb.toString());

        // 4. 把二进制转换成大写的十六进制
        String sign = byte2Hex(bytes);

        return sign;
    }

    public static boolean checkTimestamp(Date timestamp) {
        return (System.currentTimeMillis() - timestamp.getTime()) <= MAX_EXPIRE;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sign.append("0");
            }

            sign.append(hex.toUpperCase());
        }

        return sign.toString();
    }

    private static byte[] encryptMD5(String message) throws IOException {
        try {
            MessageDigest md5Instance = MessageDigest.getInstance("MD5");
            return md5Instance.digest(message.getBytes("UTF-8"));
        } catch (GeneralSecurityException var3) {
            throw new IOException(var3.toString());
        }
    }

    public static String urlDecode(String old) {
        try {
            return URLDecoder.decode(old, Constants.UTF8);
        } catch (Exception e) {
            logger.error(old + ":url解码错误：" + e, e);
        }
        return old;
    }
}

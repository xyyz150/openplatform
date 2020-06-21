package com.github.opf.utils;

import com.github.opf.Constants;
import com.github.opf.MessageFormat;
import com.github.opf.config.SystemParameterNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by xyyz150
 */
public class OpfUtils {
    protected static Logger logger = LoggerFactory.getLogger(OpfUtils.class);

    public static MessageFormat getFormat(HttpServletRequest request) {
        String format = OpfUtils.urlDecode(request.getParameter(SystemParameterNames.getFormat()));
        return MessageFormat.getFormat(format);
    }

    public static String getBody(HttpServletRequest request) throws Exception {
        ServletInputStream stream=null;
        try {
            stream=request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Constants.UTF8));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } catch (Exception e) {
            logger.error("获取body错误：" + e.toString());
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String sign(Map<String, String> params, String body, String secretKey) {
        // 1. 第一步，确保参数已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 2. 第二步，把所有参数名和参数值拼接在一起(包含body体)
        String joinedParams = joinRequestParams(params, body, secretKey, keys);
        System.out.println(joinedParams);
        // 3. 第三步，使用加密算法进行加密
        byte[] abstractMesaage = digest(joinedParams);

        // 4. 把二进制转换成大写的十六进制
        String sign = byte2Hex(abstractMesaage);

        return sign;

    }

    private static String joinRequestParams(Map<String, String> params, String body, String secretKey, String[] sortedKes) {
        StringBuilder sb = new StringBuilder(secretKey); // 前面加上secretKey

        for (String key : sortedKes) {
            if ("sign".equals(key)) {
                continue; // 签名时不计算sign本身
            } else {
                String value = params.get(key);
                if (isNotEmpty(key) && isNotEmpty(value)) {
                    sb.append(key).append(value);
                }
            }
        }
        sb.append(body); // 拼接body体
        sb.append(secretKey);// 最后加上secretKey
        return sb.toString();
    }

    private static boolean isNotEmpty(String s) {
        return null != s && !"".equals(s);
    }

    private static String byte2Hex(byte[] bytes) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (byte byte0 : bytes) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    private static byte[] digest(String message) {
        try {
            MessageDigest md5Instance = MessageDigest.getInstance("MD5");
            md5Instance.update(message.getBytes("UTF-8"));
            return md5Instance.digest();
        } catch (UnsupportedEncodingException e) {
            //TODO
            return null;
        } catch (NoSuchAlgorithmException e) {
            //TODO
            return null;
        }
    }

    public static String urlDecode(String old) {
        try {
            return URLDecoder.decode(old, Constants.UTF8);
        } catch (Exception e) {
            logger.error(old + ":url解码错误：" + e.toString());
        }
        return old;
    }
}

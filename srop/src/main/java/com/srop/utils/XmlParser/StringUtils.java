package com.srop.utils.XmlParser;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public class StringUtils {
    public static boolean isNumeric(Object obj) {
        if(obj == null) {
            return false;
        } else {
            char[] chars = obj.toString().toCharArray();
            int length = chars.length;
            if(length < 1) {
                return false;
            } else {
                int i = 0;
                if(length > 1 && chars[0] == 45) {
                    i = 1;
                }

                while(i < length) {
                    if(!Character.isDigit(chars[i])) {
                        return false;
                    }

                    ++i;
                }

                return true;
            }
        }
    }

    public static String firstToUp(String str){
        if(Character.isUpperCase(str.charAt(0))){
            return str;
        }else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }
}

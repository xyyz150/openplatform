package com.qimenapi.util;

import java.util.List;

/**
 * Created by xyyz150 on 2015/6/25.
 */
public class StringHelp {
    public static boolean IsNullOrEmpty(String str) {
        if (str == null) return true;
        if (str.trim().isEmpty()) return true;

        return false;
    }
    public static boolean SearchIsContain(String searchStr,List<String> list) {
        if (list == null) return false;
        for (String str : list) {
            if (searchStr.indexOf(str) > -1) {
                return true;
            }
        }
        return false;
    }
    public static  void RemoveEmpty(List<String> list)
    {
        boolean result=true;
        while (result)
        {
            result=list.remove("");
        }
    }
}

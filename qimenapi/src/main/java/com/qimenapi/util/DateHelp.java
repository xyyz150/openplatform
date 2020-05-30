package com.qimenapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xyyz150 on 2015/6/25.
 */
public class DateHelp {
    static Date DefaultDate;

    public static Date getDefaultDate() {
        if (DefaultDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                DefaultDate = sdf.parse("1949-10-01 00:00:00");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return DefaultDate;
    }


    /*
    *
    * */
    public static String format(Date time) {
        // Date d = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    public static String format(Date time,String patten) {
        // Date d = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(patten);
        return formatter.format(time);
    }

    public static Date parseDate(String time) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        if (time.length() == 10) {
            time = time + " 00:00:00";
        }
        try {
            return sdf.parse(time);
        } catch (Exception e) {
            //return DateHelp.getDefaultDate();
            e.printStackTrace();
            throw new IllegalArgumentException("入参datetime：" + time + "解析异常，请检查格式必须为：" + dateFormat);
        }
    }


    /*
    * 转化年月日
    * */
    public static Date ToDate(Object o) throws Exception {
        Date date = null;
        if (o == null) return date;
        String strdate = o.toString();
        if (StringHelp.IsNullOrEmpty(strdate)) {
            return date;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(strdate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        if (date == null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            try {
                date = sdf1.parse(strdate);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Exception(ex.getMessage());
            }
        }
        return date;
    }
}

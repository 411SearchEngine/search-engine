package com.search.engine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuh
 * DATE 2019/12/5 23:58.
 * version 1.0
 */
public class DateUtil {

    /**
     * 获取yyyyMMdd格式的日期
     * @param date
     * @return
     */
    public static String getyyyyMMdd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }


    /**
     * 获取yyyyMMdd格式的日期
     * @param date
     * @return
     */
    public static String getyyyyMMdd(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf1.format(parse);
    }



    /**
     * 获取下一天的信息
     *
     * @return yyyyMMdd
     */
    public static String getNextDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        parse = DateUtil.addDays(parse, 1);

        return sdf.format(parse);
    }


    /**
     * 功能：当前时间增加天数。
     * @param date 日期
     * @param days 正值时时间延后，负值时时间提前。
     * @return Date
     */
    public static Date addDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE)+days);
        return new Date(c.getTimeInMillis());
    }

    public static Date getDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = new Date();
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getStandardDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

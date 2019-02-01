package com.haoyu.swift.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haoyu on 2018/9/1.
 */
public class DateUtils {

    private static final SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

    public static Date dateToYesterday(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        return instance.getTime();
    }

    public static Date dateToYesterdayBegin(Date date) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Date time = instance.getTime();
        String format = daySdf.format(time);
        return daySdf.parse(format);
    }

    public static Date dateBeforeYesterdayBegin(Date date) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -2);
        Date time = instance.getTime();
        String format = daySdf.format(time);
        return daySdf.parse(format);
    }

    public static String dateToYesterDayStr(Date date) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Date time = instance.getTime();
        return daySdf.format(time);
    }

    public static String dayBeforeYesterday(Date date) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -2);
        Date time = instance.getTime();
        return daySdf.format(time);
    }

    public static String dayRegulation(Date date , int count) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, count);
        Date time = instance.getTime();
        return daySdf.format(time);
    }

    public static Date strToDateBegin(String dateStr) throws ParseException {
        return daySdf.parse(dateStr);
    }

    public static Date strToNextDay(String dateStr) throws ParseException {
        Date parse = daySdf.parse(dateStr);
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    public static String dateToStr(Date date) {
        return daySdf.format(date);
    }

    public static boolean nowAfterNextTomorrow(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, 2);
        Date nextTomorrow = instance.getTime();
        return new Date().after(nextTomorrow);
    }

    public static boolean nowAfterNextTomorrow(String dateStr) throws ParseException {
        Date date = DateUtils.strToDateBegin(dateStr);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        instance.add(Calendar.MINUTE , 60 * 22 );
        Date nextTomorrow = instance.getTime();
        return new Date().after(nextTomorrow);
    }

    public static Date zToUTC(String ztime) throws ParseException {
        ztime = ztime.replace("Z", " UTC");
        return format.parse(ztime);
    }

    public static Date timeToDayHour(Date time , String hour) throws ParseException {
        String day = dateToStr(time);
        return timeSdf.parse(day + " " + hour);
    }

}

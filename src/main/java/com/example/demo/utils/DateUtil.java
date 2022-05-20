package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date largeDay = sdf.parse("2023-01-01 01:59:59.999");
        Date smallDay = sdf.parse("2022-12-31 09:00:00.000");
        System.out.println(distanceDays(largeDay, smallDay));

        Date date = new Date();
        Date start = getStartDayOfWeek(date);
        Date end = getEndDayOfWeek(start);
        System.out.println(sdf.format(start));
        System.out.println(sdf.format(end));
    }

    /**
     * 两个日期对比间隔天数
     * @param largeDay
     * @param smallDay
     * @return
     */
    private static int distanceDays(Date largeDay, Date smallDay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        largeDay = sdf.parse(sdf.format(largeDay));
        smallDay = sdf.parse(sdf.format(smallDay));
        int day = (int) ((largeDay.getTime() - smallDay.getTime()) / (1000 * 60 * 60 * 24));
        return day;
    }

    /**
     * 获取当前日期的周开始时间
     * @param date 当前日期
     * @return
     */
    public static Date getStartDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        calendar.add(Calendar.DATE, 2 - dayOfWeek);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取当前日期的周结束时间
     * @param date 当前日期的周开始时间
     * @return
     */
    public static Date getEndDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartDayOfWeek(date));
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        return getDayEndTime(calendar.getTime());
    }

    //获取某个日期的开始时间
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if(null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    //获取某个日期的结束时间
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if(null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}

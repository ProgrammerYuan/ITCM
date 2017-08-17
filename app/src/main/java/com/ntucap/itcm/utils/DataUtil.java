package com.ntucap.itcm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ProgrammerYuan on 04/06/17.
 */

public class DataUtil {
    private static final String[] MONTH_ARRAY = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] MONTH_ACRONYM = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static final int TIME_SECOND = 1000;
    public static final int TIME_MINUTE = 60 * TIME_SECOND;
    public static final int TIME_HOUR = 60 * TIME_MINUTE;
    public static final int TIME_DAY = 24 * TIME_HOUR;

    private static final Pattern sNumberPattern = Pattern.compile("-?\\d+");

    private static final int[] MONTH_DAYS_NUM = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static float toMinutes(long duration) {
        return ((float)duration) / TIME_MINUTE;
    }

    public static String getMonth(int index) {
        if(index >= 12) throw new IndexOutOfBoundsException("There's only 12 months.");
        return MONTH_ARRAY[index];
    }

    public static String getMonthAcronym(int index) {
        if(index >= 12) throw new IndexOutOfBoundsException("There's only 12 months.");
        return MONTH_ACRONYM[index];
    }

    public static String getFormalMonthAcronym(int index) {
        return getMonthAcronym(index) + ".";
    }

    public static int getMonthDaysNum(int year, int month) {
        if(month > 11) throw new IndexOutOfBoundsException("There's only 12 months.");
        if(month == 1) {
            if(isLeapYear(year)) return MONTH_DAYS_NUM[month] + 1;
            return MONTH_DAYS_NUM[month];
        } else {
            return MONTH_DAYS_NUM[month];
        }
    }

    public static int extractOneNumberFromString(String str) {

        Matcher m = sNumberPattern.matcher(str);
        if(m.find()) return Integer.parseInt(m.group());
        return 0;
    }

    public static boolean isLeapYear(int year) {
        if(year % 4 != 0) return false;
        if(year % 100 == 0 && year % 400 != 0) return false;
        return true;
    }

    /**
     *
     * @param date Time to be transformed
     * @return String array in order of [Year, Month&date, time]
     *
     * Example Return:
     *     ["2017", "08-16", "13:50:17"]
     */
    public static String[] getYMDTFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM-dd HH:mm:ss");
        return dateFormat.format(date).split(" ");
    }
}

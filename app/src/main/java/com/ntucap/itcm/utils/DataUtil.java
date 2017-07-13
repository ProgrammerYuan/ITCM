package com.ntucap.itcm.utils;

/**
 * Created by ProgrammerYuan on 04/06/17.
 */

public class DataUtil {
    private static final String[] MONTH_ARRAY = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] MONTH_ACRONYM = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec"};

    private static final int[] MONTH_DAYS_NUM = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

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

    public static boolean isLeapYear(int year) {
        if(year % 4 != 0) return false;
        if(year % 100 == 0 && year % 400 != 0) return false;
        return true;
    }
}

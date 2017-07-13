package com.ntucap.itcm.utils;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class DBUtil {

    public static final String COMMA_SEP = ",";

    public static final String SQL_TEXT_TYPE = " TEXT" + COMMA_SEP;
    public static final String SQL_TINYINT_TYPE = " TINYINT";
    public static final String SQL_TINYINT_TYPE_WITH_SEP = " TINYINT" + COMMA_SEP;
    public static final String SQL_INTEGER_TYPE = " INTEGER" + COMMA_SEP;
    public static final String SQL_INTEGER_PRIMARY_TYPE = " INTEGER PRIMARY KEY" + COMMA_SEP;
    public static final String SQL_LONG_TYPE = " LONG" + COMMA_SEP;
    public static final String SQL_VARCHAR255_TYPE = " VARCHAR(255)" + COMMA_SEP;
    public static final String SQL_VARCHAR_TYPE_FORMAT = " VARCHAR(%d)" + COMMA_SEP;
    public static final String SQL_DOUBLE_TYPE = " DOUBLE" + COMMA_SEP;
    public static final String SQL_DATE_TYPE = " INTEGER" + COMMA_SEP;
    public static final String SQL_TIMESTAMP_TYPE = " TIMESTAMP" + COMMA_SEP;
    public static final String SQL_AUTO_INCREMENT = " AUTO_INCREMENT" + COMMA_SEP;

    public static String stringToSQLWrapper(String str) {
        return '`' + str + '`';
    }

    public static String intToSQLWrapper(int integer) {
        return stringToSQLWrapper(String.valueOf(integer));
    }

    public static String longToSQLWrapper(long longInt) {
        return stringToSQLWrapper(String.valueOf(longInt));
    }
}

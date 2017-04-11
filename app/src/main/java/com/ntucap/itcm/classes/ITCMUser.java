package com.ntucap.itcm.classes;

import android.database.Cursor;

import com.alibaba.fastjson.JSONObject;
import com.ntucap.itcm.db.ITCMDB;

/**
 * Created by ProgrammerYuan on 10/04/17.
 */

public class ITCMUser extends ITCMObject {

    private int mId;
    private int mAge;
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private String mGender;

    public static final String TABLE_NAME = "`users`";
    public static final String COLUMN_NAME_ID = "`id`";
    public static final String COLUMN_NAME_EMAIL = "`email`";
    public static final String COLUMN_NAME_PASSWORD = "`password`";
    public static final String COLUMN_NAME_FIRSTNAME = "`firstname`";
    public static final String COLUMN_NAME_LASTNAME = "`lastname`";
    public static final String COLUMN_NAME_AGE = "`age`";
    public static final String COLUMN_NAME_GENDER = "`gender`";

    public ITCMUser() {}

    public ITCMUser(JSONObject json) {

    }

    public ITCMUser(Cursor cursor) {

    }

    @Override
    public String getCreateTableSQL() {
        return "CREATE TABLE " + TABLE_NAME + " （" +
                COLUMN_NAME_ID + ITCMDB.SQL_INTEGER_PRIMARY_TYPE +
                COLUMN_NAME_EMAIL + ITCMDB.SQL_VARCHAR255_TYPE +
                COLUMN_NAME_PASSWORD + ITCMDB.SQL_VARCHAR255_TYPE +
                COLUMN_NAME_FIRSTNAME + ITCMDB.SQL_VARCHAR255_TYPE +
                COLUMN_NAME_LASTNAME + ITCMDB.SQL_VARCHAR255_TYPE +
                COLUMN_NAME_AGE + ITCMDB.SQL_INTEGER_TYPE +
                COLUMN_NAME_GENDER + ITCMDB.SQL_INTEGER_TYPE + ");";
    }

    @Override
    public String getUpdateTableSQL() {
        return null;
    }

    @Override
    public String getDeleteTableSQL() {
        return "DELETE FROM ";
    }
}

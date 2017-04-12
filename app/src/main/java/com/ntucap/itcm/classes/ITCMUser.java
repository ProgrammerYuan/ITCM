package com.ntucap.itcm.classes;

import android.content.ContentValues;
import android.database.Cursor;

import com.alibaba.fastjson.JSONObject;
import com.ntucap.itcm.utils.DBUtility;

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
    private boolean mIsCurrentUser = false;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_FIRSTNAME = "firstname";
    public static final String COLUMN_NAME_LASTNAME = "lastname";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_GENDER = "gender";
    public static final String COLUMN_NAME_CURRENT_USER = "current_user";

    public ITCMUser() {
        super();
    }

    public ITCMUser(JSONObject json) {

    }

    public ITCMUser(Cursor cursor) {

    }

    @Override
    public String getCreateTableSQL() {
        return "CREATE TABLE " + DBUtility.stringToSQLWrapper(TABLE_NAME) + " （" +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_ID) + DBUtility.SQL_INTEGER_PRIMARY_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_EMAIL) + DBUtility.SQL_VARCHAR255_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_PASSWORD) + DBUtility.SQL_VARCHAR255_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_FIRSTNAME) + DBUtility.SQL_VARCHAR255_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_LASTNAME) + DBUtility.SQL_VARCHAR255_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_AGE) + DBUtility.SQL_INTEGER_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_GENDER) + DBUtility.SQL_INTEGER_TYPE +
                DBUtility.stringToSQLWrapper(COLUMN_NAME_CURRENT_USER) + DBUtility.SQL_TINYINT_TYPE +
                ");";
    }

    @Override
    public ContentValues getUpdateContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_ID, mId);
        cv.put(COLUMN_NAME_EMAIL, mEmail);
        cv.put(COLUMN_NAME_PASSWORD, mPassword);
        cv.put(COLUMN_NAME_FIRSTNAME, mFirstName);
        cv.put(COLUMN_NAME_LASTNAME, mLastName);
        cv.put(COLUMN_NAME_AGE, mAge);
        cv.put(COLUMN_NAME_GENDER, mGender);
        cv.put(COLUMN_NAME_CURRENT_USER, mIsCurrentUser);
        return cv;
    }

    @Override
    public String getDeleteTableSQL() {
        return "DELETE FROM " + DBUtility.stringToSQLWrapper(TABLE_NAME) +
                "where " + DBUtility.stringToSQLWrapper(COLUMN_NAME_ID) + " = " +
                DBUtility.intToSQLWrapper(mId) + ";";
    }
}

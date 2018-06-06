package com.ntucap.itcm.classes;

import android.content.ContentValues;
import android.database.Cursor;

import com.alibaba.fastjson.JSONObject;
import com.ntucap.itcm.utils.DBUtil;
import com.ntucap.itcm.utils.ValidationUtil;

import org.jsoup.helper.StringUtil;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ProgrammerYuan on 10/04/17.
 */

public class ITCMUser extends ITCMObject implements Serializable{

    private long mId;
    private int mAge;
    private int mHeight;
    private int mWeight;
    private boolean mIsCurrentUser = false;
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private String mGender;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_FIRSTNAME = "firstname";
    public static final String COLUMN_NAME_LASTNAME = "lastname";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_GENDER = "gender";
    public static final String COLUMN_NAME_WEIGHT = "weight";
    public static final String COLUMN_NAME_HEIGHT = "height";
    public static final String COLUMN_NAME_CURRENT_USER = "current_user";

    public ITCMUser() {
        super();
    }

    public ITCMUser(JSONObject json) {
        this();
        updateByJson(json);
    }

    public ITCMUser(ITCMUser user) {

    }

    public ITCMUser(Cursor cursor) {
        this();
        mId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
        mEmail = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EMAIL));
        mPassword = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PASSWORD));
        mFirstName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRSTNAME));
        mLastName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LASTNAME));
        mAge = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE));
        mGender = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GENDER));
        mIsCurrentUser = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_CURRENT_USER)) > 0;
    }

    /** For Test Cases
     *
     */
    public ITCMUser(HashMap<String, String> params) {
        this();
        mEmail = ValidationUtil.validateHashmapGet(params, "email", "");
        mPassword = ValidationUtil.validateHashmapGet(params, "password", "");
        mFirstName = ValidationUtil.validateHashmapGet(params, "firstName", "");
        mLastName = ValidationUtil.validateHashmapGet(params, "lastName", "");
        mGender = ValidationUtil.validateHashmapGet(params, "gender", "");
        mWeight = Integer.valueOf(params.get("weight"));
        mHeight = Integer.valueOf(params.get("height"));
        mAge = Integer.parseInt(ValidationUtil.validateHashmapGet(params, "age", "0"));
    }

    public void update(ITCMUser user) {
        mEmail = user.getEmail();
        mPassword = user.getPassword();
        mFirstName = user.getFirstName();
        mLastName = user.getLastName();
        mGender = user.getGender();
        mWeight = user.getWeight();
        mHeight = user.getHeight();
        mAge = user.getAge();
    }

    public void updateInfo(HashMap<String, String> params) {
        mFirstName = params.get("firstName");
        mLastName = params.get("lastName");
        mGender = params.get("gender");
        mWeight = Integer.valueOf(params.get("weight"));
        mHeight = Integer.valueOf(params.get("height"));
        mAge = Integer.valueOf(params.get("age"));
    }

    public void updateByJson(JSONObject json) {
        mPassword = json.getString("password");
        mFirstName = json.getString("firstName");
        mLastName = json.getString("lastName");
        mGender = json.getString("gender");
        mWeight = json.getIntValue("weight");
        mHeight = json.getIntValue("height");
        mAge = json.getIntValue("age");
        mEmail = json.getString("email");
    }

    public void setIsCurrentUser(boolean isCurrentUser) {
        this.mIsCurrentUser = isCurrentUser;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public long getID() {
        return mId;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFullName() {
        return getLastName() + " " + getFirstName();
    }

    public int getAge() {
        return mAge;
    }

    public String getGender() {
        return mGender;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWeight() {
        return mWeight;
    }

    public String getCombinedDetail() {
        String ret = "Email: " + getEmail();
        ret += "\n" + getGender() + " · ";
        ret += String.valueOf(getAge()) + " years old";
        return ret;
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("email", this.mEmail);
        data.put("password", this.mPassword);
        data.put("firstName", this.mFirstName);
        data.put("lastName", this.mLastName);
        data.put("age", String.valueOf(this.mAge));
        data.put("gender", this.mGender);
        data.put("weight", String.valueOf(this.mWeight));
        data.put("height", String.valueOf(this.mHeight));
        return data;
    }

    @Override
    public String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + DBUtil.stringToSQLWrapper(TABLE_NAME) + "(" +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_ID) + DBUtil.SQL_INTEGER_PRIMARY_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_EMAIL) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_PASSWORD) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_FIRSTNAME) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_LASTNAME) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_AGE) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_GENDER) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_WEIGHT) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_HEIGHT) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_CURRENT_USER) + DBUtil.SQL_TINYINT_TYPE +
                " UNIQUE(" + COLUMN_NAME_EMAIL + "));";
    }

    @Override
    public ContentValues getUpdateContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_EMAIL, mEmail);
        cv.put(COLUMN_NAME_PASSWORD, mPassword);
        cv.put(COLUMN_NAME_FIRSTNAME, mFirstName);
        cv.put(COLUMN_NAME_LASTNAME, mLastName);
        cv.put(COLUMN_NAME_AGE, mAge);
        cv.put(COLUMN_NAME_GENDER, mGender);
        cv.put(COLUMN_NAME_WEIGHT, mWeight);
        cv.put(COLUMN_NAME_HEIGHT, mHeight);
        cv.put(COLUMN_NAME_CURRENT_USER, mIsCurrentUser);
        return cv;
    }

    @Override
    public String getDeleteTableSQL() {
        return "DELETE FROM " + DBUtil.stringToSQLWrapper(TABLE_NAME) +
                "where " + DBUtil.stringToSQLWrapper(COLUMN_NAME_ID) + " = " +
                DBUtil.longToSQLWrapper(mId) + ";";
    }
}

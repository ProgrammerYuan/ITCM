package com.ntucap.itcm.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.Preference;

import com.alibaba.fastjson.JSONObject;
import com.ntucap.itcm.utils.DBUtil;

import java.util.HashMap;

/**
 * Created by ProgrammerYuan on 10/08/17.
 */

public class ITCMUserPreference extends ITCMObject {

    private int mIndoorAirTemp, mIndoorHumidity, mComfortLevelFrom, mComfortLevelTo;
    private String mUserEmail;

    public static final String TABLE_NAME = "user_preferences";
    public static final String COLUMN_NAME_EMAIL = "email";
    private static final String COLUMN_NAME_AIR_TEMP = "air_temp";
    private static final String COLUMN_NAME_HUMIDITY = "humidity";
    private static final String COLUMN_NAME_COMFORT_FROM = "comfort_from";
    private static final String COLUMN_NAME_COMFORT_TO = "comfort_to";

    public ITCMUserPreference() {
        this(25, 50, 1, -1);
    }

    public ITCMUserPreference(Cursor cursor) {
        super();
        mUserEmail = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EMAIL));
        mIndoorAirTemp = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AIR_TEMP));
        mIndoorHumidity = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_HUMIDITY));
        mComfortLevelFrom = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_COMFORT_FROM));
        mComfortLevelTo = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_COMFORT_TO));
    }

    public ITCMUserPreference(
            int indoorAirTemp,
            int indoorHumidity,
            int comfortLevelFrom,
            int comfortLevelTo
    ) {
        super();
        mIndoorAirTemp = indoorAirTemp;
        mIndoorHumidity = indoorHumidity;
        mComfortLevelFrom = comfortLevelFrom;
        mComfortLevelTo = comfortLevelTo;
    }


    /**
     *
     * @param json
     *
     * "preferredIndoorAirTemp": "25",
     * "preferredIndoorAirHumidity": "50",
     * "acceptableComfortLevelTo": "-1",
     * "acceptableComfortLevelFrom": "1"
     */
    public ITCMUserPreference(JSONObject json) {
        mIndoorAirTemp = json.getIntValue("preferredIndoorAirTemp");
        mIndoorHumidity = json.getIntValue("preferredIndoorAirHumidity");
        mComfortLevelFrom = json.getIntValue("acceptableComfortLevelFrom");
        mComfortLevelTo = json.getIntValue("acceptableComfortLevelTo");
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("preferredIndoorAirTemp", String.valueOf(mIndoorAirTemp));
        params.put("preferredIndoorAirHumidity", String.valueOf(mIndoorHumidity));
        params.put("acceptableComfortLevelFrom", String.valueOf(mComfortLevelFrom));
        params.put("acceptableComfortLevelTo", String.valueOf(mComfortLevelTo));
        return params;
    }

    public void setEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public void setIndoorAirTemp(int indoorAirTemp) {
        mIndoorAirTemp = indoorAirTemp;
    }

    public void setIndoorHumidity(int indoorHumidity) {
        mIndoorHumidity = indoorHumidity;
    }

    public void setComfortLevelFrom(int comfortLevelFrom) {
        mComfortLevelFrom = comfortLevelFrom;
    }

    public void setComfortLevelTo(int comfortLevelTo) {
        mComfortLevelTo = comfortLevelTo;
    }

    public String getEmail() {
        return mUserEmail;
    }

    public int getIndoorAirTemp() {
        return mIndoorAirTemp;
    }

    public int getIndoorHumidity() {
        return mIndoorHumidity;
    }

    public int getComfortLevelFrom() {
        return mComfortLevelFrom;
    }

    public int getComfortLevelTo() {
        return mComfortLevelTo;
    }

    @Override
    public String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + DBUtil.stringToSQLWrapper(TABLE_NAME) + "(" +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_EMAIL) + DBUtil.SQL_VARCHAR255_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_AIR_TEMP) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_HUMIDITY) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_COMFORT_FROM) + DBUtil.SQL_INTEGER_TYPE +
                DBUtil.stringToSQLWrapper(COLUMN_NAME_COMFORT_TO) + DBUtil.SQL_INTEGER_TYPE_WITHOUT_SEP +
                ");";
    }

    @Override
    public ContentValues getUpdateContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_EMAIL, mUserEmail);
        cv.put(COLUMN_NAME_AIR_TEMP, mIndoorAirTemp);
        cv.put(COLUMN_NAME_HUMIDITY, mIndoorHumidity);
        cv.put(COLUMN_NAME_COMFORT_FROM, mComfortLevelFrom);
        cv.put(COLUMN_NAME_COMFORT_TO, mComfortLevelTo);
        return cv;
    }

    @Override
    public String getDeleteTableSQL() {
        return "DELETE FROM " + DBUtil.stringToSQLWrapper(TABLE_NAME) +
                "where " + DBUtil.stringToSQLWrapper(COLUMN_NAME_EMAIL) + " = " +
                DBUtil.stringToSQLWrapper(mUserEmail) + ";";
    }
}

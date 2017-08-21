package com.ntucap.itcm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.classes.ITCMObject;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.classes.ITCMUserPreference;
import com.ntucap.itcm.utils.DBUtil;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class ITCMDB {

    private static ITCMSQLHelper mDBHelper;
    private static ArrayList<Class<? extends ITCMObject>> classes;
    public static SQLiteDatabase db;

    public ITCMDB() {
    }

    public static void init(Context context) {
        classes = new ArrayList<>();
        classes.add(ITCMUser.class);
        classes.add(ITCMUserPreference.class);
        mDBHelper = new ITCMSQLHelper(context, classes);
        db = mDBHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getDB() {
        if (db == null) return mDBHelper.getWritableDatabase();
        return db;
    }

    private static boolean validateCursor(Cursor cursor, int rowCountThreshold) {
        return cursor != null && cursor.getCount() > rowCountThreshold;
    }

    public static long signout() {
        long id = ITCMApplication.getCurrentUser().getID();
        return setCurrentUser(id, false);
    }

    public static long saveUser(ITCMUser user) {
        if (user == null) return -1;
        long id = getDB().insertWithOnConflict(ITCMUser.TABLE_NAME, null, user.getUpdateContentValue(),SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) return updateUser(user);
        return id;
    }

    public static long updateUser(ITCMUser user) {
        return getDB().update(ITCMUser.TABLE_NAME, user.getUpdateContentValue(),
                ITCMUser.COLUMN_NAME_ID + " = " + user.getID(), null);
    }

    private static long setCurrentUser(long id, boolean isCurrentUser) {
        ContentValues cv = new ContentValues();
        cv.put(ITCMUser.COLUMN_NAME_CURRENT_USER, isCurrentUser);
        return getDB().update(ITCMUser.TABLE_NAME, cv,
                ITCMUser.COLUMN_NAME_ID + " = " + id, null);
    }

    public static ITCMUser getSingleUser(long id) {
        if (id == -1) return null;
        ITCMUser user = null;
        Cursor cursor = getSingleUserCursor(id);
        if (validateCursor(cursor, 0)) {
            cursor.moveToFirst();
            user = new ITCMUser(cursor);
            cursor.close();
        }
        return user;
    }

    public static ITCMUser getCurrentUser() {
        ITCMUser user = null;
        String sql = "select * from " + DBUtil.stringToSQLWrapper(ITCMUser.TABLE_NAME) +
                " where " + ITCMUser.COLUMN_NAME_CURRENT_USER + " = 1;";
        Cursor cursor = getDB().rawQuery(sql, null);
        if (validateCursor(cursor, 0)) {
            cursor.moveToFirst();
            user = new ITCMUser(cursor);
            cursor.close();
        }
        return user;
    }

    public static Cursor getSingleUserCursor(long id) {
        if (getDB() != null) {
            String sql = "select * from " + DBUtil.stringToSQLWrapper(ITCMUser.TABLE_NAME) +
                    " where " + ITCMUser.COLUMN_NAME_ID + " = " + id + ";";
            return getDB().rawQuery(sql, null);
        }
        return null;
    }

    public static long saveUserPreference(ITCMUserPreference preference) {
        if (preference == null) return -1;
        long id = getDB().insertWithOnConflict(ITCMUserPreference.TABLE_NAME, null, preference.getUpdateContentValue(),SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) return updatePreference(preference);
        return id;
    }

    public static long updatePreference(ITCMUserPreference preference) {
        return getDB().update(ITCMUserPreference.TABLE_NAME, preference.getUpdateContentValue(),
                ITCMUserPreference.COLUMN_NAME_EMAIL + " = " + preference.getEmail(), null);
    }

    public static ITCMUserPreference getCurrentUserPreference() {
        return getUserPreference(ITCMApplication.getCurrentUserEmail());
    }

    public static ITCMUserPreference getUserPreference(String userEmail) {
        if(userEmail == null) return null;
        ITCMUserPreference preference = null;
        Cursor cursor = getUserPreferenceCursor(userEmail);
        if (validateCursor(cursor, 0)) {
            cursor.moveToFirst();
            preference = new ITCMUserPreference(cursor);
            cursor.close();
        }
        return preference;
    }

    public static Cursor getUserPreferenceCursor(String userEmail) {
        Cursor cursor;
        if(getDB() != null) {
            String sql = "select * from " + DBUtil.stringToSQLWrapper(ITCMUserPreference.TABLE_NAME) +
                    " where " + ITCMUserPreference.COLUMN_NAME_EMAIL + " = " + DBUtil.stringToSQLWrapper(userEmail);
            return getDB().rawQuery(sql, null);
        }
        return null;
    }
}
package com.ntucap.itcm.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ntucap.itcm.classes.ITCMObject;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class ITCMSQLHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "ITCMSQLHELPER";

    private ArrayList<Class<? extends ITCMObject>> classes;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ITCMDatabase.db";

    public ITCMSQLHelper(Context context, ArrayList<Class<? extends ITCMObject>> classes) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.classes = classes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            for (Class<? extends ITCMObject> classObject : classes)
                db.execSQL(classObject.newInstance().getCreateTableSQL());
        } catch (SQLException e) {
            Log.e(LOG_TAG, "SQL EXCEPTION:");
            e.printStackTrace();
        } catch (InstantiationException e) {
            Log.e(LOG_TAG, "INSTANTIATION EXCEPTION:");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, "ILLEGAL ACCESS EXCEPTION:");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

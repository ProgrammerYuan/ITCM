package com.ntucap.itcm.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ntucap.itcm.classes.ITCMObject;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class ITCMSQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ITCMDatabase.db";
    private ArrayList<? extends ITCMObject> classes;

    public ITCMSQLHelper(Context context, ArrayList<? extends ITCMObject> classes) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.classes = classes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            for (ITCMObject classObject : classes)
                db.execSQL(classObject.getCreateTableSQL());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.ntucap.itcm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ntucap.itcm.classes.ITCMObject;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.utils.DBUtility;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class ITCMDB {

    private static ITCMSQLHelper mDBHelper;
    private static ArrayList<Class<? extends ITCMObject>> classes;
    public static SQLiteDatabase db;

    public ITCMDB() {}

    public static void init(Context context) {
        classes = new ArrayList<>();
        classes.add(ITCMUser.class);
        mDBHelper = new ITCMSQLHelper(context, classes);
        db = mDBHelper.getWritableDatabase();
    }

    private SQLiteDatabase getDB() {
        if(db == null) return mDBHelper.getWritableDatabase();
        return db;
    }

    public static long saveUser(ITCMUser user) {
        if(db != null)
            return db.insert(ITCMUser.TABLE_NAME, null, user.getUpdateContentValue());
        return -1L;
    }

    public static ITCMUser getSingleUser(long id) {
        ITCMUser user = null;
        Cursor cursor = getSingleUserCursor(id);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new ITCMUser(cursor);
            cursor.close();
        }
        return user;
    }

    public static Cursor getSingleUserCursor(long id) {
        if(db != null) {
            String sql = "select * from " + DBUtility.stringToSQLWrapper(ITCMUser.TABLE_NAME) +
                    " where id = " + id + ";";
            return db.rawQuery(sql, null);
        }
        return null;
    }

}

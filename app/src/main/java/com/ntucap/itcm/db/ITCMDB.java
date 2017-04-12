package com.ntucap.itcm.db;

import android.content.Context;

import com.ntucap.itcm.classes.ITCMObject;
import com.ntucap.itcm.classes.ITCMUser;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 11/04/17.
 */

public class ITCMDB {

    private ITCMSQLHelper mDBHelper;
    private Context context;
    private static ArrayList<Class<? extends ITCMObject>> classes;

    public ITCMDB(Context context) {
        this.context = context;
        classes = new ArrayList<>();
        classes.add(ITCMUser.class);
        mDBHelper = new ITCMSQLHelper(context, classes);
    }

}

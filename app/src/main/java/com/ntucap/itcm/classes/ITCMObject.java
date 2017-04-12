package com.ntucap.itcm.classes;

import android.content.ContentValues;

/**
 * Created by ProgrammerYuan on 10/04/17.
 */

public abstract class ITCMObject {

    public abstract String getCreateTableSQL();

    public abstract ContentValues getUpdateContentValue();

    public abstract String getDeleteTableSQL();
}

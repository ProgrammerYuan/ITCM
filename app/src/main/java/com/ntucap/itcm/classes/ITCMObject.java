package com.ntucap.itcm.classes;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by ProgrammerYuan on 10/04/17.
 */

public abstract class ITCMObject implements Serializable{

    public abstract String getCreateTableSQL();

    public abstract ContentValues getUpdateContentValue();

    public abstract String getDeleteTableSQL();
}

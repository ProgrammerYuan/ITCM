package com.ntucap.itcm.classes;

/**
 * Created by ProgrammerYuan on 10/04/17.
 */

public abstract class ITCMObject {

    public abstract String getCreateTableSQL();

    public abstract String getUpdateTableSQL();

    public abstract String getDeleteTableSQL();
}

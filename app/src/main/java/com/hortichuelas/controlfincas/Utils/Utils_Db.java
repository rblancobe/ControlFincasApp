package com.hortichuelas.controlfincas.Utils;

import android.provider.BaseColumns;

public final class Utils_Db {
    public static  class Tables implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME = "title";
    }
    private static final String CREATE_TABLE = "CREATE TABLE "+Tables.TABLE_NAME +" (" +
            Tables._ID + "INTEGER PRIMARY KEY ,"+
            Tables.COLUMN_NAME + " TEXT )";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+Tables.TABLE_NAME;

}

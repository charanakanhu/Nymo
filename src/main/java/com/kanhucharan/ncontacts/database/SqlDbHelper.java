package com.kanhucharan.ncontacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDbHelper extends SQLiteOpenHelper {
        public static final String TABLE_NAME = "SAVE_CONTACTS";

        public static final String SL_NO = "slno";
        public static final String NAME = "name";
        public static final String PHONE_NUMBER = "phone";
        public static final String IMAGE = "image";
        private static final String CREATE_DATABASE = "create table "
                + TABLE_NAME + " (" + SL_NO
                + " integer primary key autoincrement, " + NAME
                + " text not null, " + PHONE_NUMBER + " text not null, " + IMAGE + " BLOB);";

        public SqlDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_DATABASE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }


}

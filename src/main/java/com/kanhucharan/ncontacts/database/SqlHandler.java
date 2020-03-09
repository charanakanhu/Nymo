package com.kanhucharan.ncontacts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlHandler {

    public static final String DATABASE_NAME = "CONTACTS_DATABASE";
    public static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase sqlDatabase;
    SqlDbHelper dbHelper;

    public SqlHandler(Context context) {

        dbHelper = new SqlDbHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
        sqlDatabase = dbHelper.getWritableDatabase();
    }

    public void executeQuery(String query) {
        try {

            if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }

            sqlDatabase = dbHelper.getWritableDatabase();
            sqlDatabase.execSQL(query);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);
        }

    }

    public Cursor selectQuery(String query) {
        Cursor c1 = null;
        try {

            if (sqlDatabase.isOpen()) {
                sqlDatabase.close();

            }
            sqlDatabase = dbHelper.getWritableDatabase();
            c1 = sqlDatabase.rawQuery(query, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR " + e);

        }
        return c1;

    }
    public int update(String id, String name, String phone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlDbHelper.NAME, name);
        contentValues.put(SqlDbHelper.PHONE_NUMBER, phone);
        int i = sqlDatabase.update(SqlDbHelper.TABLE_NAME, contentValues, SqlDbHelper.SL_NO + " = " + id, null);
        return i;
    }

    public void delete(String id) {
        sqlDatabase.delete(SqlDbHelper.TABLE_NAME, SqlDbHelper.SL_NO + "=" + id, null);
    }

}


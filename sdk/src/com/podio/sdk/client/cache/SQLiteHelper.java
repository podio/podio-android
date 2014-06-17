/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.client.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE = "content";
    public static final String KEY_COLUMN = "key";
    public static final String DATA_COLUMN = "data";

    public SQLiteHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        clearDatabase(database);
        setupDatabase(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        clearDatabase(database);
        setupDatabase(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        clearDatabase(database);
        setupDatabase(database);
    }

    private void clearDatabase(SQLiteDatabase database) {
        if (database == null) {
            throw new NullPointerException("database cannot be null");
        }

        Cursor cursor = null;

        database.beginTransaction();
        try {
            // Drop all tables, views and triggers in the database.
            String query = "SELECT type, name FROM sqlite_master WHERE type IN (?, ?, ?)";
            String[] arguments = { "table", "view", "trigger" };
            cursor = database.rawQuery(query, arguments);

            if (cursor != null && cursor.moveToFirst()) {
                int typeColumn = cursor.getColumnIndex("type");
                int nameColumn = cursor.getColumnIndex("name");

                do {
                    String type = cursor.getString(typeColumn);
                    String name = cursor.getString(nameColumn);
                    database.execSQL("DROP " + type + " IF EXISTS " + name);
                } while (cursor.moveToNext());
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            database.endTransaction();
        }
    }

    private void setupDatabase(SQLiteDatabase database) {
        if (database == null) {
            throw new NullPointerException("database cannot be null");
        }

        String sql = "CREATE TABLE %s (" + //
        " %s TEXT PRIMARY KEY," + //
        " %s BLOB NOT NULL DEFAULT (''))";

        database.beginTransaction();
        try {
            database.execSQL(String.format(sql, TABLE, KEY_COLUMN, DATA_COLUMN));
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

}

package com.podio.sdk.client.delegate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

final class SQLiteHelper extends SQLiteOpenHelper {

    SQLiteHelper(Context context, String name, int version) {
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
        if (database != null) {
            Cursor cursor = null;

            try {
                // Drop all tables, views and triggers in the database.
                database.beginTransaction();
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
    }

    private void setupDatabase(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.beginTransaction();
                database.execSQL("CREATE TABLE content (" + //
                        " uri TEXT PRIMARY KEY," + //
                        " json TEXT NOT NULL DEFAULT (''))");
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    }

}

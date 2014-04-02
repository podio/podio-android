package com.podio.sdk.client.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public interface DatabaseClientDelegate {

    public Cursor delete(Uri uri);

    public Cursor insert(Uri uri, ContentValues values);

    public Cursor query(Uri uri);

    public Cursor update(Uri uri, ContentValues values);

    public void initialize(SQLiteDatabase database);
}

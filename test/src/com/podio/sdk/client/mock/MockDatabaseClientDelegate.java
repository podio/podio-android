package com.podio.sdk.client.mock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.podio.sdk.client.database.DatabaseClientDelegate;

public class MockDatabaseClientDelegate implements DatabaseClientDelegate {

    private Uri deleteUri = null;
    private Uri insertUri = null;
    private Uri queryUri = null;
    private Uri updateUri = null;

    private boolean isDeleteCalled = false;
    private boolean isInsertCalled = false;
    private boolean isQueryCalled = false;
    private boolean isUpdateCalled = false;

    @Override
    public Cursor delete(Uri uri) {
        isDeleteCalled = true;
        deleteUri = uri;
        return null;
    }

    @Override
    public Cursor insert(Uri uri, ContentValues values) {
        isInsertCalled = true;
        insertUri = uri;
        return null;
    }

    @Override
    public Cursor query(Uri uri) {
        isQueryCalled = true;
        queryUri = uri;
        return null;
    }

    @Override
    public Cursor update(Uri uri, ContentValues values) {
        isUpdateCalled = true;
        updateUri = uri;
        return null;
    }

    @Override
    public void initialize(SQLiteDatabase database) {
    }

    public Uri mock_getDeleteUri() {
        return deleteUri;
    }

    public Uri mock_getInsertUri() {
        return insertUri;
    }

    public Uri mock_getQueryUri() {
        return queryUri;
    }

    public Uri mock_getUpdateUri() {
        return updateUri;
    }

    public boolean mock_isDeleteCalled() {
        return isDeleteCalled;
    }

    public boolean mock_isInsertCalled() {
        return isInsertCalled;
    }

    public boolean mock_isQueryCalled() {
        return isQueryCalled;
    }

    public boolean mock_isUpdateCalled() {
        return isUpdateCalled;
    }
}

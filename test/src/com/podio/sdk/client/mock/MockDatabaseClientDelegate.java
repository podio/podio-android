package com.podio.sdk.client.mock;

import android.net.Uri;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;

public class MockDatabaseClientDelegate implements RestClientDelegate {

    private Uri deleteUri = null;
    private Uri insertUri = null;
    private Uri queryUri = null;
    private Uri updateUri = null;

    private boolean isDeleteCalled = false;
    private boolean isInsertCalled = false;
    private boolean isQueryCalled = false;
    private boolean isUpdateCalled = false;

    @Override
    public RestResult delete(Uri uri) {
        isDeleteCalled = true;
        deleteUri = uri;
        return null;
    }

    @Override
    public RestResult get(Uri uri, Class<?> classOfResult) {
        isQueryCalled = true;
        queryUri = uri;
        return null;
    }

    @Override
    public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
        isInsertCalled = true;
        insertUri = uri;
        return null;
    }

    @Override
    public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
        isUpdateCalled = true;
        updateUri = uri;
        return null;
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

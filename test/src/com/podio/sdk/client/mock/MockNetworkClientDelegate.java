package com.podio.sdk.client.mock;

import android.net.Uri;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;

public class MockNetworkClientDelegate implements RestClientDelegate {

    private Uri deleteUri = null;
    private Uri getUri = null;
    private Uri postUri = null;
    private Uri putUri = null;

    private boolean isDeleteCalled = false;
    private boolean isGetCalled = false;
    private boolean isPostCalled = false;
    private boolean isPutCalled = false;

    @Override
    public RestResult delete(Uri uri) {
        isDeleteCalled = true;
        deleteUri = uri;
        return null;
    }

    @Override
    public RestResult get(Uri uri, Class<?> classOfResult) {
        isGetCalled = true;
        getUri = uri;
        return null;
    }

    @Override
    public RestResult post(Uri uri, Object item, Class<?> classOfItem) {
        isPostCalled = true;
        postUri = uri;
        return null;
    }

    @Override
    public RestResult put(Uri uri, Object item, Class<?> classOfItem) {
        isPutCalled = true;
        putUri = uri;
        return null;
    }

    public Uri mock_getDeleteUri() {
        return deleteUri;
    }

    public Uri mock_getGetUri() {
        return getUri;
    }

    public Uri mock_getPostUri() {
        return postUri;
    }

    public Uri mock_getPutUri() {
        return putUri;
    }

    public boolean mock_isDeleteCalled() {
        return isDeleteCalled;
    }

    public boolean mock_isGetCalled() {
        return isGetCalled;
    }

    public boolean mock_isPostCalled() {
        return isPostCalled;
    }

    public boolean mock_isPutCalled() {
        return isPutCalled;
    }
}

package com.podio.sdk.client.mock;

import android.net.Uri;

import com.podio.sdk.client.network.NetworkClientDelegate;

public class MockNetworkClientDelegate implements NetworkClientDelegate {

    private Uri deleteUri = null;
    private Uri getUri = null;
    private Uri postUri = null;
    private Uri putUri = null;

    private boolean isDeleteCalled = false;
    private boolean isGetCalled = false;
    private boolean isPostCalled = false;
    private boolean isPutCalled = false;

    @Override
    public String delete(Uri uri) {
        isDeleteCalled = true;
        deleteUri = uri;
        return null;
    }

    @Override
    public String get(Uri uri) {
        isGetCalled = true;
        getUri = uri;
        return null;
    }

    @Override
    public String post(Uri uri, String json) {
        isPostCalled = true;
        postUri = uri;
        return null;
    }

    @Override
    public String put(Uri uri, String json) {
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

package com.podio.sdk.client.delegate.mock;

import android.net.Uri;

import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;

public class MockRestClientDelegate implements RestClientDelegate {

    private RestResult authorizeResult = null;
    private RestResult deleteResult = null;
    private RestResult getResult = null;
    private RestResult postResult = null;
    private RestResult putResult = null;

    private Uri authorizeUri = null;
    private Uri deleteUri = null;
    private Uri getUri = null;
    private Uri postUri = null;
    private Uri putUri = null;

    private int authorizeCount = 0;
    private int deleteCount = 0;
    private int getCount = 0;
    private int postCount = 0;
    private int putCount = 0;

    @Override
    public RestResult authorize(Uri uri) {
        authorizeCount++;
        authorizeUri = uri;
        return authorizeResult;
    }

    @Override
    public RestResult delete(Uri uri) {
        deleteCount++;
        deleteUri = uri;
        return deleteResult;
    }

    @Override
    public RestResult get(Uri uri) {
        getCount++;
        getUri = uri;
        return getResult;
    }

    @Override
    public RestResult post(Uri uri, Object item) {
        postCount++;
        postUri = uri;
        return postResult;
    }

    @Override
    public RestResult put(Uri uri, Object item) {
        putCount++;
        putUri = uri;
        return putResult;
    }

    public Uri mock_getAuthorizeUri() {
        return authorizeUri;
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

    public int mock_getAuthorizeCallCount() {
        return authorizeCount;
    }

    public int mock_getDeleteCallCount() {
        return deleteCount;
    }

    public int mock_getGetCallCount() {
        return getCount;
    }

    public int mock_getPostCallCount() {
        return postCount;
    }

    public int mock_getPutCallCount() {
        return putCount;
    }

    public void mock_setMockAuthorizeResult(RestResult authorizeResult) {
        this.authorizeResult = authorizeResult;
    }

    public void mock_setMockDeleteResult(RestResult deleteResult) {
        this.deleteResult = deleteResult;
    }

    public void mock_setMockGetResult(RestResult getResult) {
        this.getResult = getResult;
    }

    public void mock_setMockPostResult(RestResult postResult) {
        this.postResult = postResult;
    }

    public void mock_setMockPutResult(RestResult putResult) {
        this.putResult = putResult;
    }
}

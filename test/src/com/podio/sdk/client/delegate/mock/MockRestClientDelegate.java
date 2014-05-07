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

package com.podio.sdk.client.delegate.mock;

import android.net.Uri;

import com.podio.sdk.PodioParser;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.client.delegate.JsonClientDelegate;

public class MockRestClientDelegate extends JsonClientDelegate {

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
    public RestResult authorize(Uri uri, PodioParser<?> itemParser) {
        authorizeCount++;
        authorizeUri = uri;
        return authorizeResult;
    }

    @Override
    public RestResult delete(Uri uri, PodioParser<?> itemParser) {
        deleteCount++;
        deleteUri = uri;
        return deleteResult;
    }

    @Override
    public RestResult get(Uri uri, PodioParser<?> itemParser) {
        getCount++;
        getUri = uri;
        return getResult;
    }

    @Override
    public RestResult post(Uri uri, Object item, PodioParser<?> itemParser) {
        postCount++;
        postUri = uri;
        return postResult;
    }

    @Override
    public RestResult put(Uri uri, Object item, PodioParser<?> itemParser) {
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

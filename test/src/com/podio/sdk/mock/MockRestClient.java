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

package com.podio.sdk.mock;

import android.net.Uri;

import com.podio.sdk.client.QueuedRestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;

/**
 * @author Christian Holm
 */
public class MockRestClient extends QueuedRestClient {
    public RuntimeException causeOfFailure;
    public RestResult<?> result;
    public Object data;
    public Uri uri;

    public MockRestClient() {
        this(-1);
    }

    public MockRestClient(int capacity) {
        this(capacity, RestResult.success());
    }

    public MockRestClient(RestResult<?> result) {
        this(-1, result);
    }

    public MockRestClient(RuntimeException exception) {
        this(-1, RestResult.failure(exception));
        this.causeOfFailure = exception;
    }

    public MockRestClient(int capacity, RestResult<?> result) {
        super("test", "podio.test", capacity);
        this.result = result;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
        if (causeOfFailure != null) {
            throw causeOfFailure;
        }

        this.uri = restRequest.getFilter().buildUri(getScheme(), getAuthority());
        this.data = restRequest.getContent();
        return (RestResult<T>) result;
    }

}

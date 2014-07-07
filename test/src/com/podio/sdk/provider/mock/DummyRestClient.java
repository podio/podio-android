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

package com.podio.sdk.provider.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.net.Uri;

import com.podio.sdk.client.QueuedRestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;

public final class DummyRestClient extends QueuedRestClient {

    private final RestResult<?> result;
    private Object data;
    private Uri uri;

    public DummyRestClient(RestResult<?> result) {
        super("content", "test.uri", Integer.MAX_VALUE);
        this.result = result;
    }

    public Object mock_getRequestData() {
        return data;
    }

    public Uri mock_getUri() {
        return uri;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
        return (RestResult<T>) result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<RestResult<T>> enqueue(RestRequest<T> request) {
        uri = request.getFilter().buildUri(getScheme(), getAuthority());
        data = request.getContent();
        callListener(request, (RestResult<T>) result);

        return new Future<RestResult<T>>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public RestResult<T> get() throws InterruptedException, ExecutionException {
                return (RestResult<T>) result;
            }

            @Override
            public RestResult<T> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return (RestResult<T>) result;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

        };
    }

}

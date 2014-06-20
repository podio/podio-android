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

package com.podio.sdk.client;

public class MockRestClient extends QueuedRestClient {
    private RestResult<?> result = RestResult.success();
    private boolean async = false;

    public MockRestClient() {
        this(-1);
    }

    public MockRestClient(int capacity) {
        this("test://", "podio.test", capacity);
    }

    public MockRestClient(String scheme, String authority) {
        this(scheme, authority, -1);
    }

    public MockRestClient(String scheme, String authority, int capacity) {
        super(scheme, authority, capacity);
    }

    public void setResult(RestResult<?> result) {
        this.result = result;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
        return (RestResult<T>) result;
    }

    @Override
    protected <T> void reportResult(RestRequest<T> request, RestResult<T> result) {
        if (async) {
            super.reportResult(request, result);
        } else {
            // This makes sure the listeners are called on the worker thread
            super.callListener(request, result);
        }
    }

}

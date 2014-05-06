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

package com.podio.sdk.domain.mock;

import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.ResultListener;

public final class MockRestClient implements RestClient {

    private RestRequest request;

    @Override
    public String getAuthority() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public boolean enqueue(RestRequest request) {
        this.request = request;
        return true;
    }

    public RestRequest mock_getLastPushedRestRequest() {
        return request;
    }

    public void mock_processLastPushedRestRequest(boolean shouldBeSuccess, String withMockMessage,
            Object withMockItem) {

        if (request != null) {
            ResultListener listener = request.getResultListener();

            if (listener != null) {
                Object ticket = request.getTicket();

                if (shouldBeSuccess) {
                    listener.onSuccess(ticket, withMockItem);
                } else {
                    listener.onFailure(ticket, withMockMessage);
                }
            }
        }
    }

}

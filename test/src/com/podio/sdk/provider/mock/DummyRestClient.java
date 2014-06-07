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

import com.podio.sdk.client.QueuedRestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;

public final class DummyRestClient extends QueuedRestClient {

    private RestResult<?> result;

	public DummyRestClient(RestResult<?> result) {
		super(null, null);
		
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> RestResult<T> handleRequest(RestRequest<T> restRequest) {
		return (RestResult<T>) result;
	}

	@SuppressWarnings("unchecked")
	@Override
    public <T> boolean enqueue(RestRequest<T> request) {
        this.callListener(request, (RestResult<T>) result);
        return true;
    }

}

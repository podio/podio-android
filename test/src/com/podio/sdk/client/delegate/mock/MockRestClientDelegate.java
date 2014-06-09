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

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.RestOperation;

public class MockRestClientDelegate implements RestClientDelegate {
	
	private Map<RestOperation, Integer> calls = new HashMap<RestOperation, Integer>();
	private Map<RestOperation, Uri> uris = new HashMap<RestOperation, Uri>();

	
	private <T> RestResult<T> process(RestOperation operation, Uri uri) {
		Integer callCount = calls.get(operation);
		if (callCount == null) {
			callCount = 0;
		}
		callCount++;
		calls.put(operation, callCount);
		
		uris.put(operation, uri);
		
		return RestResult.success();
	}
	
	public int getCalls(RestOperation operation) {
		Integer callCount = calls.get(operation);
		if (callCount != null) {
			return callCount;
		} else {
			return 0;
		}
	}
	
	public Uri getUri(RestOperation operation) {
		return uris.get(operation);
	}

    @Override
    public RestResult<Session> authorize(Uri uri) {
    	return process(RestOperation.AUTHORIZE, uri);
    }

    @Override
    public <T> RestResult<T> delete(Uri uri, PodioParser<? extends T> itemParser) {
    	return process(RestOperation.DELETE, uri);
    }

    @Override
    public <T> RestResult<T> get(Uri uri, PodioParser<? extends T> itemParser) {
    	return process(RestOperation.GET, uri);
    }

    @Override
    public <T> RestResult<T> post(Uri uri, Object item, PodioParser<? extends T> itemParser) {
    	return process(RestOperation.POST, uri);
    }

    @Override
    public <T> RestResult<T> put(Uri uri, Object item, PodioParser<? extends T> itemParser) {
    	return process(RestOperation.PUT, uri);
    }
}

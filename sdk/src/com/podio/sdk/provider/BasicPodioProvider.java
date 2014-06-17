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

package com.podio.sdk.provider;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class BasicPodioProvider {

    private final RestClient client;

    public BasicPodioProvider(RestClient client) {
        if (client == null) {
            throw new NullPointerException("client cannot be null");
        }
        this.client = client;
    }

    protected <T> Object get(PodioFilter filter, Class<T> classOfItem, ResultListener<? super T> resultListener) {
        return request(RestOperation.GET, filter, null, PodioParser.fromClass(classOfItem), resultListener);
    }

    protected <T> Object post(PodioFilter filter, Object content, Class<T> classOfItem, ResultListener<? super T> resultListener) {
        return request(RestOperation.POST, filter, content, PodioParser.fromClass(classOfItem), resultListener);
    }

    protected <T> Object put(PodioFilter filter, Object content, Class<T> classOfItem, ResultListener<? super T> resultListener) {
        return request(RestOperation.PUT, filter, content, PodioParser.fromClass(classOfItem), resultListener);
    }

    protected <T> Object request(RestOperation operation, PodioFilter filter,
            Object content, PodioParser<? extends T> parser, ResultListener<? super T> resultListener) {
        Object ticket = filter;

        RestRequest<T> restRequest = new RestRequest<T>() //
        .setContent(content) //
        .setFilter(filter) //
        .setParser(parser) //
        .setOperation(operation) //
        .setResultListener(resultListener) //
        .setTicket(ticket);

        if (client.enqueue(restRequest)) {
            return ticket;
        } else {
            return null;
        }
    }
}

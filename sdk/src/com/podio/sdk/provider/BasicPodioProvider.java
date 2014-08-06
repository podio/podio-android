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
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RequestFuture;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.parser.JsonParser;

public class BasicPodioProvider {

    private RestClient client;

    public void setRestClient(RestClient client) {
        this.client = client;
    }

    protected <T> RequestFuture<T> get(PodioFilter filter, Class<T> classOfItem) {
        return request(RestClient.Operation.GET, filter, null, JsonParser.fromClass(classOfItem));
    }

    protected <T> RequestFuture<T> post(PodioFilter filter, Object content, Class<T> classOfItem) {
        return request(RestClient.Operation.POST, filter, content, JsonParser.fromClass(classOfItem));
    }

    protected <T> RequestFuture<T> put(PodioFilter filter, Object content, Class<T> classOfItem) {
        return request(RestClient.Operation.PUT, filter, content, JsonParser.fromClass(classOfItem));
    }

    protected <T> RequestFuture<T> request(RestClient.Operation operation, PodioFilter filter, Object content, JsonParser<? extends T> parser) {

        RestRequest<T> restRequest = new RestRequest<T>()
                .setContent(content)
                .setFilter(filter)
                .setParser(parser)
                .setOperation(operation);

        return client.enqueue(restRequest);
    }
}

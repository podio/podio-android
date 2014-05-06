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

import com.podio.sdk.PodioFilter;
import com.podio.sdk.client.delegate.ItemParser;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public final class RestRequest {
    private Object content;
    private PodioFilter filter;
    private ItemParser<?> itemParser;
    private RestOperation operation;
    private ResultListener resultListener;
    private Object ticket;

    public Object getContent() {
        return content;
    }

    public PodioFilter getFilter() {
        return filter;
    }

    public ItemParser<?> getItemParser() {
        return itemParser;
    }

    public RestOperation getOperation() {
        return operation;
    }

    public ResultListener getResultListener() {
        return resultListener;
    }

    public Object getTicket() {
        return ticket;
    }

    public RestRequest setContent(Object item) {
        this.content = item;
        return this;
    }

    public RestRequest setFilter(PodioFilter filter) {
        this.filter = filter;
        return this;
    }

    public RestRequest setItemParser(ItemParser<?> itemParser) {
        this.itemParser = itemParser;
        return this;
    }

    public RestRequest setOperation(RestOperation operation) {
        this.operation = operation;
        return this;
    }

    public RestRequest setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public RestRequest setTicket(Object ticket) {
        this.ticket = ticket;
        return this;
    }
}

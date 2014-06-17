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
import com.podio.sdk.PodioParser;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public final class RestRequest<T> {

    private Object content;
    private PodioFilter filter;
    private PodioParser<? extends T> parser;
    private RestOperation operation;
    private ResultListener<? super T> resultListener;
    private Object ticket;

    @Override
    public String toString() {
        return "RestRequest [content=" + content + ", filter=" + filter
                + ", parser=" + parser + ", operation=" + operation
                + ", resultListener=" + resultListener + ", ticket=" + ticket
                + "]";
    }

    /**
     * Equals and hashCode is needed for test comparisons
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (content == null ? 0 : content.hashCode());
        result = prime * result + (filter == null ? 0 : filter.hashCode());
        result = prime * result
                + (operation == null ? 0 : operation.hashCode());
        result = prime * result + (parser == null ? 0 : parser.hashCode());
        result = prime * result
                + (resultListener == null ? 0 : resultListener.hashCode());
        result = prime * result + (ticket == null ? 0 : ticket.hashCode());
        return result;
    }

    /**
     * Equals and hashCode is needed for test comparisons
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        RestRequest other = (RestRequest) obj;
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (filter == null) {
            if (other.filter != null) {
                return false;
            }
        } else if (!filter.equals(other.filter)) {
            return false;
        }
        if (operation != other.operation) {
            return false;
        }
        if (parser == null) {
            if (other.parser != null) {
                return false;
            }
        } else if (!parser.equals(other.parser)) {
            return false;
        }
        if (resultListener == null) {
            if (other.resultListener != null) {
                return false;
            }
        } else if (!resultListener.equals(other.resultListener)) {
            return false;
        }
        if (ticket == null) {
            if (other.ticket != null) {
                return false;
            }
        } else if (!ticket.equals(other.ticket)) {
            return false;
        }
        return true;
    }

    public void validate() {
        if (filter == null) {
            throw new NullPointerException("filter cannot be null");
        }
        if (operation == null) {
            throw new NullPointerException("operation cannot be null");
        }
    }

    public Object getContent() {
        return content;
    }

    public PodioFilter getFilter() {
        return filter;
    }

    public PodioParser<? extends T> getParser() {
        return parser;
    }

    public RestOperation getOperation() {
        return operation;
    }

    public ResultListener<? super T> getResultListener() {
        return resultListener;
    }

    public Object getTicket() {
        return ticket;
    }

    public RestRequest<T> setContent(Object item) {
        this.content = item;
        return this;
    }

    public RestRequest<T> setFilter(PodioFilter filter) {
        this.filter = filter;
        return this;
    }

    public RestRequest<T> setParser(PodioParser<? extends T> parser) {
        this.parser = parser;
        return this;
    }

    public RestRequest<T> setOperation(RestOperation operation) {
        this.operation = operation;
        return this;
    }

    public RestRequest<T> setResultListener(ResultListener<? super T> resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public RestRequest<T> setTicket(Object ticket) {
        this.ticket = ticket;
        return this;
    }
}

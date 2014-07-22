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

import com.podio.sdk.ErrorListener;
import com.podio.sdk.PodioException;
import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.parser.JsonParser;

public final class RestRequest<T> {

    private Object content;
    private PodioFilter filter;
    private RestClient.Operation operation;
    private JsonParser<? extends T> parser;

    private ErrorListener errorListener;
    private ResultListener<? super T> resultListener;
    private SessionListener sessionListener;

    @Override
    public String toString() {
        return "RestRequest [content=" + content + ", filter=" + filter
                + ", parser=" + parser + ", operation=" + operation
                + ", resultListener=" + resultListener
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
        result = prime * result + (operation == null ? 0 : operation.hashCode());
        result = prime * result + (parser == null ? 0 : parser.hashCode());
        result = prime * result + (errorListener == null ? 0 : errorListener.hashCode());
        result = prime * result + (resultListener == null ? 0 : resultListener.hashCode());
        result = prime * result + (sessionListener == null ? 0 : sessionListener.hashCode());

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

        if (errorListener == null) {
            if (other.errorListener != null) {
                return false;
            }
        } else if (!errorListener.equals(other.errorListener)) {
            return false;
        }

        if (resultListener == null) {
            if (other.resultListener != null) {
                return false;
            }
        } else if (!resultListener.equals(other.resultListener)) {
            return false;
        }

        if (sessionListener == null) {
            if (other.sessionListener != null) {
                return false;
            }
        } else if (!sessionListener.equals(other.sessionListener)) {
            return false;
        }

        return true;
    }

    public void validate() throws PodioException {
        if (filter == null) {
            Throwable cause = new NullPointerException("filter cannot be null");
            throw new PodioException("RestRequest invalid", cause);
        }

        if (operation == null) {
            Throwable cause = new NullPointerException("operation cannot be null");
            throw new PodioException("RestRequest invalid", cause);
        }
    }

    public Object getContent() {
        return content;
    }

    public PodioFilter getFilter() {
        return filter;
    }

    public JsonParser<? extends T> getParser() {
        return parser;
    }

    public RestClient.Operation getOperation() {
        return operation;
    }

    public ErrorListener getErrorListener() {
        return errorListener;
    }

    public ResultListener<? super T> getResultListener() {
        return resultListener;
    }

    public SessionListener getSessionListener() {
        return sessionListener;
    }

    public RestRequest<T> setContent(Object item) {
        this.content = item;
        return this;
    }

    public RestRequest<T> setFilter(PodioFilter filter) {
        this.filter = filter;
        return this;
    }

    public RestRequest<T> setOperation(RestClient.Operation operation) {
        this.operation = operation;
        return this;
    }

    public RestRequest<T> setParser(JsonParser<? extends T> parser) {
        this.parser = parser;
        return this;
    }

    public RestRequest<T> setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public RestRequest<T> setResultListener(ResultListener<? super T> resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public RestRequest<T> setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
        return this;
    }
}

package com.podio.sdk.client;

import com.podio.sdk.Filter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public final class RestRequest {
    private Object content;
    private Class<?> itemType;
    private RestOperation operation;
    private ResultListener resultListener;
    private Filter filter;

    public Object getContent() {
        return content;
    }

    public Filter getFilter() {
        return filter;
    }

    public Class<?> getItemType() {
        return itemType;
    }

    public RestOperation getOperation() {
        return operation;
    }

    public ResultListener getResultListener() {
        return resultListener;
    }

    public RestRequest setContent(Object item) {
        this.content = item;
        return this;
    }

    public RestRequest setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public RestRequest setOperation(RestOperation operation) {
        this.operation = operation;
        return this;
    }

    public RestRequest setItemType(Class<?> itemType) {
        this.itemType = itemType;
        return this;
    }

    public RestRequest setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

}

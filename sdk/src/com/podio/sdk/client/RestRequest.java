package com.podio.sdk.client;

import java.util.List;
import java.util.Map;

import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public final class RestRequest {
    private Object content;
    private Class<?> itemType;
    private RestOperation operation;
    private String path;
    private Map<String, List<String>> query;
    private ResultListener resultListener;
    private Object ticket;

    public RestRequest setContent(Object item) {
        this.content = item;
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

    public RestRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public RestRequest setQueryParameters(Map<String, List<String>> parameters) {
        this.query = parameters;
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

    public Object getContent() {
        return content;
    }

    public Class<?> getItemType() {
        return itemType;
    }

    public RestOperation getOperation() {
        return operation;
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getQueryParameters() {
        return query;
    }

    public ResultListener getResultListener() {
        return resultListener;
    }

    public Object getTicket() {
        return ticket;
    }

}

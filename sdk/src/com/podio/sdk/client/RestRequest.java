package com.podio.sdk.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.net.Uri;

import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;
import com.podio.sdk.internal.utils.Utils;

public final class RestRequest {
    private Object content;
    private Class<?> itemType;
    private RestOperation operation;
    private String path;
    private Map<String, List<String>> query;
    private ResultListener resultListener;
    private Object ticket;

    public Uri buildUri(String scheme, String authority) {
        Uri uri = Uri.EMPTY;

        if (Utils.notEmpty(scheme) && Utils.notEmpty(authority)) {

            Uri.Builder uriBuilder = new Uri.Builder() //
                    .scheme(scheme) //
                    .authority(authority) //
                    .path(path);

            if (Utils.notEmpty(query)) {
                Set<String> keys = query.keySet();

                for (String key : keys) {
                    List<String> values = query.get(key);

                    for (String value : values) {
                        uriBuilder.appendQueryParameter(key, value);
                    }
                }
            }

            uri = uriBuilder.build();
        }

        return uri;
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

}

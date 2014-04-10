package com.podio.sdk;

import android.net.Uri;

import com.podio.sdk.client.RestResult;

public interface RestClientDelegate {

    public RestResult delete(Uri uri);

    public RestResult get(Uri uri, Class<?> classOfResult);

    public RestResult post(Uri uri, Object item, Class<?> classOfItem);

    public RestResult put(Uri uri, Object item, Class<?> classOfItem);

}

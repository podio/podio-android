package com.podio.sdk;

import android.net.Uri;

import com.podio.sdk.client.RestResult;

public interface RestClientDelegate {

    public RestResult authorize(Uri uri);

    public RestResult delete(Uri uri);

    public RestResult get(Uri uri);

    public RestResult post(Uri uri, Object item);

    public RestResult put(Uri uri, Object item);

}

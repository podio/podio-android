package com.podio.sdk.client.network;

import android.net.Uri;

public interface NetworkClientDelegate {

    public String delete(Uri uri);

    public String get(Uri uri);

    public String post(Uri uri, String json);

    public String put(Uri uri, String json);

}

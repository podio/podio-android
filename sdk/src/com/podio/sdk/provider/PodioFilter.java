package com.podio.sdk.provider;

import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.internal.utils.Utils;

public class PodioFilter implements Filter {
    private final Uri.Builder uriBuilder;

    public PodioFilter() {
        this(null);
    }

    public PodioFilter(String path) {
        uriBuilder = new Uri.Builder();

        if (Utils.notEmpty(path)) {
            uriBuilder.appendEncodedPath(path);
        }
    }

    @Override
    public Filter addQueryParameter(String key, String value) {
        if (Utils.notEmpty(key) && value != null) {
            uriBuilder.appendQueryParameter(key, value);
        }

        return this;
    }

    @Override
    public Filter addPathSegment(String segment) {
        if (Utils.notEmpty(segment)) {
            uriBuilder.appendPath(segment);
        }

        return this;
    }

    @Override
    public Uri buildUri(String scheme, String authority) {
        Uri uri = null;

        if (Utils.notEmpty(scheme) && Utils.notEmpty(authority)) {
            uri = uriBuilder.scheme(scheme).authority(authority).build();
        }

        return uri;
    }
}

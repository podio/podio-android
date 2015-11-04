
package com.podio.sdk;

import android.net.Uri;

import com.podio.sdk.internal.Utils;

public class Filter {
    private final Uri.Builder uriBuilder;

    protected Filter() {
        this(null);
    }

    protected Filter(String path) {
        uriBuilder = new Uri.Builder();

        if (Utils.notEmpty(path)) {
            uriBuilder.appendEncodedPath(path);
        }
    }

    protected Filter addQueryParameter(String key, String value) {
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    protected Filter addPathSegment(String segment) {
        uriBuilder.appendPath(segment);
        return this;
    }

    public Uri buildUri(String scheme, String authority) {
        return uriBuilder
                .scheme(scheme)
                .authority(authority)
                .build();
    }
}

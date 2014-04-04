package com.podio.sdk;

import android.net.Uri;

/**
 * Defines the filter interface that describes which data set any given rest
 * operation is to address. This interface is primarily used with the
 * {@link Provider}.
 * 
 * @author László Urszuly
 */
public interface Filter {

    /**
     * Adds a new query parameter with the given name and value.
     * 
     * @param key
     *            The name of the parameter.
     * @param value
     *            The corresponding parameter value.
     * @return This filter object for further chaining.
     */
    public Filter addQueryParameter(String key, String value);

    /**
     * Adds a new path segment to the filter.
     * 
     * @param segment
     *            The new segment.
     * @return This filter object for further chaining.
     */
    public Filter addPathSegment(String segment);

    /**
     * Builds a Uri based on the given parameters and the information contained
     * in the filter.
     * 
     * @param scheme
     *            The Uri scheme.
     * @param authority
     *            The Uri authority
     * @return The Uri.
     */
    public Uri buildUri(String scheme, String authority);

}

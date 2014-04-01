package com.podio.sdk;

import java.util.List;
import java.util.Map;

/**
 * Defines the filter interface that describes which data set any given rest
 * operation is to address. This interface is primarily used with the
 * {@link Provider}.
 *
 * @author László Urszuly
 */
public interface Filter {

    /**
     * Returns the query parameters for this filter.
     *
     * @return A map of key/value pairs.
     */
    public Map<String, List<String>> getQueryParameters();

    /**
     * Adds a new query parameter with the given name and value.
     *
     * @param key
     *        The name of the parameter.
     * @param value
     *        The corresponding parameter value.
     */
    public void addQueryParameter(String key, String value);

}

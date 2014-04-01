package com.podio.sdk;

import java.util.List;

/**
 * Defines the parser interface to convert data from one data structure to
 * another. The interface doesn't constrain the data, nor the responsibility of
 * keeping data intact after it's been parsed. It's up to the implementation to
 * handle those issues.
 *
 * @param <T>
 *        The data structure to convert.
 * @author László Urszuly
 */
public interface Parser<T> {

    /**
     * Performs the actual parsing of the given source and produces a list of
     * parsed items.
     *
     * @param source
     *        The data source to parse.
     * @param classOfTarget
     *        The class of the produced, parsed items.
     * @return A list of new items that conform to the requested data structure.
     */
    public List<?> parse(T source, Class<?> classOfTarget);

}

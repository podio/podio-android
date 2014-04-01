package com.podio.sdk;

import java.util.List;

/**
 * Defines the {@link Provider} callback interface as seen by the third party
 * client application.
 *
 * @author László Urszuly
 */
public interface ProviderListener {

    /**
     * Notifies the calling implementation that a call to the
     * {@link Provider#fetchItems(Filter, ProviderListener)} method has
     * successfully been performed. The result of the call and the association
     * to which call the result belongs to are delivered through the method
     * arguments.
     *
     * @param filter
     *        The filter defining the items collection. It's the same filter the
     *        caller provided when calling the fetch request.
     * @param items
     *        The result of the previously made fetch request.
     */
    public void onFetchCompleted(Filter filter, List<?> items);

    /**
     * Notifies the calling implementation that a request couldn't be performed.
     *
     * @param filter
     *        The filter provided by the caller when the specific request was
     *        made.
     * @param message
     *        A message describing what went wrong.
     */
    public void onRequestFailed(Filter filter, String message);

}

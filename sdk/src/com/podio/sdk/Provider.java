package com.podio.sdk;

/**
 * Defines the {@link Provider} responsibilities. Each implementing class is
 * responsible for it's corresponding data and must hence be able to perform the
 * typical rest operations GET, POST, PUT and DELETE on the given
 * {@link RestClient}.
 * 
 * @author László Urszuly
 */
public interface Provider<T> {

    /**
     * Performs a PUT rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the data set to change.
     * @param item
     *            Provides the new values to put.
     * @return A ticket identifying this request.
     * 
     */
    public Object changeItem(Filter filter, T item);

    /**
     * Performs a DELETE rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the data set to delete.
     * @return A ticket identifying this request.
     */
    public Object deleteItems(Filter filter);

    /**
     * Performs a GET rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the data set to fetch.
     * @return A ticket identifying this request.
     */
    public Object fetchItems(Filter filter);

    /**
     * Performs a POST rest operation on the given {@link RestClient}.
     * 
     * @param item
     *            Provides the new data.
     * @return A ticket identifying this request.
     */
    public Object pushItem(T item);

    /**
     * Sets the callback interface used to report the result through. If this
     * callback is not given, then the rest operations can still be executed
     * silently. Note, though, that the GET operation, even though technically
     * possible, wouldn't make any sense without this callback.
     * 
     * @param providerListener
     *            The callback implementation. Null is valid.
     */
    public void setProviderListener(ProviderListener providerListener);

    /**
     * Sets the rest client that will perform the rest operation.
     * 
     * @param client
     *            The target {@link RestClient}.
     */
    public void setRestClient(RestClient client);

}

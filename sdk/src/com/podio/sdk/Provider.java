package com.podio.sdk;

/**
 * Defines the {@link Provider} responsibilities. Each implementing class is
 * responsible for it's corresponding data and must hence be able to perform the
 * typical rest operations GET, POST, PUT and DELETE on the given
 * {@link RestClient}.
 * 
 * @author László Urszuly
 */
public interface Provider {

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
    public Object changeRequest(Filter filter, Object item);

    /**
     * Performs a DELETE rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the data set to delete.
     * @return A ticket identifying this request.
     */
    public Object deleteRequest(Filter filter);

    /**
     * Performs a GET rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the data set to fetch.
     * @return A ticket identifying this request.
     */
    public Object fetchRequest(Filter filter);

    /**
     * Performs a POST rest operation on the given {@link RestClient}.
     * 
     * @param filter
     *            Defines the end point where to push.
     * @param item
     *            Provides the new data.
     * @return A ticket identifying this request.
     */
    public Object pushRequest(Filter filter, Object item);

}

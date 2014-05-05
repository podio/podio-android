/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

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

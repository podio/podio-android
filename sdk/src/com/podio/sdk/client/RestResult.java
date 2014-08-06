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

package com.podio.sdk.client;

import com.podio.sdk.domain.Session;

/**
 * Wraps the result of a performed {@link RestRequest}.
 * 
 * @author László Urszuly
 */
public class RestResult<T> {

    /**
     * Creates a new success result object with no further information.
     * 
     * @return A success result.
     */
    public static <T> RestResult<T> success() {
        return success(null);
    }

    /**
     * Creates a new success result object, containing the requested data.
     * 
     * @return A success result.
     */
    public static <T> RestResult<T> success(T item) {
        return success(item, null);
    }

    /**
     * Creates a new success result object, containing the requested data and
     * the new session information.
     * 
     * @return A success result.
     */
    public static <T> RestResult<T> success(T item, Session session) {
        return new RestResult<T>(item, session);
    }

    private final Session session;
    private final T item;

    /**
     * Creates a new rest result object with the given parameters.
     * 
     * @param item
     *        Optional content of this object.
     * @param session
     *        The new session variables, if changed. Otherwise null.
     */
    private RestResult(T item, Session session) {
        this.item = item;
        this.session = session;
    }

    /**
     * Returns whether this result object contains new session information.
     * 
     * @return
     */
    public boolean hasSession() {
        return session != null;
    }

    /**
     * Returns whether this result object contains new session information and,
     * if so, the information indicates an authorized session.
     * 
     * @return
     */
    public boolean hasAuthorizedSession() {
        return hasSession() && session.isAuthorized();
    }

    /**
     * Returns the new session information associated with this result.
     * 
     * @return A session object.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns any content associated with the corresponding request.
     * 
     * @return The requested content.
     */
    public T getItem() {
        return item;
    }

}

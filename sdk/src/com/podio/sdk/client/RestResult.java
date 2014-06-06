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
    private final boolean isSuccess;
    private final Session session;
    private final String message;
    private final T item;

    /**
     * Constructor. The one and only way to set the state of this object.
     * 
     * @param isSuccess
     *            Boolean true if this object represents a successfully
     *            performed {@link RestRequest}. Boolean false otherwise.
     * @param message
     *            A optional message provided to the caller by the creator of
     *            this object.
     * @param item
     *            Optional content of this object.
     */
    public RestResult(boolean isSuccess, String message, T item) {
        this(isSuccess, null, message, item);
    }

    /**
     * Constructor. The one and only way to set the state of this object.
     * 
     * @param isSuccess
     *            Boolean true if this object represents a successfully
     *            performed {@link RestRequest}. Boolean false otherwise.
     * @param session
     *            The new session variables, if changed. Otherwise null.
     * @param message
     *            A optional message provided to the caller by the creator of
     *            this object.
     * @param item
     *            Optional content of this object.
     */
    public RestResult(boolean isSuccess, Session session, String message, T item) {
        this.isSuccess = isSuccess;
        this.session = session;
        this.message = message;
        this.item = item;
    }

    /**
     * Returns the read-only success state.
     * 
     * @return Boolean true if the corresponding request was successfully
     *         performed. Boolean false otherwise.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    public Session session() {
        return session;
    }

    /**
     * Returns any items associated with the corresponding request. May be null.
     * 
     * @return A list of items or null.
     */
    public T item() {
        return item;
    }

    /**
     * Returns any message provided by the underlying infrastructure. May be
     * null or empty.
     * 
     * @return A message string.
     */
    public String message() {
        return message;
    }
    
    public static <T> RestResult<T> failure() {
    	return failure(null);    	
    }
    
    public static <T> RestResult<T> failure(String message) {
    	return new RestResult<T>(false, message, null);
    }
    
    public static <T> RestResult<T> success() {
    	return success(null);    	
    }
    
    public static <T> RestResult<T> success(T item) {
    	return new RestResult<T>(true, null, item);
    }

}

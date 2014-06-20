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

import java.util.HashMap;

/**
 * @author László Urszuly
 */
public class PodioException extends RuntimeException {
    private static final long serialVersionUID = -798570582194918180L;

    private static final class ErrorRequest {
        private final String url = null;
        private final String query_string = null;
        private final String method = null;
    }

    private final HashMap<String, String> error_parameters = null;
    private final String error_detail = null;
    private final Boolean error_propagate = null;
    private final ErrorRequest request = null;
    private final String error_description = null;
    private final String error = null;

    private Integer statusCode = null;

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException()
     */
    public PodioException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException(String))
     */
    public PodioException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public PodioException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException(String, Throwable))
     */
    public PodioException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Returns the API provided <code>error_propagate</code> state flag.
     * 
     * @return Boolean. Defaults to false if not stated.
     */
    public boolean doPropagate() {
        return error_propagate != null ? error_propagate.booleanValue() : false;
    }

    /**
     * Returns the API provided <code>error_description</code>.
     * 
     * @return String or null.
     */
    public String getDescription() {
        return error_description;
    }

    /**
     * Returns the API provided <code>error</code> name.
     * 
     * @return String or null
     */
    public String getError() {
        return error;
    }

    /**
     * Returns the API provided <code>error_detail</code>.
     * 
     * @return String or null.
     */
    public String getErrorDetail() {
        return error_detail;
    }

    /**
     * Returns the <code>error_parameter</code> with the given name as a string.
     * 
     * @param key
     *        The name of the parameter.
     * @return String or null.
     */
    public String getErrorParameter(String key) {
        return error_parameters != null ? error_parameters.get(key) : null;
    }

    /**
     * Returns the <code>error_parameter</code> with the given name as an
     * integer.
     * 
     * @param key
     *        The name of the parameter.
     * @param fallback
     *        The number to return if the sought parameter doesn't exist or
     *        can't be parsed as an <code>int</code> value.
     * @return Integer.
     */
    public int getErrorParameterAsInt(String key, int fallback) {
        String value = getErrorParameter(key);

        try {
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
            return fallback;
        } catch (NullPointerException e) {
            return fallback;
        }
    }

    /**
     * Returns the method of the request that caused this API error to be
     * thrown.
     * 
     * @return String or null.
     */
    public String getMethod() {
        return request != null ? request.method : null;
    }

    /**
     * Returns the query string of the request that caused this API error to be
     * thrown.
     * 
     * @return String or null.
     */
    public String getQueryString() {
        return request != null ? request.query_string : null;
    }

    /**
     * Returns the HTTP status code of the request that caused this API error to
     * be thrown.
     * 
     * @return HTTP status as an integer or -1.
     */
    public int getStatusCode() {
        return statusCode != null ? statusCode.intValue() : -1;
    }

    /**
     * Returns the URL of the request that caused this API error to be thrown.
     * 
     * @return String or null.
     */
    public String getUrl() {
        return request != null ? request.url : null;
    }

    /**
     * Returns boolean <code>true</code> if there is an error parameter with the
     * given name in the <code>error_parameters</code> map.
     * 
     * @param key
     *        The name of the sought error parameter.
     * @return Boolean.
     */
    public boolean hasErrorParameter(String key) {
        return error_parameters != null && error_parameters.containsKey(key);
    }

    /**
     * Initializes the status code of this exception. This method can only be
     * called once.
     * 
     * @param statusCode
     */
    public void initStatusCode(int statusCode) throws IllegalStateException {
        if (this.statusCode == null) {
            this.statusCode = Integer.valueOf(statusCode);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Checks whether this API error was thrown due to a session being
     * unauthorized or expired.
     * 
     * @return Boolean <code>true</code> if the session was unauthorized or
     *         expired.
     */
    public boolean isExpiredError() {
        return getStatusCode() == 401 || "unauthorized".equals(error) || "expired_token".equals(error_description);
    }
}

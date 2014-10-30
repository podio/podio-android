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

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public class PodioError extends RuntimeException {
    private transient static final long serialVersionUID = -798570582194918180L;

    private static final class ErrorRequest {
        private final String url = null;
        private final String query_string = null;
        private final String method = null;
    }

    public static PodioError fromThrowable(Throwable throwable) {
        return fromThrowable(throwable, null);
    }

    /**
     * Wraps the given {@link Throwable} with a new PodioException instance.
     * 
     * @param throwable
     *        The cause for this PodioException.
     * @param detailMessage
     *        (Optional) detail message to show in the stack trace.
     * @return A new PodioException instance.
     */
    public static PodioError fromThrowable(Throwable throwable, String detailMessage) {
        VolleyError volleyError;

        if (throwable instanceof VolleyError) {
            volleyError = (VolleyError) throwable;
        } else if (throwable.getCause() instanceof VolleyError) {
            volleyError = (VolleyError) throwable.getCause();
        } else {
            return new PodioError(detailMessage, throwable);
        }

        if (volleyError.networkResponse != null && Utils.notEmpty(volleyError.networkResponse.data)) {
            NetworkResponse response = volleyError.networkResponse;
            byte[] errorData = response.data;
            int statusCode = response.statusCode;

            String json = new String(errorData);
            return fromJson(json, statusCode, throwable);
        } else {
            return new PodioError(detailMessage, throwable);
        }
    }

    /**
     * Constructs a new PodioException instance from the given data.
     * 
     * @param json
     *        The API provided error JSON.
     * @param statusCode
     *        The HTTP status code that describes the error.
     * @param cause
     *        (Optional) cause from the error. This is only used for maintaining
     *        the stack trace hierarchy.
     * @return A new PodioException instance.
     */
    public static PodioError fromJson(String json, int statusCode, Throwable cause) {
        PodioError podioError;

        if (Utils.notEmpty(json)) {
            podioError = JsonParser.fromJson(json, PodioError.class);
        } else {
            podioError = new PodioError();
        }

        podioError.initCause(cause);
        podioError.initSource(json);
        podioError.initStatusCode(statusCode);

        return podioError;
    }

    private final HashMap<String, String> error_parameters = null;
    private final String error_detail = null;
    private final Boolean error_propagate = null;
    private final ErrorRequest request = null;
    private final String error_description = null;
    private final String error = null;

    private transient Integer statusCode = null;
    private transient String source = null;

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException()
     */
    private PodioError() {
        super();
    }

    /**
     * Constructor.
     * 
     * @see RuntimeException#RuntimeException(String, Throwable))
     */
    private PodioError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();

        if (Utils.isEmpty(message)) {
            return source;
        } else {
            return message;
        }
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
     * Returns the source JSON string for this PodioException.
     * 
     * @return String or null;
     */
    public String getSourceJson() {
        return source;
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
     * Initializes the source JSON of this exception. This method can only be
     * called once.
     * 
     * @param json
     *        The source JSON string for this exception.
     */
    public void initSource(String json) throws IllegalStateException {
        if (this.source == null) {
            this.source = json;
        } else {
            throw new IllegalStateException("Source already initialized: " + source);
        }
    }

    /**
     * Initializes the status code of this exception. This method can only be
     * called once.
     * 
     * @param statusCode
     *        The desired (HTTP?) status code for this exception.
     */
    public void initStatusCode(int statusCode) throws IllegalStateException {
        if (this.statusCode == null) {
            this.statusCode = Integer.valueOf(statusCode);
        } else {
            throw new IllegalStateException("Status code already initialized: " + this.statusCode);
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
        return getStatusCode() == 401 || "unauthorized".equals(error) || "invalid_grant".equals(error);
    }
}

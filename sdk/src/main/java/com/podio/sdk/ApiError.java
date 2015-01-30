/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.podio.sdk;

import com.podio.sdk.internal.Utils;

import java.util.HashMap;

/**
 * This class represents a server side error returned by the Podio API. These errors are fully
 * completed network responses and they model an unhandled state (not a crash) in the API, caused by
 * the client request. Examples of issues can be missing permissions, expired authentication or
 * invalid client requests (like asking for the eleventh item in a list of ten).
 *
 * @author László Urszuly
 */
public class ApiError extends PodioError {

    /**
     * Sub object of the ErrorBundle class.
     *
     * @author László Urszuly
     */
    private static final class ErrorRequest {
        private final String url = null;
        private final String query_string = null;
        private final String method = null;
    }

    /**
     * The very Podio error domain model as sent by the API. This class is used to isolate the
     * object layout and not have the PodioError exception being directly dependent of it.
     *
     * @author László Urszuly
     */
    private static final class ErrorBundle {
        private final HashMap<String, String> error_parameters = null;
        private final String error_detail = null;
        private final Boolean error_propagate = null;
        private final ErrorRequest request = null;
        private final String error_description = null;
        private final String error = null;
    }

    private ErrorBundle errorBundle = null;
    private Integer statusCode = null;

    /**
     * Creates a new ApiError runtime exception. The given json will serve as the exception message.
     * This implementation will also parse the json for quick and easy runtime access to the
     * contained data.
     *
     * @param json
     *         The Podio API provided error body.
     * @param statusCode
     *         The associated HTTP status code.
     */
    public ApiError(String json, int statusCode) {
        this(json, statusCode, null);
    }

    /**
     * Same as {@link com.podio.sdk.ApiError#ApiError(String, int)} but also with the possibility to
     * provide an underlying cause.
     *
     * @param json
     *         The Podio API provided error body.
     * @param statusCode
     *         The associated HTTP status code.
     * @param cause
     *         The logical exception cause that delivered this ApiError. This depends on the
     *         underlying implementation.
     */
    public ApiError(String json, int statusCode, Throwable cause) {
        super(json, cause);
        this.statusCode = statusCode;

        if (Utils.notEmpty(json)) {
            errorBundle = JsonParser.fromJson(json, ErrorBundle.class);
        }
    }

    /**
     * Returns the API provided {@code error_propagate} state flag.
     *
     * @return Boolean. Defaults to false if not stated.
     */
    public boolean doPropagate() {
        return errorBundle != null && Utils.getNative(errorBundle.error_propagate, false);
    }

    /**
     * Returns the API provided {@code error_description}.
     *
     * @return String or null.
     */
    public String getErrorDescription() {
        return errorBundle != null ? errorBundle.error_description : null;
    }

    /**
     * Returns the API provided {@code error} name.
     *
     * @return String or null
     */
    public String getError() {
        return errorBundle != null ? errorBundle.error : null;
    }

    /**
     * Returns the API provided {@code error_detail}.
     *
     * @return String or null.
     */
    public String getErrorDetail() {
        return errorBundle != null ? errorBundle.error_detail : null;
    }

    /**
     * Returns the {@code error_parameter} with the given name as a string.
     *
     * @param key
     *         The name of the parameter.
     *
     * @return String or null.
     */
    public String getErrorParameter(String key) {
        return errorBundle != null && errorBundle.error_parameters != null ? errorBundle.error_parameters.get(key) : null;
    }

    /**
     * Returns the {@code error_parameter} with the given name as an integer.
     *
     * @param key
     *         The name of the parameter.
     * @param fallback
     *         The number to return if the sought parameter doesn't exist or can't be parsed as an
     *         {@code int} value.
     *
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
     * Returns the method of the request that caused this API error to be thrown.
     *
     * @return String or null.
     */
    public String getMethod() {
        return errorBundle != null && errorBundle.request != null ? errorBundle.request.method : null;
    }

    /**
     * Returns the query string of the request that caused this API error to be thrown.
     *
     * @return String or null.
     */
    public String getQueryString() {
        return errorBundle != null && errorBundle.request != null ? errorBundle.request.query_string : null;
    }

    /**
     * Returns the HTTP status code of the request that caused this API error to be thrown.
     *
     * @return HTTP status as an integer or -1.
     */
    public int getStatusCode() {
        return statusCode != null ? statusCode : -1;
    }

    /**
     * Returns the URL of the request that caused this API error to be thrown.
     *
     * @return String or null.
     */
    public String getUrl() {
        return errorBundle != null && errorBundle.request != null ? errorBundle.request.url : null;
    }

    /**
     * Returns boolean true if there is an error parameter with the given name in the {@code
     * error_parameters} map.
     *
     * @param key
     *         The name of the sought error parameter.
     *
     * @return Boolean.
     */
    public boolean hasErrorParameter(String key) {
        return errorBundle != null && errorBundle.error_parameters != null && errorBundle.error_parameters.containsKey(key);
    }

    /**
     * Checks whether this API error was thrown due to a session having invalid grants.
     *
     * @return Boolean true if the session has invalid grands.
     */
    public boolean isAuthError() {
        int status = getStatusCode();
        String error = getError();
        String description = getErrorDescription();
        return status == 401 ||
                ("unauthorized".equals(error) && "expired_token".equals(description)) ||
                (status == 400 && ("invalid_grant".equals(error) || ("invalid_client".equals(error) && "invalid_auth".equals(description))));
    }

}

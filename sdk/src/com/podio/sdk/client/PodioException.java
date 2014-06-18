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

/**
 * @author László Urszuly
 */
public class PodioException extends Exception {
    private static final long serialVersionUID = -798570582194918180L;

    private static final class ErrorParameters {
    }

    private static final class ErrorRequest {
        private final String url = null;
        private final String query_string = null;
        private final String method = null;
    }

    private final ErrorParameters error_parameters = null;
    private final String error_detail = null;
    private final Boolean error_propagate = null;
    private final ErrorRequest request = null;
    private final String error_description = null;
    private final String error = null;

    private Integer statusCode = null;

    public PodioException() {
        super();
    }

    public PodioException(Throwable throwable) {
        super(throwable);
    }

    public PodioException(String detailMessage) {
        super(detailMessage);
    }

    public PodioException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public boolean doPropagate() {
        return error_propagate != null ? error_propagate.booleanValue() : false;
    }

    public String getDescription() {
        return error_description;
    }

    public String getError() {
        return error;
    }

    public String getErrorDetail() {
        return error_detail;
    }

    public String getMethod() {
        return request != null ? request.method : null;
    }

    public String getQueryString() {
        return request != null ? request.query_string : null;
    }

    public int getStatusCode() {
        return statusCode != null ? statusCode.intValue() : -1;
    }

    public String getUrl() {
        return request != null ? request.url : null;
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

    public boolean isExpiredError() {
        return statusCode == 401 || "unauthorized".equals(error) || "expired_token".equals(error_description);
    }
}

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

import com.android.volley.VolleyError;

/**
 * This class represents a generic client side error with an undefined or unknown cause.
 *
 * @author László Urszuly
 */
public class PodioError extends RuntimeException {

    private int responseCode;

    public PodioError(String message, Throwable cause) {
        super(message, cause);
        responseCode = 0;
    }

    public PodioError(String message) {
        super(message);
        responseCode = 0;
    }

    public PodioError(Throwable cause) {
        super(cause);
        responseCode = 0;
    }

    public PodioError(VolleyError volleyError, int responseCode) {
        super(volleyError);
        this.responseCode = responseCode;
    }

    /**
     * In the scenario that our API does not return a json error we will fallback to using
     * PodioError rather than ApiError and in such scenario this method will return the response
     * code but PodioError is used in more scenarios than actual API responses and in such scenarios
     * the response code returned will be 0 and thus not valid.
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }
}

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
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.PodioRequest;
import com.podio.sdk.domain.User;
import com.podio.sdk.volley.VolleyProvider;

/**
 * Enables access to the User API end point.
 * 
 * @author László Urszuly
 */
public class UserProvider extends VolleyProvider {

    static class Path extends Filter {

        private Path() {
            super("user");
        }

        private Path withProfile() {
            addPathSegment("profile");
            return this;
        }

        private Path withStatus() {
            addPathSegment("status");
            return this;
        }

        private Path withProperty(String property) {
            addQueryParameter("name", property);
            return this;
        }

    }

    /**
     * Fetches the currently logged in user data.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<User> getData() {
        Path filter = new Path();
        return get(filter, User.class);
    }

    /**
     * Fetches the currently logged in user profile.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<User.Profile> getProfile() {
        Path filter = new Path().withProfile();
        return get(filter, User.Profile.class);
    }

    /**
     * Fetches the currently logged in user status.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public PodioRequest<User> getUserStatus() {
        Path filter = new Path().withStatus();
        return get(filter, User.class);
    }

}

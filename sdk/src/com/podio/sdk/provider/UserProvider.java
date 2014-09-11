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

import com.podio.sdk.client.RequestFuture;
import com.podio.sdk.domain.User;
import com.podio.sdk.filter.UserFilter;

/**
 * Enables access to the User API end point.
 * 
 * @author László Urszuly
 */
public class UserProvider extends BasicPodioProvider {

    /**
     * Fetches the currently logged in user data.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<User> getData() {
        UserFilter filter = new UserFilter();
        return get(filter, User.class);
    }

    /**
     * Fetches the currently logged in user profile.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public RequestFuture<User.Profile> getProfile() {
        UserFilter filter = new UserFilter().withProfile();
        return get(filter, User.Profile.class);
    }

    public RequestFuture<User> getUserStatus() {
        UserFilter filter = new UserFilter().withStatus();
        return get(filter, User.class);
    }

}

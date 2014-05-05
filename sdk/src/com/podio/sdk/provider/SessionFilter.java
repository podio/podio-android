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

public class SessionFilter extends BasicPodioFilter {
    public static final String PATH = "oauth/token";

    public SessionFilter() {
        super(PATH);
    }

    public SessionFilter withClientCredentials(String clientId, String clientSecret) {
        addQueryParameter("client_id", clientId);
        addQueryParameter("client_secret", clientSecret);
        return this;
    }

    public SessionFilter withUserCredentials(String username, String password) {
        addQueryParameter("grant_type", "password");
        addQueryParameter("username", username);
        addQueryParameter("password", password);
        return this;
    }

    public SessionFilter withAppCredentials(String appId, String appToken) {
        addQueryParameter("grant_type", "app");
        addQueryParameter("app_id", appId);
        addQueryParameter("app_token", appToken);
        return this;
    }
}

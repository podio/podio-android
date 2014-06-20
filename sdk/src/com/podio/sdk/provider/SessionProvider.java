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

import java.util.concurrent.Future;

import com.podio.sdk.ErrorListener;
import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioParser;
import com.podio.sdk.RestClient;
import com.podio.sdk.ResultListener;
import com.podio.sdk.SessionListener;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.domain.Session;
import com.podio.sdk.filter.SessionFilter;
import com.podio.sdk.internal.request.RestOperation;

public class SessionProvider extends BasicPodioProvider {

    public SessionProvider(RestClient client) {
        super(client);
    }

    public Future<RestResult<Session>> authenticateWithUserCredentials(String clientId, String clientSecret,
            String username, String password, ResultListener<? super Session> resultListener, ErrorListener errorListener, SessionListener sessionListener) {

        PodioFilter filter = new SessionFilter()
                .withClientCredentials(clientId, clientSecret)
                .withUserCredentials(username, password);

        return authorize(filter, resultListener, errorListener, sessionListener);
    }

    public Future<RestResult<Session>> authenticateWithAppCredentials(String clientId, String clientSecret,
            String appId, String appToken, ResultListener<? super Session> resultListener, ErrorListener errorListener, SessionListener sessionListener) {

        PodioFilter filter = new SessionFilter()
                .withClientCredentials(clientId, clientSecret)
                .withAppCredentials(appId, appToken);

        return authorize(filter, resultListener, errorListener, sessionListener);
    }

    private Future<RestResult<Session>> authorize(PodioFilter filter, ResultListener<? super Session> resultListener, ErrorListener errorListener, SessionListener sessionListener) {
        return request(RestOperation.AUTHORIZE, filter, null, PodioParser.fromClass(Session.class), resultListener, errorListener, sessionListener);
    }
}

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

import com.podio.sdk.PodioFilter;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.delegate.ItemParser;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.RestOperation;

public class SessionProvider extends BasicPodioProvider {

    public SessionProvider(RestClient client) {
        super(client);
    }

    public Object authenticateWithUserCredentials(String clientId, String clientSecret,
            String username, String password) {

        ItemParser<Session> parser = new ItemParser<Session>(Session.class);

        PodioFilter filter = new SessionFilter() //
                .withClientCredentials(clientId, clientSecret) //
                .withUserCredentials(username, password);

        return authorize(filter, parser);
    }

    public Object authenticateWithAppCredentials(String clientId, String clientSecret,
            String appId, String appToken) {

        ItemParser<Session> parser = new ItemParser<Session>(Session.class);

        PodioFilter filter = new SessionFilter() //
                .withClientCredentials(clientId, clientSecret) //
                .withAppCredentials(appId, appToken);

        return authorize(filter, parser);
    }

    private Object authorize(PodioFilter filter, ItemParser<Session> itemParser) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.AUTHORIZE, filter, null,
                    itemParser);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }
}

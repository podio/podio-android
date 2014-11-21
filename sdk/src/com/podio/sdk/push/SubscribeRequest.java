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
package com.podio.sdk.push;

import java.util.concurrent.Callable;

class SubscribeRequest extends PushRequest<Void> {

    private static final class SubscribeData {
        @SuppressWarnings("unused")
        private final String channel;

        @SuppressWarnings("unused")
        private final String clientId;

        @SuppressWarnings("unused")
        private final String subscription;

        @SuppressWarnings("unused")
        private final ExtData ext;

        private SubscribeData(String clientId, String subscription, String signature, String timestamp) {
            this.channel = "/meta/subscribe";
            this.clientId = clientId;
            this.subscription = subscription;
            this.ext = new ExtData(signature, timestamp);
        }
    }

    SubscribeRequest(final String channel, final String signature, final String timestamp, final Transport transport) {
        super(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                SubscribeData subscribeData = new SubscribeData(clientId, channel, signature, timestamp);
                send(transport, subscribeData);
                return null;
            }

        });
    }
}

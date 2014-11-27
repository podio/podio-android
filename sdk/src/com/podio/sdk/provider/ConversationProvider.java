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
import com.podio.sdk.Request;
import com.podio.sdk.domain.Conversation;
import com.podio.sdk.volley.VolleyProvider;

/**
 * Enables access to the Conversation API end point.
 * 
 * @author László Urszuly
 */
public class ConversationProvider extends VolleyProvider {

    static class Path extends Filter {

        protected Path() {
            super("conversation");
        }

        Path withId(long id) {
            addPathSegment(Long.toString(id, 10));
            return this;
        }

        Path withSpan(int limit, int offset) {
            addQueryParameter("limit", Integer.toString(limit, 10));
            addQueryParameter("offset", Integer.toString(offset, 10));
            return this;
        }

        Path withEvents(long id, int limit, int offset) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("event");
            addQueryParameter("limit", Integer.toString(limit, 10));
            addQueryParameter("offset", Integer.toString(offset, 10));
            return this;
        }
    }

    /**
     * Fetches the given Conversation span.
     * 
     * @param limit
     *        The number of conversations to fetch.
     * @param offset
     *        The number of conversations to skip before start fetching.
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation[]> getConversations(int limit, int offset) {
        Path filter = new Path().withSpan(limit, offset);
        return get(filter, Conversation[].class);
    }

    /**
     * Fetches the Conversation with the given id.
     * 
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation> getConversation(long id) {
        Path filter = new Path().withId(id);
        return get(filter, Conversation.class);
    }

    /**
     * Fetches the events for the conversation with the given id.
     * 
     * @param id
     *        The id of the conversation.
     * @param limit
     *        The number of events to fetch.
     * @param offset
     *        The number of events to skip before start fetching.
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation.Event[]> getConversationMessages(long id, int limit, int offset) {
        Path filter = new Path().withEvents(id, limit, offset);
        return get(filter, Conversation.Event[].class);
    }

}

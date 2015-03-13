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
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Conversation;

import java.util.HashMap;

/**
 * Enables access to the Conversation API end point.
 *
 * @author László Urszuly
 */
public class ConversationProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("conversation");
        }

        Path withCreate() {
            addPathSegment("v2");
            return this;
        }

        Path withEvent(long id) {
            addPathSegment("event");
            addPathSegment(Long.toString(id));
            return this;
        }

        Path withEvents(long id) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("event");
            return this;
        }

        Path withId(long id) {
            addPathSegment(Long.toString(id, 10));
            return this;
        }

        Path withLeaveFlag(long id) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("leave");
            return this;
        }

        Path withMoreParticipants(long id) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("participant");
            addPathSegment("v2");
            return this;
        }

        Path withParticipantsOnly() {
            addQueryParameter("participants", "true");
            return this;
        }

        Path withReadFlag(long id) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("read");
            return this;
        }

        Path withReadFlag() {
            addPathSegment("read");
            return this;
        }

        Path withReply(long id) {
            addPathSegment(Long.toString(id, 10));
            addPathSegment("reply");
            addPathSegment("v2");
            return this;
        }

        Path withSearch(String text) {
            addPathSegment("search");
            addQueryParameter("text", text);
            return this;
        }

        Path withSpan(int limit, int offset) {
            addQueryParameter("limit", Integer.toString(limit, 10));
            addQueryParameter("offset", Integer.toString(offset, 10));
            return this;
        }

    }

    /**
     * Adds more profiles to a conversation.
     *
     * @param conversationId
     *         The id of the conversation.
     * @param participantIds
     *         The user ids of the profiles to add.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> addParticipants(long conversationId, long[] participantIds) {
        Path filter = new Path().withMoreParticipants(conversationId);
        HashMap<String, long[]> data = new HashMap<String, long[]>();
        data.put("participants", participantIds);
        return post(filter, data, Void.class);
    }

    /**
     * Creates a new conversation as of the parameters in the given template.
     *
     * @param data
     *         The parameters for the new conversation.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation> createConversation(Conversation.Create data) {
        Path filter = new Path().withCreate();
        return post(filter, data, Conversation.class);
    }

    /**
     * Fetches the given Conversation span.
     *
     * @param limit
     *         The number of conversations to fetch.
     * @param offset
     *         The number of conversations to skip before start fetching.
     *
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
     *         The id of the conversation.
     * @param limit
     *         The number of events to fetch.
     * @param offset
     *         The number of events to skip before start fetching.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation.Event[]> getConversationEvents(long id, int limit, int offset) {
        Path filter = new Path().withEvents(id).withSpan(limit, offset);
        return get(filter, Conversation.Event[].class);
    }

    /**
     * Fetches a single conversation event with the given id.
     *
     * @param id
     *         The id of the conversation event.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation.Event> getConversationEvent(long id) {
        Path filter = new Path().withEvent(id);
        return get(filter, Conversation.Event.class);
    }

    /**
     * Removes the current user from the conversation with the given id.
     *
     * @param id
     *         The id of the conversation to leave.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> leaveConversation(long id) {
        Path filter = new Path().withLeaveFlag(id);
        return post(filter, null, Void.class);
    }

    /**
     * Marks the conversation with the given id as "read".
     *
     * @param conversationId
     *         The id of the conversation.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> markConversationAsRead(long conversationId) {
        Path filter = new Path().withReadFlag(conversationId);
        return post(filter, null, Void.class);
    }

    /**
     * Marks all the users conversations as read.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> markAllConversationsAsRead() {
        Path filter = new Path().withReadFlag();
        return post(filter, null, Void.class);
    }

    /**
     * Sends a reply to the conversation with the given id. The request result will deliver the
     * generated conversation event.
     *
     * @param message
     *         The reply body.
     * @param link
     *         The embedded link (if any).
     * @param fileIds
     *         The list of ids of any files attached to the reply.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation.Event> replyToConversation(long conversationId, String message, String link, long[] fileIds) {
        Path filter = new Path().withReply(conversationId);
        Conversation.Reply reply = new Conversation.Reply(message, link, fileIds);
        return post(filter, reply, Conversation.Event.class);
    }

    /**
     * Searches the conversations for the given text snippet.
     *
     * @param text
     *         The text to match conversations to.
     * @param limit
     *         The maximum number of replies to return.
     * @param offset
     *         The number of replies to skip before start counting the number of replies to pick.
     * @param searchParticipants
     *         Whether to search for participant names or not.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Conversation[]> searchConversations(String text, int limit, int offset, boolean searchParticipants) {
        Path filter = new Path().withSearch(text).withSpan(limit, offset);

        if (searchParticipants) {
            filter.withParticipantsOnly();
        }

        return get(filter, Conversation[].class);
    }
}

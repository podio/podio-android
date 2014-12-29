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
package com.podio.sdk.domain.push;

import com.podio.sdk.internal.Utils;

public class ConversationRead extends Event {

    private static class Data {
        /**
         * The id of the conversation that was changed.
         */
        private final Long conversation_id = null;

        /**
         * The number of unread events on the conversation.
         */
        private final Integer unread_count = null;

        /**
         * The total number of unread events.
         */
        private final Integer total_unread_count = null;
    }

    private final Data data = null;

    public long conversationId() {
        return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
    }

    public int unreadMessagesCountInConversation() {
        return data != null ? Utils.getNative(data.unread_count, -1) : -1;
    }

    public int unreadMessagesCountInTotal() {
        return data != null ? Utils.getNative(data.total_unread_count, -1) : -1;
    }
}

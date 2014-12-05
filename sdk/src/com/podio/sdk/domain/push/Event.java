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

public abstract class Event {

    /**
     * All supported event types.
     *
     * @author László Urszuly
     */
    public static enum Type {
        comment_create(CommentCreate.class),
        comment_delete(CommentDelete.class),
        comment_update(CommentUpdate.class),
        conversation_event(ConversationEvent.class),
        conversation_read(ConversationRead.class),
        conversation_read_all(ConversationReadAll.class),
        conversation_starred(ConversationStarred.class),
        conversation_starred_count(ConversationStarredCount.class),
        conversation_unread(ConversationUnread.class),
        conversation_unread_count(ConversationUnreadCount.class),
        conversation_unstarred(ConversationUnstarred.class),
        create(ObjectCreate.class),
        delete(ObjectDelete.class),
        file_attach(FileAttach.class),
        file_delete(FileDelete.class),
        grant_create(GrantCreate.class),
        grant_delete(GrantDelete.class),
        // TODO: Add support for "live" events as well.
        // live_accept(),
        // live_decline,
        // live_end,
        // live_settings,
        notification_create(NotificationCreate.class),
        notification_unread(NotificationUnread.class),
        rating_like_create(RatingCreate.class),
        rating_like_delete(RatingDelete.class),
        stream_create(StreamCreate.class),
        stream_event(StreamEvent.class),
        subscribe(Subscribe.class),
        typing(Typing.class),
        unsubscribe(Unsubscribe.class),
        update(ObjectUpdate.class),
        viewing(Viewing.class),
        unknown(null);

        private final Class<? extends Event> classOfEvent;

        private Type(Class<? extends Event> classOfEvent) {
            this.classOfEvent = classOfEvent;
        }

        public Class<? extends Event> getClassObject() {
            return classOfEvent;
        }
    }

    /**
     * A helper class, describing an arbitrary reference.
     *
     * @author László Urszuly
     */
    protected static class Reference {
        private final String type = null;
        private final Long id = null;

        String type() {
            return type;
        }

        long id() {
            return Utils.getNative(id, -1L);
        }
    }

    /**
     * The reference to the carrier of the event
     */
    private final Reference ref = null;
    private final String event = null;
    private final Reference created_by = null;
    private final String created_via = null;

    public String referenceType() {
        return ref != null ? ref.type : null;
    }

    public long referenceId() {
        return ref != null ? Utils.getNative(ref.id, -1L) : -1L;
    }

    public Type event() {
        try {
            return Type.valueOf(event);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
        }
    }

    public String createdByType() {
        return created_by != null ? created_by.type : null;
    }

    public long createdById() {
        return created_by != null ? Utils.getNative(created_by.id, -1L) : -1L;
    }

    public String createdVia() {
        return created_via;
    }

}

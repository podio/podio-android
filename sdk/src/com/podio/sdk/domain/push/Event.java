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

abstract class Event {

    /**
     * All supported event types.
     * 
     * @author László Urszuly
     */
    public static enum Type {
        comment_create,
        comment_delete,
        comment_update,
        conversation_event,
        conversation_read,
        conversation_read_all,
        conversation_starred,
        conversation_starred_count,
        conversation_unread,
        conversation_unread_count,
        conversation_unstarred,
        create,
        delete,
        file_attach,
        file_delete,
        grant_create,
        grant_delete,
        live_accept,
        live_decline,
        live_end,
        live_settings,
        notification_create,
        notification_unread,
        rating_like_create,
        rating_like_delete,
        stream_create,
        stream_event,
        subscribe,
        typing,
        unsubscribe,
        update,
        viewing,
        unknown
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

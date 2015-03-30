/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public abstract class PushEvent {

    /**
     * All supported event types.
     *
     * @author László Urszuly
     */
    public static enum Type {
        conversation_event(Conversation.PushNewEvent.class),
        conversation_read(Conversation.PushRead.class),
        conversation_read_all(Conversation.PushReadAll.class),
        conversation_starred(Conversation.PushStarred.class),
        conversation_starred_count(Conversation.PushStarredCount.class),
        conversation_unread(Conversation.PushUnread.class),
        conversation_unread_count(Conversation.PushUnreadCount.class),
        conversation_unstarred(Conversation.PushUnstarred.class),
        unknown(null);

        private final Class<? extends PushEvent> classOfEvent;

        private Type(Class<? extends PushEvent> classOfEvent) {
            this.classOfEvent = classOfEvent;
        }

        public Class<? extends PushEvent> getClassObject() {
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

}

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
package com.podio.sdk.domain;

import java.util.Date;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the ConversationDTO API domain object.
 *
 * @author László Urszuly
 */
public class Conversation {
    public static class Reply {
        @SuppressWarnings("unused")
        private final String text;

        @SuppressWarnings("unused")
        private final String embed_url;

        @SuppressWarnings("unused")
        private final long[] file_ids;

        public Reply(String text, String embedUrl, long[] fileIds) {
            this.text = text;
            this.embed_url = embedUrl;
            this.file_ids = fileIds;
        }
    }

    public static class Create {
        @SuppressWarnings("unused")
        private final String subject;

        @SuppressWarnings("unused")
        private final String text;

        @SuppressWarnings("unused")
        private final String embed_url;

        @SuppressWarnings("unused")
        private final long[] file_ids;

        @SuppressWarnings("unused")
        private final long[] participants;

        public Create(String subject, String text, String link, long[] participants, long[] fileIds) {
            this.subject = subject;
            this.text = text;
            this.embed_url = link;
            this.file_ids = Utils.notEmpty(fileIds) ? fileIds : new long[0];
            this.participants = Utils.notEmpty(participants) ? participants : new long[0];
        }
    }

    public static class Data {
        private final Long message_id = null;
        private final File[] files = null;
        private final String text = null;
        private final String created_on = null;

        public File[] getFiles() {
            return files.clone();
        }

        public String text() {
            return text;
        }

        public Date getCreatedOnDateTime() {
            return Utils.parseDateTime(created_on);
        }

        public String getCreatedOnDateTimeString() {
            return created_on;
        }

        public long getMessageId() {
            return Utils.getNative(message_id, -1L);
        }
    }

    public static class Event {
        private final Long event_id = null;
        private final Byline created_by = null;
        private final String created_on = null;
        private final String action = null;
        private final Data data = null;

        public long getEventId() {
            return Utils.getNative(event_id, -1L);
        }

        public Byline getCreatedBy() {
            return created_by;
        }

        public long getCreatedById() {
            return created_by != null ? created_by.getId() : -1L;
        }

        public Date getCreatedDate() {
            return Utils.parseDateTime(created_on);
        }

        public String getCreatedDateString() {
            return created_on;
        }

        public String getAction() {
            return action;
        }

        public long getMessageId() {
            return data != null ? Utils.getNative(data.message_id, -1L) : -1L;
        }

        public File[] getMessageFiles() {
            return data != null && data.files != null ? data.files.clone() : new File[0];
        }

        public String getMessageText() {
            return data != null ? data.text : null;
        }

        public Date getMessageCreatedDate() {
            return data != null ? Utils.parseDateTime(data.created_on) : null;
        }

        public String getMessageCreatedDateString() {
            return data != null ? data.created_on : null;
        }

    }

    private final Long conversation_id = null;
    private final Boolean pinned = null;
    private final Boolean starred = null;
    private final Boolean unread = null;
    private final Integer unread_count = null;
    private final Byline created_by = null;
    private final Profile[] participants = null;
    private final String created_on = null;
    private final String last_event_on = null;
    private final String link = null;
    private final String type = null; // See "Type" enum.
    private final String subject = null;
    private final String excerpt = null;
    private final Presence presence = null;
    private final Push push = null;

    public long getConversationId() {
        return Utils.getNative(conversation_id, -1L);
    }

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public Byline getCreator() {
        return created_by;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public boolean hasUnreadMessages() {
        return Utils.getNative(unread, false);
    }

    public boolean isPinned() {
        return Utils.getNative(pinned, false);
    }

    public boolean isStarred() {
        return Utils.getNative(starred, false);
    }

    public Date getLastEventDate() {
        return Utils.parseDateTime(last_event_on);
    }

    public String getLastEventDateString() {
        return last_event_on;
    }

    public String getLink() {
        return link;
    }

    public Profile[] getParticipants() {
        return participants != null ? participants.clone() : new Profile[0];
    }

    public Presence getPresenceMetaData() {
        return presence;
    }

    public Push getPushMetaData() {
        return push;
    }

    public String getSubject() {
        return subject;
    }

    public ReferenceType getType() {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(unread_count, -1);
    }

}

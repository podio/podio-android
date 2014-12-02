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

import static com.podio.sdk.internal.Utils.FALSE;
import static com.podio.sdk.internal.Utils.TRUE;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the ConversationDTO API domain object.
 * 
 * @author László Urszuly
 */
public class Conversation implements Parcelable {

    public static final Parcelable.Creator<Conversation> CREATOR = new Parcelable.Creator<Conversation>() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

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

    public static class Data implements Parcelable {

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        private final Long message_id;
        private final File[] files;
        private final String text;
        private final String created_on;

        private Data(Parcel parcel) {
            this.message_id = parcel.readLong();
            this.files = parcel.createTypedArray(File.CREATOR);
            this.text = parcel.readString();
            this.created_on = parcel.readString();
        }

        private Data() {
            this.message_id = null;
            this.files = null;
            this.text = null;
            this.created_on = null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(message_id);
            dest.writeTypedArray(files, flags);
            dest.writeString(text);
            dest.writeString(created_on);
        }

    }

    public static class Event implements Parcelable {

        public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
            public Event createFromParcel(Parcel in) {
                return new Event(in);
            }

            public Event[] newArray(int size) {
                return new Event[size];
            }
        };

        private final Long event_id;
        private final Byline created_by;
        private final String created_on;
        private final String action;
        private final Data data;

        private Event(Parcel parcel) {
            this.event_id = parcel.readLong();
            this.created_by = parcel.readParcelable(ClassLoader.getSystemClassLoader());
            this.created_on = parcel.readString();
            this.action = parcel.readString();
            this.data = parcel.readParcelable(ClassLoader.getSystemClassLoader());
        }

        private Event() {
            this.event_id = null;
            this.created_by = null;
            this.created_on = null;
            this.action = null;
            this.data = null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(event_id);
            dest.writeParcelable(created_by, flags);
            dest.writeString(created_on);
            dest.writeString(action);
            dest.writeParcelable(data, flags);
        }

        public long getEventId() {
            return Utils.getNative(event_id, -1L);
        }

        public Byline getCreatedBy() {
            return created_by;
        }

        public long getCreatedById() {
            return created_by != null ? created_by.getUserId() : -1L;
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

    private final Long conversation_id;
    private final Boolean pinned;
    private final Boolean starred;
    private final Boolean unread;
    private final Integer unread_count;
    private final Byline created_by;
    private final User.Profile[] participants;
    private final String created_on;
    private final String last_event_on;
    private final String link;
    private final String type; // See "Type" enum.
    private final String subject;
    private final String excerpt;
    private final Presence presence;
    private final Push push;

    private Conversation(Parcel parcel) {
        this.conversation_id = parcel.readLong();
        this.pinned = (parcel.readInt() == 1);
        this.starred = (parcel.readInt() == 1);
        this.unread = (parcel.readInt() == 1);
        this.unread_count = parcel.readInt();
        this.created_by = parcel.readParcelable(ClassLoader.getSystemClassLoader());
        this.participants = parcel.createTypedArray(User.Profile.CREATOR);
        this.created_on = parcel.readString();
        this.last_event_on = parcel.readString();
        this.link = parcel.readString();
        this.type = parcel.readString();
        this.subject = parcel.readString();
        this.excerpt = parcel.readString();
        this.presence = parcel.readParcelable(ClassLoader.getSystemClassLoader());
        this.push = parcel.readParcelable(ClassLoader.getSystemClassLoader());
    }

    private Conversation() {
        this.conversation_id = null;
        this.pinned = null;
        this.starred = null;
        this.unread = null;
        this.unread_count = null;
        this.created_by = null;
        this.participants = null;
        this.created_on = null;
        this.last_event_on = null;
        this.link = null;
        this.type = null; // See "Type" enum.
        this.subject = null;
        this.excerpt = null;
        this.presence = null;
        this.push = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(conversation_id);
        dest.writeInt(pinned ? TRUE : FALSE);
        dest.writeInt(starred ? TRUE : FALSE);
        dest.writeInt(unread ? TRUE : FALSE);
        dest.writeInt(unread_count);
        dest.writeParcelable(created_by, flags);
        dest.writeTypedArray(participants, flags);
        dest.writeString(created_on);
        dest.writeString(last_event_on);
        dest.writeString(link);
        dest.writeString(type);
        dest.writeString(subject);
        dest.writeString(excerpt);
        dest.writeParcelable(presence, flags);
        dest.writeParcelable(push, flags);
    }

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

    public User.Profile[] getParticipants() {
        return participants != null ? participants.clone() : new User.Profile[0];
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

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
        }
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(unread_count, -1);
    }

}

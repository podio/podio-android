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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.Utils;

public class Conversation {
    private final Integer unread_count = null;
    private final Presence presence = null;
    private final String excerpt = null;
    private final List<User> participants = null;
    private final User created_by = null;
    private final Push push = null;
    private final String created_on = null;
    private final String last_event_on = null;
    private final Long conversation_id = null;
    private final Boolean starred = null;
    private final Boolean unread = null;
    private final String type = null;
    private final String subject = null;

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public User getCreator() {
        return created_by;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public long getId() {
        return Utils.getNative(conversation_id, -1L);
    }

    public boolean isStarred() {
        return Utils.getNative(starred, false);
    }

    public boolean hasUnreadMessages() {
        return Utils.getNative(unread, false);
    }

    public Date getLastEventDate() {
        return Utils.parseDateTime(last_event_on);
    }

    public String getLastEventDateString() {
        return last_event_on;
    }

    public List<User> getParticipants() {
        return participants != null ?
                new ArrayList<User>(participants) :
                new ArrayList<User>();
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

    public String getType() {
        return type;
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(unread_count, -1);
    }

}
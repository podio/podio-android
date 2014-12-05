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

import com.podio.sdk.internal.Utils;

public class UserStatus {
    private final User user = null;
    private final Profile profile = null;
    private final Integer inbox_new = null;
    private final Integer message_unread_count = null;
    private final String calendar_code = null;
    private final String mailbox = null;
    private final Push push = null;
    private final Presence presence = null;

    // TODO: Add support for these?
    // private final Properties properties = null;
    // private final Referral referral = null;
    // private final Betas betas = null;
    // private final List<String> flags = null;

    public User getUser() {
        return user;
    }

    public Profile getProfile() {
        return profile;
    }

    public int getNewMessagesCount() {
        return Utils.getNative(inbox_new, -1);
    }

    public int getUnreadMessagesCount() {
        return Utils.getNative(message_unread_count, -1);
    }

    public String getCalendarCode() {
        return calendar_code;
    }

    public String getMailboxPrefix() {
        return mailbox;
    }

    public Push getPush() {
        return push;
    }

    public Presence getPresence() {
        return presence;
    }

}

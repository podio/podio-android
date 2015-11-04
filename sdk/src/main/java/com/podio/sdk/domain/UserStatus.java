
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

    public int getUnreadNotificationsCount() {
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

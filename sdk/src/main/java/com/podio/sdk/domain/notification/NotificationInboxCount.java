package com.podio.sdk.domain.notification;

import com.google.gson.annotations.SerializedName;

/**
 * Simple class that enables a client to get hold of the number of unread notifications in the
 * inbox.
 *
 */
public class NotificationInboxCount {

    @SerializedName("new")
    private int newUnreadNotificationsCount = 0;

    public int getCount() {
        return newUnreadNotificationsCount;
    }
}

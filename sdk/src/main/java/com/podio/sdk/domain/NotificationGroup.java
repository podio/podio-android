
package com.podio.sdk.domain;

import com.podio.sdk.domain.notification.Notification;
import com.podio.sdk.domain.notification.NotificationContext;

import java.util.List;

/**
 * A Java representation of the NotificationContextDTO and NotificationContentDTO API domain
 * object.
 *
 */
public class NotificationGroup {

    private final NotificationContext context = null;
    private final List<Notification> notifications = null;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public NotificationContext getNotificationContext() {
        return context;
    }
}

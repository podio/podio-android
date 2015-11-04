
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Application;

/**
 * This class is used when the notification context object is of type "app".
 *
 */
public class AppNotificationContext extends NotificationContext {

    private final Application data = null;

    public Application getApp() {
        return data;
    }
}

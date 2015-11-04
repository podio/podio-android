
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Grant;

/**
 * This class is used when the notification is of type "grant_create".
 *
 * @author Tobias Lindberg
 */
public class GrantNotification extends Notification {

    private final Grant data = null;

    public Grant getGrant() {
        return data;
    }
}

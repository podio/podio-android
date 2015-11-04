
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Rating;

/**
 * This class is used when the notification is of type "rating".
 *
 */
public class RatingNotification extends Notification {

    private final Rating data = null;

    public Rating getRating() {
        return data;
    }
}

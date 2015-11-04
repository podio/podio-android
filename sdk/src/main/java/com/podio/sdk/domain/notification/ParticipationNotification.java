
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.ItemParticipation;

/**
 * This class is used when the notification was of type "participation".
 *
 * @author Tobias Lindberg
 */
public class ParticipationNotification extends Notification {

    private final ItemParticipation data = null;

    public ItemParticipation getItemParticipation() {
        return data;
    }
}


package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Vote;

/**
 * This class is used when the notification is of type "vote".
 *
 * @author Tobias Lindberg
 */
public class VoteNotification extends Notification {

    private final Vote data = null;

    public Vote getVote() {
        return data;
    }
}

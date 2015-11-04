
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Comment;

/**
 * This class is used when the notification is of type "comment".
 *
 */
public class CommentNotification extends Notification {

    private final Comment data = null;

    public Comment getComment() {
        return data;
    }
}

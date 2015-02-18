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

package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Byline;
import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
 * This class is the base class for all kinds of notifications.
 *
 * @author Tobias Lindberg
 */
public abstract class Notification {
    /**
     * The field type enumeration. Each notification can have exactly one of these type values.
     */
    public static enum NotificationType {
        comment(CommentNotification.class),
        rating(RatingNotification.class),
        participation(ParticipationNotification.class),
        alert(UnknownNotification.class),
        team_alert(UnknownNotification.class),
        creation(UnknownNotification.class),
        update(UnknownNotification.class),
        delete(UnknownNotification.class),
        message(UnknownNotification.class),
        space_invite(UnknownNotification.class),
        space_delete(UnknownNotification.class),
        bulletin(UnknownNotification.class),
        member_reference_add(UnknownNotification.class),
        member_reference_remove(UnknownNotification.class),
        file(UnknownNotification.class),
        role_change(UnknownNotification.class),
        conversation_add(UnknownNotification.class),
        answer(UnknownNotification.class),
        self_kicked_from_space(UnknownNotification.class),
        space_create(UnknownNotification.class),
        meeting_participant_add(UnknownNotification.class),
        meeting_participant_remove(UnknownNotification.class),
        reminder(UnknownNotification.class),
        batch_process(UnknownNotification.class),
        batch_complete(UnknownNotification.class),
        space_member_request(UnknownNotification.class),
        grant_create(UnknownNotification.class),
        grant_delete(UnknownNotification.class),
        grant_create_other(UnknownNotification.class),
        grant_delete_other(UnknownNotification.class),
        reference(UnknownNotification.class),
        like(UnknownNotification.class),
        vote(UnknownNotification.class),
        item_transaction_confirmed(UnknownNotification.class),
        file_delete(UnknownNotification.class),
        unknown(UnknownNotification.class); // Custom value to handle errors.

        private final Class<? extends Notification> notificationClass;

        private NotificationType(Class<? extends Notification> notificationClass) {
            this.notificationClass = notificationClass;
        }

        public Class<? extends Notification> getNotificationClass() {
            return notificationClass;
        }
    }

    private final Long notification_id = null;
    private final String type = null;
    private final Byline created_by = null;
    private final String created_on = null;
    private final String text = null;
    private final String text_short = null;

    public long getNotificationId() {
        return Utils.getNative(notification_id, -1L);
    }

    public NotificationType getType() {
        try {
            return NotificationType.valueOf(type);
        } catch (NullPointerException e) {
            return NotificationType.unknown;
        } catch (IllegalArgumentException e) {
            return NotificationType.unknown;
        }
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public String getCreatedString() {
        return created_on;
    }

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getText() {
        return text;
    }

    public String getTextShort() {
        return text_short;
    }
}

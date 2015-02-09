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
    public static enum Type {
        comment(CommentNotification.class),
        rating(RatingNotification.class),
        participation(ParticipationNotification.class),
        alert(UndefinedNotification.class),
        team_alert(UndefinedNotification.class),
        creation(UndefinedNotification.class),
        update(UndefinedNotification.class),
        delete(UndefinedNotification.class),
        message(UndefinedNotification.class),
        space_invite(UndefinedNotification.class),
        space_delete(UndefinedNotification.class),
        bulletin(UndefinedNotification.class),
        member_reference_add(UndefinedNotification.class),
        member_reference_remove(UndefinedNotification.class),
        file(UndefinedNotification.class),
        role_change(UndefinedNotification.class),
        conversation_add(UndefinedNotification.class),
        answer(UndefinedNotification.class),
        self_kicked_from_space(UndefinedNotification.class),
        space_create(UndefinedNotification.class),
        meeting_participant_add(UndefinedNotification.class),
        meeting_participant_remove(UndefinedNotification.class),
        reminder(UndefinedNotification.class),
        batch_process(UndefinedNotification.class),
        batch_complete(UndefinedNotification.class),
        space_member_request(UndefinedNotification.class),
        grant_create(UndefinedNotification.class),
        grant_delete(UndefinedNotification.class),
        grant_create_other(UndefinedNotification.class),
        grant_delete_other(UndefinedNotification.class),
        reference(UndefinedNotification.class),
        like(UndefinedNotification.class),
        vote(UndefinedNotification.class),
        item_transaction_confirmed(UndefinedNotification.class),
        file_delete(UndefinedNotification.class),
        undefined(UndefinedNotification.class);

        private final Class<? extends Notification> notificationClass;

        private Type(Class<? extends Notification> notificationClass) {
            this.notificationClass = notificationClass;
        }

        public Class<? extends Notification> getNotificationClass() {
            return notificationClass;
        }
    }

    private final Long notification_id = null;
    private final Type type = null;
    private final Byline created_by = null;
    private final String created_on = null;
    private final String text = null;
    private final String text_short = null;

    public long getNotificationId() {
        return Utils.getNative(notification_id, -1L);
    }

    public Type getType() {
        return type;
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


package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Byline;
import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
 * This class is the base class for all kinds of notifications.
 * <p/>
 * In most cases all information we are interested in is provided by this class so even if you are
 * getting notifications of type {@link UnknownNotification} there is still plenty of information
 * available in that one.
 *
 */
public abstract class Notification {
    /**
     * The field type enumeration. Each notification can have exactly one of these type values.
     */
    public static enum NotificationType {
        comment,
        rating,
        participation,
        alert,
        team_alert,
        creation,
        update,
        delete,
        message,
        space_invite,
        space_delete,
        bulletin,
        member_reference_add,
        member_reference_remove,
        file,
        role_change,
        conversation_add,
        answer,
        self_kicked_from_space,
        space_create,
        meeting_participant_add,
        meeting_participant_remove,
        reminder,
        batch_process,
        batch_complete,
        space_member_request,
        grant_create,
        grant_delete,
        grant_create_other,
        grant_delete_other,
        reference,
        like,
        vote,
        item_transaction_confirmed,
        file_delete,
        unknown; // Custom value to handle errors.

        public static NotificationType getType(String type) {
            try {
                return NotificationType.valueOf(type);
            } catch (NullPointerException e) {
                return NotificationType.unknown;
            } catch (IllegalArgumentException e) {
                return NotificationType.unknown;
            }
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
        return NotificationType.getType(type);
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public String getCreatedString() {
        return created_on;
    }

    public Date getCreatedDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getText() {
        return text;
    }

    public String getTextShort() {
        return text_short;
    }
}

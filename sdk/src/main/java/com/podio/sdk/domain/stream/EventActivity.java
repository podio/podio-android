
package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Byline;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.internal.Utils;

import java.util.Date;

/**
 * This class is the base class for all activities.
 * <p/>
 * In most cases all information we are interested in is provided by this class so even if you are
 * getting activities of type {@link UnknownEventActivity} there is still plenty of information
 * available in that one.
 *
 * @author Tobias Lindberg
 */
public abstract class EventActivity {

    public static enum EventType {
        comment,
        file,
        rating,
        creation,
        update,
        task,
        answer,
        rsvp,
        grant,
        reference,
        like,
        vote,
        participation,
        file_delete,
        unknown; // Custom value to handle errors.

        public static EventType getActivityType(String activity_type) {
            try {
                return EventType.valueOf(activity_type);
            } catch (NullPointerException e) {
                return EventType.unknown;
            } catch (IllegalArgumentException e) {
                return EventType.unknown;
            }
        }
    }

    private final String type = null;
    private final String activity_type = null;
    private final Byline created_by = null;
    private final String created_on = null;

    public Date getCreatedOnDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getCreatedOnString() {
        return created_on;
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public ReferenceType getType() {
        return ReferenceType.getType(type);
    }

    public EventType getActivityType() {
        return EventType.getActivityType(activity_type);
    }
}

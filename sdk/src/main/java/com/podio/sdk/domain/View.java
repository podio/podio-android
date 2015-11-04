
package com.podio.sdk.domain;

import java.util.Date;
import java.util.Set;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;

/**
 * Domain object representing saved app views that you can use to filter your
 * app item lists.
 *
 * @author Tobias Lindberg
 */
public class View {

    public static enum Type {
        standard, saved, undefined
    }

    private final Long view_id = null;
    private final String name = null;
    private final String created_on = null;
    private final User created_by = null;
    private final Set<Right> rights = null;
    @SerializedName("private")
    private final Boolean is_private = null;
    private final Integer items = null;
    private final Type type = null;

    private View() {
    }

    public long getId() {
        return Utils.getNative(view_id, -1L);
    }

    public String getName() {
        return name;
    }

    /**
     * Gets the end date of the calendar event as a Java Date object.
     *
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public User getCreatedByUser() {
        return created_by;
    }

    /**
     * Checks whether the list of rights the user has for this application
     * contains <em>all</em> the given permissions.
     *
     * @param permissions
     *        The list of permissions to check for.
     * @return Boolean true if all given permissions are found or no permissions
     *         are given. Boolean false otherwise.
     */
    public boolean hasRights(Right... permissions) {
        if (rights != null) {
            for (Right permission : permissions) {
                if (!rights.contains(permission)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isPrivate() {
        return Utils.getNative(is_private, false);
    }

    public int getItemsCount() {
        return Utils.getNative(items, -1);
    }

    public Type getType() {
        return type != null ? type : Type.undefined;
    }
}

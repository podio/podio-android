
package com.podio.sdk.domain.notification;

import com.podio.sdk.domain.Organization;
import com.podio.sdk.domain.Reference;
import com.podio.sdk.domain.Right;
import com.podio.sdk.domain.Space;
import com.podio.sdk.internal.Utils;

import java.util.List;

/**
 * This class is the base class of all notification contexts.
 * <p/>
 * In most cases all information we are interested in is provided by this class so even if you are
 * getting notification contexes of type {@link UnknownNotificationContext} there is still plenty of
 * information available in that one.
 *
 * @author Tobias Lindberg
 */
public abstract class NotificationContext {

    private final Reference ref = null;
    private final String title = null;
    private final List<Right> rights = null;
    private final Integer comment_count = null;
    private final Space space = null;
    private final Organization org = null;

    public Organization getOrganization() {
        return org;
    }

    public Space getSpace() {
        return space;
    }

    public int getCommentCount() {
        return Utils.getNative(comment_count, -1);
    }

    /**
     * Checks whether the list of rights the user has for this notification context contains
     * <em>all</em> the given permissions.
     *
     * @param permissions
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are found or no permissions are given. Boolean
     * false otherwise.
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

    public String getTitle() {
        return title;
    }

    public Reference getReference() {
        return ref;
    }
}

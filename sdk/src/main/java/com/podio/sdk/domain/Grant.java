
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.util.Date;
import java.util.List;

/**
 * A Java representation of the GrantDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Grant {
    private final Application app = null;
    private final Byline created_by = null;
    private final List<String> rights = null;
    private final Long grant_id = null;
    private final Space space = null;
    private final String action = null;
    private final String created_on = null;
    private final String message = null;
    private final Profile user = null;

    // These attributes are defined in the API source code,
    // but not supported by the SDK right now.
    //private final Object access_level = null;
    //private final Object ref = null;

    public String getAction() {
        return action;
    }

    public Application getApp() {
        return app;
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public Date getCreatedDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public long getGrantId() {
        return Utils.getNative(grant_id, -1L);
    }

    public String getMessage() {
        return message;
    }

    public Space getSpace() {
        return space;
    }

    public Profile getUser() {
        return user;
    }

    /**
     * Checks whether the list of rights the user has for this grant contains <em>all</em> the given
     * permissions.
     *
     * @param rights
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are granted for this Grant. Boolean false
     * otherwise.
     */
    public boolean hasAllRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (!this.rights.contains(right.name())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this grant contains <em>any</em> of the
     * given permissions.
     *
     * @param rights
     *         The list of permissions to check any single one for.
     *
     * @return Boolean true if any given permission is granted for this Grant. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (this.rights.contains(right.name())) {
                    return true;
                }
            }
        }

        return false;
    }

}

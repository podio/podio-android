
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Status;

import java.util.List;

/**
 * Enables access to the Status API end point.
 *
 * @author rabie
 */
public class StatusProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("status");
        }

        Path withSpace() {
            addPathSegment("space");
            return this;
        }

        Path withId(long id) {
            addPathSegment(Long.toString(id));
            return this;
        }

        Path withAlertInvite(boolean alertInvite) {
            addQueryParameter("alert_invite", alertInvite ? "true" : "false");
            return this;
        }

    }

    /**
     * @param spaceId
     * @param alertInvite
     *         true if any mentioned user should be automatically invited to the workspace if the
     *         user does not have access to the object and access cannot be granted to the object.
     * @param value
     * @param fileIds
     *
     * @return
     */
    public Request<Status> addStatusMessage(long spaceId, boolean alertInvite, String value, List<Long> fileIds) {
        Path path = new Path().withSpace().withId(spaceId).withAlertInvite(alertInvite);
        return post(path, new Status.PushData(value, fileIds), Status.class);
    }

}

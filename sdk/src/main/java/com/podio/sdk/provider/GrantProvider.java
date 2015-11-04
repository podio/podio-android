package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.ReferenceType;

/**
 * Enables access to the Grants API end point.
 *
 */
public class GrantProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("grant");
        }

        public Path withReference(ReferenceType type, long id) {
            this.addPathSegment(type.name());
            this.addPathSegment(Long.toString(id, 10));

            return this;
        }

        public Path withUserId(long id) {
            addPathSegment(Long.toString(id, 10));
            return this;
        }

    }

    /**
     * Removes the grant from the given user on the given object
     *
     * @param type
     *         reference type of the object
     * @param referenceId
     *         id of the object
     * @param userId
     *         id of the user
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> removeGrant(ReferenceType type, long referenceId, long userId) {
        Path path = new Path().withReference(type, referenceId).withUserId(userId);
        return delete(path);
    }
}

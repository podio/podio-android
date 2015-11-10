package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Space;

/**
 * Enables access to the Status API end point.
 */
public class SpacesProvider extends Provider {

    protected static class Path extends Filter {

        public Path() {
            super("space");
        }

        public Path withId(long spaceId) {
            addPathSegment(Long.toString(spaceId));
            return this;
        }
    }

    /**
     * Fetches a space domain object based on the given space id
     *
     * @param spaceId
     *         ID of the space
     *
     * @return
     */
    public Request<Space> getSpace(long spaceId) {
        return get(new Path().withId(spaceId), Space.class);
    }
}

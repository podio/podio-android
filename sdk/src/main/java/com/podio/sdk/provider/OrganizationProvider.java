
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Organization;

/**
 * Enables access to the organization API end point.
 *
 */
public class OrganizationProvider extends Provider {

    static class Path extends Filter {

        Path() {
            super("org");
        }

    }

    /**
     * Fetches all organizations - including a minimal set of information on the contained
     * workspaces - that are available to the user.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Organization[]> getAll() {
        Path filter = new Path();

        return get(filter, Organization[].class);
    }

}


package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.View;

/**
 * Enables access to the view API end point.
 *
 */
public class ViewProvider extends Provider {

    static class Path extends Filter {

        Path() {
            super("view");
        }

        Path withApplicationId(long applicationId) {
            addPathSegment("app");
            addPathSegment(Long.toString(applicationId, 10));

            return this;
        }

    }

    /**
     * Fetches views for a given application that can be used to filter items for that application.
     *
     * @param applicationId
     *         The id of the parent application.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<View[]> getAllViews(long applicationId) {
        Path filter = new Path().withApplicationId(applicationId);
        return get(filter, View[].class);
    }

}

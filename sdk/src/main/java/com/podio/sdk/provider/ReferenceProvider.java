
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.DataReference;
import com.podio.sdk.domain.reference.ReferenceGroup;

/**
 * Enables access to the Reference API end point.
 *
 * @author Tobias Lindberg
 */
public class ReferenceProvider extends Provider {

    static class Path extends Filter {

        protected Path() {
            super("reference");
        }

        Path withSearch() {
            addPathSegment("search");
            return this;
        }

        Path withResolve() {
            addPathSegment("resolve");
            return this;
        }

        Path withUrl(String url) {
            addQueryParameter("url", url);
            return this;
        }
    }

    /**
     * Performs a reference search based on the @referenceTarget.
     *
     * @param referenceTarget
     *         The reference search target.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<ReferenceGroup[]> referenceSearch(ReferenceGroup.ReferenceTarget referenceTarget) {
        Path filter = new Path().withSearch();
        return post(filter, referenceTarget, ReferenceGroup[].class);
    }

    public Request<DataReference> resolveURL(String url) {
        Path filter = new Path().withResolve().withUrl(url);
        return get(filter, DataReference.class);
    }

}

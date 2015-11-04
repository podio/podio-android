
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Address;

/**
 * Enables access to the location API end point.
 *
 * @author Tobias Lindberg
 */
public class LocationProvider extends Provider {

    static class LocationFilter extends Filter {

        protected LocationFilter() {
            super("location");
        }

        public LocationFilter withAddress(String address) {
            this.addPathSegment("lookup");
            this.addQueryParameter("address", address);

            return this;
        }
    }

    /**
     * Lookup the address to get location information of the query longitude, latitude, city, post code,
     * etc.
     *
     * @param address
     *         The address search query to lookup.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Address[]> lookupAddress(String address) {
        LocationFilter filter = new LocationFilter();
        filter.withAddress(address);

        return get(filter, Address[].class);
    }
}

package com.podio.sdk.provider;

import com.podio.sdk.Filter;

public class OrganizationProvider extends PodioProvider {

    public Object getAll() {
        Filter filter = new OrganizationFilter();
        return fetchRequest(filter);
    }

}

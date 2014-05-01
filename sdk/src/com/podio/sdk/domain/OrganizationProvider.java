package com.podio.sdk.domain;

import com.podio.sdk.Filter;

public class OrganizationProvider extends PodioProvider<Organization> {

    public Object getAll() {
        Filter filter = new OrganizationFilter();
        return fetchItems(filter);
    }

}

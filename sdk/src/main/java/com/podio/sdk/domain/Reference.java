
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Reference {
    private final String title = null;
    private final String type = null;
    private final Long id = null;

    public ReferenceType getType() {
        return ReferenceType.getType(type);
    }

    /**
     * @return returns the id of the reference or -1 if for some reason there is no id
     */
    public long getId() {
        return Utils.getNative(id, -1);
    }

    public String getTitle() {
        return title;
    }
}


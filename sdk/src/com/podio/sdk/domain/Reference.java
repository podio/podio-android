package com.podio.sdk.domain;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;

public class Reference {

    private final String type = null;
    @SerializedName("type_name")
    private final String typeName = null;
    private final Long id = null;
    private final String title = null;
    private final Data data = null;

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * @return returns the id of the reference or -1 if for some reason there is
     *         no id
     */
    public Long getId() {
        return Utils.getNative(id, -1);
    }

    public String getTitle() {
        return title;
    }

    public Data getData() {
        return data;
    }

}


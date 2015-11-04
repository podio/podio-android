
package com.podio.sdk.domain;

import com.podio.sdk.domain.data.Data;
import com.podio.sdk.internal.Utils;
import com.podio.sdk.json.DataReferenceDeserializer;

/**
 * Same as {@link Reference} but with the added data attribute. Ideally we shouldn't have this
 * duplicate class definition but since the URL resolver API call needs the data object for certain
 * cases and we risk breaking the app for new unsupported types of references we have this separate
 * class for our custom deserializer {@link DataReferenceDeserializer} to handle.
 *
 * This is of course not the ideal solution
 *
 */
public class DataReference {
    private final String title;
    private final Data data;
    private final String type;
    private final Long id;

    public DataReference(String title, Data data, String type, long id) {
        this.title = title;
        this.data = data;
        this.type = type;
        this.id = id;
    }

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

    /**
     * @return Returns the data object for this reference if the reference type is supported (see
     * {@link DataReferenceDeserializer}, otherwise {@link com.podio.sdk.domain.data.UnknownData}
     */
    public Data getData() {
        return data;
    }
}

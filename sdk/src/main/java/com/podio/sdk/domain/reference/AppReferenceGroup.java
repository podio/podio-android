
package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.Item;

import java.util.List;

/**
 * When the reference search group name is "app" you will get this object.
 *
 * @author Tobias Lindberg
 */
public class AppReferenceGroup extends ReferenceGroup {
    private final Application data = null;
    private final List<Item> contents = null;

    public Application getData() {
        return data;
    }

    public List<Item> getContents() {
        return contents;
    }
}

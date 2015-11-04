
package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Space;

import java.util.List;

/**
 * When the reference search group name is "spaces" you will get this object.
 *
 */
public class SpacesReferenceGroup extends ReferenceGroup{
    private final List<Space> contents = null;

    public List<Space> getContents() {
        return contents;
    }
}


package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Application;

import java.util.List;

/**
 * When the reference search group name is "apps" you will get this object.
 *
 * @author Tobias Lindberg
 */
public class AppsReferenceGroup extends ReferenceGroup {
    private final List<Application> contents = null;

    public List<Application> getContents() {
        return contents;
    }
}

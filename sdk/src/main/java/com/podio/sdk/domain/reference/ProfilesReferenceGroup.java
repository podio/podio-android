
package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Profile;

import java.util.List;

/**
 * When the reference search group name is "profiles" you will get this object.
 *
 * @author Tobias Lindberg
 */
public class ProfilesReferenceGroup extends ReferenceGroup {
    private final List<Profile> contents = null;

    public List<Profile> getContents() {
        return contents;
    }
}

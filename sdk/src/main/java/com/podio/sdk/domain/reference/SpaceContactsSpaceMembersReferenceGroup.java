
package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Profile;
import com.podio.sdk.domain.Space;

import java.util.List;

/**
 * When the reference search group name is "space_contacts" or "space_members" you will get this
 * object.
 *
 * @author Tobias Lindberg
 */
public class SpaceContactsSpaceMembersReferenceGroup extends ReferenceGroup {
    private final Space data = null;
    private final List<Profile> contents = null;

    public Space getData() {
        return data;
    }

    public List<Profile> getContents() {
        return contents;
    }
}

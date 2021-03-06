
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.util.Date;
import java.util.List;

public class Profile {
    private final Long user_id = null;
    private final Long profile_id;
    private final Long org_id = null;
    private final Long space_id = null;
    private final String external_id = null;
    private final String last_seen_on = null;
    private final String type = null;
    private final String link = null;
    private final List<String> rights = null;
    private final Push push = null;
    private final File image = null;

    // Not found in the API ProfileDTO
    private final String name = null;
    private final String about = null;
    private final String[] title = null;
    private final String[] location = null;
    private final String[] phone = null;
    private final String[] mail = null;
    private final String[] address = null;         // -> ContactAPI, not in ProfileDTO
    private final String zip = null;               // -> ContactAPI, not in ProfileDTO
    private final String city = null;              // -> ContactAPI, not in ProfileDTO
    private final String country = null;           // -> ContactAPI, not in ProfileDTO

    /**
     * Use this constructor to create a profile object that you want to upload a workspace contact
     * or member to the API in e.g. the create item API call (if it contains a contact field)
     *
     * @param profileId
     */
    public Profile(long profileId) {
        this.profile_id = profileId;
    }

    public String getAbout() {
        return about;
    }

    public ReferenceType getType() {
        return ReferenceType.getType(type);
    }

    public String[] getEmailAddresses() {
        return mail != null ? mail.clone() : new String[0];
    }

    public String getExternalId() {
        return external_id;
    }

    public long getId() {
        return Utils.getNative(profile_id, -1L);
    }

    public File getImage() {
        return image;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTimeUtc(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    public String getLink() {
        return link;
    }

    public String[] getLocations() {
        return location != null ? location.clone() : new String[0];
    }

    public String getName() {
        return name;
    }

    public long getOrganizationId() {
        return Utils.getNative(org_id, -1L);
    }

    public String[] getPhoneNumbers() {
        return phone != null ? phone.clone() : new String[0];
    }

    public Push getPushTokens() {
        return push;
    }

    public String getThumbnailUrl() {
        return image != null ? image.getThumbnailLink() : null;
    }

    public String[] getTitles() {
        return title != null ? title.clone() : new String[0];
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getWorkspaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public String[] getAddressLines() {
        return address.clone();
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    /**
     * Checks whether the list of rights the user has for this domain object contains the given
     * permission.
     *
     * @param permission
     *         The permission to verify.
     *
     * @return True if the given permission is granted, false otherwise.
     */
    public boolean hasRight(Right permission) {
        return rights != null && rights.contains(permission.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return profile_id.equals(profile.profile_id);

    }

    @Override
    public int hashCode() {
        return profile_id.hashCode();
    }
}

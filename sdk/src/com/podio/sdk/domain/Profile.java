/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */
package com.podio.sdk.domain;

import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.Utils;

public class Profile {
    private final Long user_id = null;
    private final Long profile_id = null;
    private final Long org_id = null;
    private final Long space_id = null;
    private final String external_id = null;
    private final String last_seen_on = null;
    private final String type = null; // See the "Type" enum.
    private final String link = null;
    private final List<String> rights = null; // See the "Right" enum.
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile)) {
            return false;
        }

        Profile other = (Profile) o;
        long pid1 = Utils.getNative(profile_id, -1);
        long uid1 = Utils.getNative(user_id, -1);
        long pid2 = other.getId();
        long uid2 = other.getUserId();

        return (pid1 == pid2 && pid1 != -1) || (uid1 == uid2 && uid1 != -1);
    }

    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;

        result = prime * result + (user_id != null ? user_id.hashCode() : 0);
        result = prime * result + (profile_id != null ? profile_id.hashCode() : 0);

        return result;
    }

    public String getAbout() {
        return about;
    }

    public String getType() {
        return type;
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
        return Utils.parseDateTime(last_seen_on);
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
     * Checks whether the list of rights the user has for this domain object
     * contains the given permission.
     * 
     * @param permission
     *        The permission to verify.
     * @return True if the given permission is granted, false otherwise.
     */
    public boolean hasRight(Right permission) {
        return rights != null && rights.contains(permission.name());
    }

}

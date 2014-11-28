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

public class Contact {
    public static enum Type {
        user, unknown
    }

    private final Long profile_id = null;
    private final Long user_id = null;
    private final Long space_id = null;
    private final Long org_id = null;
    private final Long avatar = null;
    private final String external_id = null;
    private final String type = null;
    private final String name = null;
    private final String[] title = null;
    private final String[] address = null;
    private final String zip = null;
    private final String city = null;
    private final String country = null;
    private final File image = null;
    private final String[] mail = null;
    private final String[] phone = null;
    private final String link = null;
    private final String last_seen_on = null;
    private final List<String> rights = null;

    public long getProfileId() {
        return Utils.getNative(profile_id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getSpaceId() {
        return Utils.getNative(space_id, -1L);
    }

    public long getOrganizationId() {
        return Utils.getNative(org_id, -1L);
    }

    public long getAvatarId() {
        return Utils.getNative(avatar, -1L);
    }

    public String getExternalId() {
        return external_id;
    }

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
        }
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return Utils.notEmpty(title) ? Utils.join(title, ", ") : null;
    }

    public String getAddress() {
        return Utils.notEmpty(address) ? Utils.join(address, ", ") : null;
    }

    public String[] getAddressLines() {
        return address;
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

    public File getImage() {
        return image;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public String[] getEmailAddresses() {
        return mail;
    }

    public String[] getPhoneNumbers() {
        return phone;
    }

    public String getPodioLink() {
        return link;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTime(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    public boolean hasRights(String... permissions) {
        if (rights != null) {
            for (String permission : permissions) {
                if (!rights.contains(permission)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

}

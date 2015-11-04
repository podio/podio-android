
package com.podio.sdk.domain;

import java.util.Date;

import com.podio.sdk.internal.Utils;

public class Byline {
    private final Long id = null;
    private final Long user_id = null;
    private final Long avatar = null;
    private final Long avatar_id = null;
    private final String avatar_type = null; // See "Type" enum.
    private final String name = null;
    private final String last_seen_on = null;
    private final String type = null; // See "Type" enum.
    private final String url = null;
    private final File image = null;

    public long getId() {
        return Utils.getNative(id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public long getAvatar() {
        return Utils.getNative(avatar, -1L);
    }

    public long getAvatarId() {
        return Utils.getNative(avatar_id, -1L);
    }

    public ReferenceType getAvatarType() {
        try {
            return ReferenceType.valueOf(avatar_type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public String getName() {
        return name;
    }

    public Date getLastSeenDate() {
        return Utils.parseDateTimeUtc(last_seen_on);
    }

    public String getLastSeenDateString() {
        return last_seen_on;
    }

    public ReferenceType getType() {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return image != null ? image.getLink() : null;
    }

    public File getImage() {
        return image;
    }

}

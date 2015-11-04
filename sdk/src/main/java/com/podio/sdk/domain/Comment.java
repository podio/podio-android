
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 */
public class Comment {
    private final Boolean is_liked = null;
    private final Byline created_by = null;
    private final Embed embed = null;
    private final File embed_file = null;
    private final File[] files = null;
    private final Integer like_count = null;
    private final List<String> rights = null;
    private final Long comment_id = null;
    private final String value = null;
    private final String rich_value = null;
    private final String created_on = null;

    // These attributes are defined in the API source code,
    // but not supported by the SDK right now.
    //private final Object external_id = null;
    //private final Object ref = null;
    //private final Object last_edit_on = null;
    //private final Object questions = null;
    //private final Byline user = null;
    //private final Object created_via = null;
    //private final Object granted_users = null;
    //private final Object invited_users = null;

    public static class Create {
        @SuppressWarnings("unused")
        private final String value;

        @SuppressWarnings("unused")
        private final long[] file_ids;

        public Create(String value, long[] fileIds) {
            this.value = value;
            this.file_ids = Utils.notEmpty(fileIds) ? fileIds : new long[0];
        }
    }

    public long getCommentId() {
        return Utils.getNative(comment_id, -1L);
    }

    public Byline getCreatedBy() {
        return created_by;
    }

    public Date getCreatedOnDate() {
        return Utils.parseDateTimeUtc(created_on);
    }

    public String getCreatedOnString() {
        return created_on;
    }

    public Embed getEmbed() {
        return embed;
    }

    public File getEmbedFile() {
        return embed_file;
    }

    public List<File> getFiles() {
        return Utils.notEmpty(files) ? Arrays.asList(files) : Arrays.asList(new File[0]);
    }

    public int getLikeCount() {
        return Utils.getNative(like_count, 0);
    }

    public String getRichValue() {
        return rich_value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Checks whether the list of rights the user has for this Space contains <em>all</em> the given
     * permissions.
     *
     * @param rights
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are granted for this Space. Boolean false
     * otherwise.
     */
    public boolean hasAllRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (!this.rights.contains(right.name())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this Space contains <em>any</em> of the
     * given permissions.
     *
     * @param rights
     *         The list of permissions to check any single one for.
     *
     * @return Boolean true if any given permission is granted for this Space. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (this.rights.contains(right.name())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isLiked() {
        return Utils.getNative(is_liked, false);
    }
}


package com.podio.sdk.domain;

import com.podio.sdk.domain.data.Data;
import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Java representation of the StatusDTO API domain object.
 *
 */
public class Status implements Data{

    /**
     * A class representing the new status message the client wants to create
     */
    public static class PushData {
        private final String value;
        private final List<Long> file_ids;

        public PushData(String value, List<Long> fileIds){
            this.value = value;
            this.file_ids = new ArrayList<>(fileIds);
        }

        public String getValue() {
            return value;
        }

        public List<Long> getFileIds() {
            return file_ids;
        }
    }

    private final Long status_id = null;
    private final String created_on = null;
    private final String value = null;
    private final String rich_value = null;
    private final List<File> files = null;
    private final List<Right> rights = null;
    private final Byline created_by = null;
    private final Integer like_count = null;
    private Boolean is_liked = null;
    private Embed embed = null;
    private File embed_file = null;

    public String getRichValue() {
        return rich_value;
    }

    public String getValue() {
        return value;
    }

    public List<File> getFiles() {
        return files;
    }

    /**
     * Checks whether the list of rights the user has for this comment contains <em>all</em> the
     * given permissions.
     *
     * @param permissions
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are found or no permissions are given. Boolean
     * false otherwise.
     */
    public boolean hasRights(Right... permissions) {
        if (rights != null) {
            for (Right permission : permissions) {
                if (!rights.contains(permission)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public long getStatusId() {
        return Utils.getNative(status_id, -1L);
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

    public Integer getLikeCount() {
        return Utils.getNative(like_count, 0);
    }

    public Boolean isLiked() {
        return is_liked;
    }

    /**
     * Note that embed file contains a subset of FileDTO attributes so many of these attributes may
     * be null.
     *
     * @return
     */
    public File getEmbedFile() {
        return embed_file;
    }

    public Embed getEmbed() {
        return embed;
    }
}

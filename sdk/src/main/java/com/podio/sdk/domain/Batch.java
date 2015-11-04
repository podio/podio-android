
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the BatchDTO API domain object.
 *
 */
public class Batch {

    public static enum PluginTypes {
        app_import,
        app_export,
        connection_load,
        unknown // Custom value to handle errors.
    }

    private final String plugin = null;

    private final Integer completed = null;

    private final Integer skipped = null;

    private final Long batch_id = null;

    public long getBatchId() {
        return Utils.getNative(batch_id, -1L);
    }

    public PluginTypes getPluginType() {
        try {
            return PluginTypes.valueOf(plugin);
        } catch (NullPointerException e) {
            return PluginTypes.unknown;
        } catch (IllegalArgumentException e) {
            return PluginTypes.unknown;
        }
    }

    public int getSkipped() {
        return Utils.getNative(skipped, 0);
    }

    public int getCompleted() {
        return Utils.getNative(completed, 0);
    }

    // TODO Add missing attributes
}

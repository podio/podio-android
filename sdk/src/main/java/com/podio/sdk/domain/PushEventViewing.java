
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * This class describes a push event sent by the API when someone is viewing a target object.
 *
 * @author László Urszuly
 */
public class PushEventViewing extends PushEvent {

    private final Long[] data = null;

    public long get(int index) throws IndexOutOfBoundsException {
        return Utils.getNative(data[index], -1);
    }

    public int size() {
        return Utils.notEmpty(data) ? data.length : 0;
    }

}

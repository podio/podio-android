
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * This class describes a push event sent by the API when someone is typing text on a target
 * object.
 *
 */
public class PushEventTyping extends PushEvent {

    private final Long[] data = null;

    public long get(int index) throws IndexOutOfBoundsException {
        return Utils.getNative(data[index], -1);
    }

    public int size() {
        return Utils.notEmpty(data) ? data.length : 0;
    }

}

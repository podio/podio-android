
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Push {

    private final Long timestamp = null;
    private final Integer expires_in = null;
    private final String channel = null;
    private final String signature = null;

    public long getTimestamp() {
        return Utils.getNative(timestamp, -1L);
    }

    public int getExpiresIn() {
        return Utils.getNative(expires_in, -1);
    }

    public String getChannel() {
        return channel;
    }

    public String getSignature() {
        return signature;
    }

}

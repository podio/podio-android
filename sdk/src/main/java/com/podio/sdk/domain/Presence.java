
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Presence {
    private final Long ref_id = null;
    private final Long user_id = null;
    private final String ref_type = null;
    private final String signature = null;

    public ReferenceType getRefType() {
        try {
            return ReferenceType.valueOf(ref_type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }

    public long getRefId() {
        return Utils.getNative(ref_id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public String getSignature() {
        return signature;
    }

}

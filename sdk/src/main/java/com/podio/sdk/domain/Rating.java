
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the RatingDTO API domain object.
 *
 */
public class Rating {

    public static enum RatingType {
        like,
        approved,
        rsvp,
        fivestar,
        yesno,
        thumbs,
        unknown // Custom value to handle errors.
    }

    public static class Create {
        int value;

        public Create(int value){
            this.value = value;
        }
    }

    private final Long rating_id = null;

    private final String type = null;

    private final Integer value = null;

    public long getRatingId() {
        return Utils.getNative(rating_id, -1L);
    }

    public RatingType getType() {
        try {
            return RatingType.valueOf(type);
        } catch (NullPointerException e) {
            return RatingType.unknown;
        } catch (IllegalArgumentException e) {
            return RatingType.unknown;
        }
    }

    public Integer getValue() {
        return value;
    }
}

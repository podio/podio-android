
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Profile;
import com.podio.sdk.domain.Rating;
import com.podio.sdk.domain.ReferenceType;

/**
 * Enables access to the Ratings API end point.
 */
public class RatingProvider extends Provider {

    static class RatingFilter extends Filter {

        protected RatingFilter() {
            super("rating");
        }

        public RatingFilter withReference(ReferenceType type, long id) {
            this.addPathSegment(type.name());
            this.addPathSegment(Long.toString(id, 10));

            return this;
        }

        public RatingFilter withRatingType(Rating.RatingType ratingType) {
            this.addPathSegment(ratingType.name());

            return this;
        }

        public RatingFilter likedBy() {
            this.addPathSegment("liked_by");
            return this;
        }
    }

    /**
     * Create a like on a reference type with the given id. The request result will deliver a rating
     * object with only rating_id set.
     *
     * @param type
     *         The reference type of what you are liking.
     * @param id
     *         The id of the reference type your are liking.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Rating> createLike(ReferenceType type, long id) {
        RatingFilter filter = new RatingFilter();
        filter.withReference(type, id);
        filter.withRatingType(Rating.RatingType.like);
        Rating.Create create = new Rating.Create(1);

        return post(filter, create, Rating.class);
    }

    /**
     * Create an unlike on a reference type with the given id. The request result will deliver a
     * rating object with only rating_id set.
     *
     * @param type
     *         The reference type of what you are unliking.
     * @param id
     *         The id of the reference type your are unliking.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> createUnlike(ReferenceType type, long id) {
        RatingFilter filter = new RatingFilter();
        filter.withReference(type, id);
        filter.withRatingType(Rating.RatingType.like);

        return delete(filter);
    }

    /**
     * get a list of profiles of everyone that liked the given object.
     *
     * @param type
     *         The reference type of what of the object.
     * @param id
     *         The id of the reference type of the object.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Profile[]> getWhoLikedAnObject(ReferenceType type, long id) {
        RatingFilter filter = new RatingFilter();
        filter.withReference(type, id);
        filter.likedBy();

        return get(filter, Profile[].class);
    }
}

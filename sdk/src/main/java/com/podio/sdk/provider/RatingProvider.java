/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Rating;
import com.podio.sdk.domain.ReferenceType;

/**
 * Enables access to the Ratings API end point.
 *
 * @author Tobias Linderg
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

        public RatingFilter withRatingType(Rating.RatingType ratingType){
            this.addPathSegment(ratingType.name());

            return this;
        }
    }

    /**
     *
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
        filter.withReference(type,id);
        filter.withRatingType(Rating.RatingType.like);
        Rating.Create create = new Rating.Create(1);

        return post(filter, create, Rating.class);
    }

    /**
     *
     * Create an unlike on a reference type with the given id. The request result will deliver a rating
     * object with only rating_id set.
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
        filter.withReference(type,id);
        filter.withRatingType(Rating.RatingType.like);

        return delete(filter);
    }
}

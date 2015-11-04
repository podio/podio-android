
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the VoteDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Vote {

    private final Integer rating = null;

    private final Answer answer = null;

    private final Voting voting = null;

    public Integer getRating() {
        return Utils.getNative(rating, -1);
    }

    public Answer getAnswer(){
        return answer;
    }

    public Voting getVoting() {
        return voting;
    }
}

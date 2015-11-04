
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the VotingDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Voting {

    public static enum VotingKind {
        answer,
        fivestar,
        unknown // Custom value to handle errors.
    }
    private final String kind = null;

    private final String question = null;

    private final Long voting_id = null;

    public long getVotingId() {
        return Utils.getNative(voting_id, -1L);
    }

    public VotingKind getKind() {
        try {
            return VotingKind.valueOf(kind);
        } catch (NullPointerException e) {
            return VotingKind.unknown;
        } catch (IllegalArgumentException e) {
            return VotingKind.unknown;
        }
    }

    public String getQuestion() {
        return question;
    }
}

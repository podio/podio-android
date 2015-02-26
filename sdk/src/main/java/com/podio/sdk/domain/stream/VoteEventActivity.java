package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Vote;

/**
 * This class is used when the activity is of type "vote".
 *
 * @author Tobias Lindberg
 */
public class VoteEventActivity extends EventActivity {

    private final Vote data = null;


    public Vote getVote() {
        return data;
    }

}

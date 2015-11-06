package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Rating;

/**
 * This class is used when the activity is of type "rating".
 *
 */
public class RatingEventActivity extends EventActivity {

    private final Rating data = null;


    public Rating getRating() {
        return data;
    }

}

package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Grant;

/**
 * This class is used when the activity is of type "grant".
 *
 */
public class GrantEventActivity extends EventActivity {

    private final Grant data = null;

    public Grant getGrant() {
        return data;
    }

}

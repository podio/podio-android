package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Item;

/**
 * This class is used when the activity is of type "item".
 *
 * @author Tobias Lindberg
 */
public class ItemEventActivity extends EventActivity {

    private final Item data = null;

    public Item getItem() {
        return data;
    }

}

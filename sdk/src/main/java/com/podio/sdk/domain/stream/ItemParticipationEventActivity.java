package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.ItemParticipation;

/**
 * This class is used when the activity is of type "item_participation".
 *
 */
public class ItemParticipationEventActivity extends EventActivity {

    private final ItemParticipation data = null;

    public ItemParticipation getItemParticipation() {
        return data;
    }

}

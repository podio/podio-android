package com.podio.sdk.domain;

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;

public class Reminder {
    @SerializedName("remind_delta")
    private final Integer reminderDelta = null;

    /**
     * @return returns minutes to remind before the due_date or -1 if no such
     *         value exists
     */
    public Integer getReminderDelta() {
        return Utils.getNative(reminderDelta, -1);
    }
}

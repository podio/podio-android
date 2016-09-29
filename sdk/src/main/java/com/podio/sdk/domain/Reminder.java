package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Reminder {

    private final Integer remind_delta = null;

    /**
     * @return returns minutes to remind before the due_date or -1 if no such value exists
     */
    public int getReminderDelta() {
        return Utils.getNative(remind_delta, -1);
    }
}

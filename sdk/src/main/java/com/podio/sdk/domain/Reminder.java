package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Reminder {
    private final Integer reminder_delta;

    public Reminder(int reminderDelta) {
        this.reminder_delta = reminderDelta;
    }

    public Reminder() {
        reminder_delta = null;
    }

    /**
     * @return returns minutes to remind before the due_date or -1 if no such value exists
     */
    public int getReminderDelta() {
        return Utils.getNative(reminder_delta, -1);
    }
}

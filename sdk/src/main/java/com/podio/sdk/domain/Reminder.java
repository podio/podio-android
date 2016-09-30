package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

public class Reminder {

    public static class CreateData {
        private final int remind_delta;

        public CreateData(int remind_delta) {
            this.remind_delta = remind_delta;
        }
    }
    private final Integer remind_delta = null;

    /**
     * @return returns minutes to remind before the due_date or -1 if no such value exists
     */
    public int getReminderDelta() {
        return Utils.getNative(remind_delta, -1);
    }
}

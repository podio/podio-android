package com.podio.sdk.domain;

import java.util.List;

/**
 * Created by sai on 9/29/16.
 */

public class Recurrence {

    private final String name = null;

    private final Integer step = null;

    private final String until = null;

    private final Config config = null;

    public String getName() {
        return name;
    }

    public Integer getStep() {
        return step;
    }

    public String getUntil() {
        return until;
    }

    public Config getConfig() {
        return config;
    }

    class Config {
        private final List<String> days = null;

        private final Integer repeat_on = null;

        public List<String> getDays() {
            return days;
        }

        public Integer getRepeatOn() {
            return repeat_on;
        }
    }
}

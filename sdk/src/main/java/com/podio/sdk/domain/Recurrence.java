package com.podio.sdk.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 9/29/16.
 */

public class Recurrence {

    public static class CreateData {
        public static final String DAY_OF_WEEK = "day_of_week";
        public static final String DAY_OF_MONTH = "day_of_month";

        private static final String DAYS = "days";
        private static final String REPEAT_ON = "repeat_on";

        private final String name;
        private int step;
        private Map<String, Object> config;
        private String until = null;

        public CreateData(String name) {
            this.name = name;
            config = new HashMap<>();
        }

        public void setStep(int step) {
            this.step = step;
        }

        public void setUntil(String until) {
            this.until = until;
        }

        public void setDays(List<String> days) {
            config.put(DAYS, days);
        }

        public void setRepeatOn(String repeatOn) {
            config.put(REPEAT_ON, repeatOn);
        }
    }

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

    public class Config {
        private final List<String> days = null;

        private final String repeat_on = null;

        public List<String> getDays() {
            return days;
        }

        public String getRepeatOn() {
            return repeat_on;
        }
    }
}

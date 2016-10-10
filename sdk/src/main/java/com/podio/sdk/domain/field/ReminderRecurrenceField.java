package com.podio.sdk.domain.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 9/29/16.
 */

/**
 * Field to display the reminder and recurrence for a date field of type "meeting type"
 */
public class ReminderRecurrenceField extends Field<ReminderRecurrenceField.Value>{

    public static final String NAME = "reminder_recurrence";

    public static class Value extends Field.Value {
        private static final String REMIND_DELTA = "remind_delta";
        private static final String NAME = "name";
        private static final String WEEKLY = "weekly";
        private static final String DAYS = "days";
        private static final String CONFIG = "config";
        private static final String STEP = "step";
        private static final String MONTHLY = "monthly";
        private static final String REPEAT_ON = "repeat_on";
        private static final String UNTIL = "until";

        // For reminder
        private final Integer remind_delta;

        //For recurrence
        private final Integer step;
        private final List<String> days;
        private final String repeat_on;
        private final String name;
        private final String until;

        public Value(Integer remind_delta, Integer step, List<String> days, String repeat_on, String name, String until) {
            this.remind_delta = remind_delta;
            this.step = step;
            this.days = days;
            this.until = until;
            this.name = name;
            this.repeat_on = repeat_on;
        }

        @Override
        public Map<String, Object> getCreateData() {
            //Is not used
            return null;
        }

        public Map<String, Object> getReminderData() {
            if (remind_delta == null || remind_delta == -1) {
                return null;
            }

            Map<String, Object> reminderData = new HashMap<>();
            reminderData.put(REMIND_DELTA, remind_delta);

            return reminderData;
        }

        public Map<String, Object> getRecurrenceData() {
            if(name == null) {
                return null;
            }

            Map<String, Object> recurrenceData = new HashMap<>();
            Map<String, Object> configData = new HashMap<>();

            recurrenceData.put(NAME, name);
            if(name.equalsIgnoreCase(WEEKLY)) {
                configData.put(DAYS, days);
                recurrenceData.put(CONFIG, configData);
                recurrenceData.put(STEP, step);
            }
            else if(name.equalsIgnoreCase(MONTHLY)) {
                configData.put(REPEAT_ON, repeat_on);
                recurrenceData.put(CONFIG, configData);
                recurrenceData.put(STEP, step);
            }
            recurrenceData.put(UNTIL, until);

            return recurrenceData;
        }

        public Integer getRemindDelta() {
            return remind_delta;
        }

        public Integer getStep() {
            return step;
        }

        public List<String> getDays() {
            return days;
        }

        public String getName() {
            return name;
        }

        public String getUntil() {
            return until;
        }

        public String getRepeatOn() {
            return repeat_on;
        }
    }

    private final ArrayList<ReminderRecurrenceField.Value> values;

    public ReminderRecurrenceField() {
        super(NAME);
        this.values = new ArrayList<>();
    }

    @Override
    public void addValue(Value value) {
        if (values != null && !values.contains(value)) {
            values.clear();
            values.add(value);
        }
    }

    @Override
    public void setValues(List<Value> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    @Override
    public Value getValue(int index) {
        return values != null ? values.get(index) : null;
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    @Override
    public void removeValue(Value value) {
        if (values != null && values.contains(value)) {
            values.remove(value);
        }
    }

    @Override
    public void clearValues() {
        values.clear();
    }

    @Override
    public Type getType() {
        return Type.reminder_recurrence;
    }
}

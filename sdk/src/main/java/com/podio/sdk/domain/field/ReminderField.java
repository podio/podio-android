package com.podio.sdk.domain.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 9/29/16.
 */

public class ReminderField extends Field<ReminderField.Value>{

    public static class Value extends Field.Value {
        private final Integer remind_delta;

        public Value(int remind_delta) {
            this.remind_delta = remind_delta;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if(remind_delta != null) {
                data = new HashMap<>();
                data.put("remind_delta", remind_delta);
            }
            return data;
        }

        public int getRemindDelta(){
            return remind_delta;
        }
    }

    private final ArrayList<ReminderField.Value> values;

    public ReminderField(String externalId) {
        super(externalId);
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
        return Type.reminder;
    }
}

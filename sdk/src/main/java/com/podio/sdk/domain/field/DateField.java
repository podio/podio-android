
package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class DateField extends Field<DateField.Value> {
    /**
     * This class describes the particular settings of a Date field configuration.
     *
     */
    private static class Settings {
        private Boolean calendar = null;
        private final String end = null;
        private String time = null;
        private Boolean isCreateViewEditHidden = null;

        public Settings(Boolean calendar, String time, Boolean isCreateViewEditHidden){
            this.calendar = calendar;
            this.time = time;
            this.isCreateViewEditHidden = isCreateViewEditHidden;
        }
    }

    /**
     * This class describes the specific configuration of a Date field.
     *
     */
    public static class Configuration extends Field.Configuration {
        private final Value default_value = null;
        private Settings settings = null;


        public Configuration(Settings settings) {
            this.settings = settings;
        }

        public Value getDefaultValue() {
            return default_value;
        }

        public boolean isCalendar() {
            return settings != null && Utils.getNative(settings.calendar, false);
        }

        public State getEndDateState() {
            try {
                return State.valueOf(settings.end);
            } catch (NullPointerException e) {
                return State.undefined;
            } catch (IllegalArgumentException e) {
                return State.undefined;
            }
        }

        public State getTimeState() {
            try {
                return State.valueOf(settings.time);
            } catch (NullPointerException e) {
                return State.undefined;
            } catch (IllegalArgumentException e) {
                return State.undefined;
            }
        }

        @Override
        public boolean getIsHiddenCreateViewEdit() {
            if(settings != null && settings.isCreateViewEditHidden != null) {
                return settings.isCreateViewEditHidden;
            }
            return super.getIsHiddenCreateViewEdit();
        }
    }

    /**
     * This class describes a Date field value.
     *
     */
    public static class Value extends Field.Value {
        private final String end;
        private final String end_date = null;
        private final String end_date_utc = null;
        private final String end_time = null;
        private final String end_time_utc = null;
        private final String end_utc;
        private final String start;
        private String start_date = null;
        private String start_date_utc = null;
        private String start_time = null;
        private String start_time_utc = null;
        private String start_utc;

        public Value(Date start) {
            this(start, null);
        }

        public Value(Date start, Date end) {
            this.start = start != null ? Utils.formatDateTimeDefault(start) : null;
            this.end = end != null ? Utils.formatDateTimeDefault(end) : null;
            start_utc = null;
            end_utc = null;
        }

        public Value(Date start, boolean startHasTime, Date end, boolean endHasTime) {
            this.start = null;
            this.end = null;
            if (startHasTime) {
                this.start_utc = start != null ? Utils.formatDateTimeUtc(start) : null;
            } else {
                this.start_utc = start != null ? Utils.formatDateDefault(start) : null;
            }

            if (endHasTime) {
                this.end_utc = end != null ? Utils.formatDateTimeUtc(end): null;
            } else {
                this.end_utc = end != null ? Utils.formatDateDefault(end) : null;
            }
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if (Utils.notEmpty(start) || Utils.notEmpty(end)
                    || Utils.notEmpty(start_utc) || Utils.notEmpty(end_utc)) {
                data = new HashMap<String, Object>();

                if (Utils.notEmpty(start)) {
                    data.put("start", start);
                }

                if (Utils.notEmpty(end)) {
                    data.put("end", end);
                }

                if (Utils.notEmpty(start_utc)) {
                    data.put("start_utc", start_utc);
                }

                if (Utils.notEmpty(end_utc)) {
                    data.put("end_utc", end_utc);
                }
            }

            return data;
        }

        /**
         * @return returns true if you have a utc start date.
         */
        public boolean hasStartDateUtc() {
            return start_date_utc != null;
        }

        /**
         * @return returns true if you have a utc end date.
         */
        public boolean hasEndDateUtc() {
            return end_date_utc != null;
        }

        /**
         * @return returns true if you can rely on having a start time component in the UTC start
         * date Date object, otherwise false.
         */
        public boolean hasStartTimeUtc() {
            return start_time_utc != null;
        }

        /**
         * @return returns true if you can rely on having a end time component in the UTC end date
         * Date object, otherwise false.
         */
        public boolean hasEndTimeUtc() {
            return end_time_utc != null;
        }

        public Date getEndDateTime() {
            return Utils.parseDateTimeDefault(end);
        }

        public Date getEndDate() {
            return Utils.parseDateDefault(end_date);
        }

        public Date getEndDateUtc() {
            return Utils.parseDateUtc(end_date_utc);
        }

        public Date getEndTime() {
            return Utils.parseTimeDefault(end_time);
        }

        public Date getEndTimeUtc() {
            return Utils.parseTimeUtc(end_time_utc);
        }

        public Date getEndUtc() {
            if (hasEndTimeUtc()) {
                return Utils.parseDateTimeUtc(end_utc);
            } else {
                return Utils.parseDateDefault(end_utc);
            }
        }

        public Date getStartDateTime() {
            return Utils.parseDateTimeUtc(start);
        }

        public Date getStartDate() {
            return Utils.parseDateDefault(start_date);
        }

        public Date getStartDateUtc() {
            return Utils.parseDateUtc(start_date_utc);
        }

        public Date getStartTime() {
            return Utils.parseTimeDefault(start_time);
        }

        public Date getStartTimeUtc() {
            return Utils.parseTimeUtc(start_time_utc);
        }

        public Date getStartUtc() {
            if (hasStartTimeUtc()) {
                return Utils.parseDateTimeUtc(start_utc);
            } else {
                return Utils.parseDateDefault(start_utc);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value = (Value) o;

            if (end != null ? !end.equals(value.end) : value.end != null) return false;
            if (end_date != null ? !end_date.equals(value.end_date) : value.end_date != null)
                return false;
            if (end_date_utc != null ? !end_date_utc.equals(value.end_date_utc) : value.end_date_utc != null)
                return false;
            if (end_time != null ? !end_time.equals(value.end_time) : value.end_time != null)
                return false;
            if (end_time_utc != null ? !end_time_utc.equals(value.end_time_utc) : value.end_time_utc != null)
                return false;
            if (end_utc != null ? !end_utc.equals(value.end_utc) : value.end_utc != null)
                return false;
            if (start != null ? !start.equals(value.start) : value.start != null) return false;
            if (start_date != null ? !start_date.equals(value.start_date) : value.start_date != null)
                return false;
            if (start_date_utc != null ? !start_date_utc.equals(value.start_date_utc) : value.start_date_utc != null)
                return false;
            if (start_time != null ? !start_time.equals(value.start_time) : value.start_time != null)
                return false;
            if (start_time_utc != null ? !start_time_utc.equals(value.start_time_utc) : value.start_time_utc != null)
                return false;
            return !(start_utc != null ? !start_utc.equals(value.start_utc) : value.start_utc != null);

        }

        @Override
        public int hashCode() {
            int result = end != null ? end.hashCode() : 0;
            result = 31 * result + (end_date != null ? end_date.hashCode() : 0);
            result = 31 * result + (end_date_utc != null ? end_date_utc.hashCode() : 0);
            result = 31 * result + (end_time != null ? end_time.hashCode() : 0);
            result = 31 * result + (end_time_utc != null ? end_time_utc.hashCode() : 0);
            result = 31 * result + (end_utc != null ? end_utc.hashCode() : 0);
            result = 31 * result + (start != null ? start.hashCode() : 0);
            result = 31 * result + (start_date != null ? start_date.hashCode() : 0);
            result = 31 * result + (start_date_utc != null ? start_date_utc.hashCode() : 0);
            result = 31 * result + (start_time != null ? start_time.hashCode() : 0);
            result = 31 * result + (start_time_utc != null ? start_time_utc.hashCode() : 0);
            result = 31 * result + (start_utc != null ? start_utc.hashCode() : 0);
            return result;
        }
    }

    public static enum State {
        disabled, enabled, required, undefined
    }

    // Private fields.
    private Configuration config = null;
    private final ArrayList<Value> values;

    public DateField(String externalId) {
        super(externalId);
        this.values = new ArrayList<Value>();
    }

    public DateField(CalculationField calculationField){
        super(calculationField);

        final CalculationField.Configuration configuration = calculationField.getConfiguration();
        this.config = new Configuration(new Settings(configuration.isCalendar(), configuration.getTimeState(), configuration.getIsHiddenCreateViewEdit()));
        this.values = new ArrayList<>();
        for (Field.Value calcValue : calculationField.getValues()) {
            this.values.add((DateField.Value)calcValue);
        }

    }

    @Override
    public void setValues(List<Value> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    @Override
    public void addValue(Value value) {
        if (values != null && !values.contains(value)) {
            values.clear();
            values.add(0, value);
        }
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
    public int valuesCount() {
        return values != null ? values.size() : 0;
    }

    public Configuration getConfiguration() {
        return config;
    }
}

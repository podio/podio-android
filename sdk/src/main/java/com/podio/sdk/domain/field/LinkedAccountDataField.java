package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 8/31/16.
 */
public class LinkedAccountDataField extends Field<LinkedAccountDataField.Value> {

    public static class Value extends Field.Value {
        private final String url;
        private final String info;
        private final String type;
        private final String id;

        public Value(String url, String info, String type, String id) {
            this.url = url;
            this.info = info;
            this.type = type;
            this.id = id;
        }

        @Override
        public Map<String, Object> getCreateData() {
            HashMap<String, Object> data = null;

            if(Utils.notEmpty(url) && Utils.notEmpty(id)) {
                data = new HashMap<>();
                data.put("url", url);
                data.put("info", info);
                data.put("type", type);
                data.put("id", id);
            }
            return data;
        }

        public Long getLinkedAccountId() {
           return Long.parseLong(id);
        }

        public String getUrl(){
            return url;
        }

        public String getInfo() {
            return info;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }
    }

    private final ArrayList<LinkedAccountDataField.Value> values;

    public LinkedAccountDataField(String externalId) {
        super(externalId);
        this.values = new ArrayList<>();
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
            values.add(value);
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

    @Override
    public Type getType() {
        return Type.linked_account_data;
    }
}

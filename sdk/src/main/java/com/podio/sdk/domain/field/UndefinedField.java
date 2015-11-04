
package com.podio.sdk.domain.field;

import java.util.List;

/**
 * Fall back Podio field when new fields are introduced and we want to not get a null pointer
 * exception in the parsing.
 *
 */
public class UndefinedField extends Field<Field.Value> {

    public UndefinedField(String externalId) {
        super(externalId);
    }

    @Override
    public void setValues(List<Value> values) {
    }

    @Override
    public void addValue(Value value) {
    }

    @Override
    public Value getValue(int index) {
        return null;
    }

    @Override
    public List<Value> getValues() {
        return null;
    }

    @Override
    public void removeValue(Value value) {
    }

    @Override
    public void clearValues() {

    }

    @Override
    public int valuesCount() {
        return 0;
    }

    public Configuration getConfiguration() {
        return null;
    }
}

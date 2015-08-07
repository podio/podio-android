/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.podio.sdk.domain.field;

import com.podio.sdk.internal.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author László Urszuly
 */
public abstract class Field<T extends Field.Value> {

    /**
     * This class describes the common properties of a field value. Specific fields may have more
     * properties, in which case they will themselves define an extended version fo this class.
     *
     * @author László Urszuly
     */
    public static abstract class Value {
        public abstract Map<String, Object> getCreateData();
    }

    /**
     * This class describes the common configuration of a field. Specific fields may have more
     * options, in which case they will themselves define an extended version of this class.
     *
     * @author László Urszuly
     */
    public static abstract class Configuration {
        private final Integer delta = null;
        private final String description = null;
        private final String label = null;
        private final Boolean hidden = null;
        private final Boolean required = null;
        private final Boolean visible = null;

        // FIXME: Was ist das "mapping"?
        // public final Mapping mapping = null;

        /**
         * Returns the description of this field.
         *
         * @return A description string.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the user-facing name of this field.
         *
         * @return The field name.
         */
        public String getLabel() {
            return label;
        }

        /**
         * Returns the position of this field within its parent template.
         *
         * @return The delta position of the field.
         */
        public int getPosition() {
            return delta != null ? delta.intValue() : -1;
        }

        /**
         * Returns the hidden state of this field.
         *
         * @return Boolean true if the field is hidden, boolean false otherwise.
         */
        public boolean isHidden() {
            return hidden != null ? hidden.booleanValue() : false;
        }

        /**
         * Returns the required state of this field.
         *
         * @return Boolean true if the field is required, boolean false otherwise.
         */
        public boolean isRequired() {
            return required != null ? required.booleanValue() : false;
        }

        /**
         * Returns the visible state of this field.
         *
         * @return Boolean true if the field is visible, boolean false otherwise.
         */
        public boolean isVisible() {
            return visible != null ? visible.booleanValue() : true;
        }
    }

    /**
     * The field status enumeration. Each field can have exactly one of these status values.
     *
     * @author László Urszuly
     */
    public static enum Status {
        active, deleted, undefined
    }

    /**
     * The field type enumeration. Each field can have exactly one of these type values.
     *
     * @author Christian Holm
     */
    public static enum Type {
        app(ReferenceField.class),
        calculation(CalculationField.class),
        category(CategoryField.class),
        contact(ContactField.class),
        date(DateField.class),
        duration(DurationField.class),
        embed(LinkField.class),
        image(ImageField.class),
        location(LocationField.class),
        money(MoneyField.class),
        number(NumberField.class),
        progress(ProgressField.class),
        text(TextField.class),
        email(EmailField.class),
        phone(PhoneField.class),
        undefined(UndefinedField.class);

        private final Class<? extends Field> fieldClass;

        Type(Class<? extends Field> fieldClass) {
            this.fieldClass = fieldClass;
        }

        public Class<? extends Field> getFieldClass() {
            return fieldClass;
        }
    }

    private Long field_id = null;
    private String external_id = null;
    private String label = null;
    private String status = null;
    private String type = null;

    /**
     * Creates a client side field instance. This field is not automatically sent to the API.
     *
     * @param externalId
     *         The desired external id for this field. The API reserves the right of ignoring this
     *         hint.
     */
    public Field(String externalId) {
        this.external_id = externalId;
    }

    public Field(CalculationField calculationField) {
        this.status = calculationField.getStatus().name();
        this.type = calculationField.getType().name();
        this.field_id = calculationField.getFieldId();
        this.label = calculationField.getLabel();
    }

    /**
     * Get the values that can be pushed back to the API.
     *
     * @return A field specific list of values.
     */
    public List<Map<String, Object>> getCreateData() {
        ArrayList<Map<String, Object>> createData = null;
        List<T> values = getValues();

        if (Utils.notEmpty(values)) {
            createData = new ArrayList<Map<String, Object>>();

            for (T value : values) {
                Map<String, Object> data = value != null ? value.getCreateData() : null;

                if (data != null) {
                    createData.add(data);
                }
            }
        }

        return createData;
    }

    /**
     * Returns the external id of this field.
     *
     * @return A textual id.
     */
    public String getExternalId() {
        return external_id;
    }

    /**
     * Returns the Podio id of this field.
     *
     * @return A numeric id.
     */
    public long getFieldId() {
        return Utils.getNative(field_id, -1L);
    }

    /**
     * Returns the label of this field.
     *
     * @return The user-facing name of this field.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the status of this field.
     *
     * @return An enumeration value, describing the field status.
     */
    public Status getStatus() {
        try {
            return Status.valueOf(status);
        } catch (NullPointerException e) {
            return Status.undefined;
        } catch (IllegalArgumentException e) {
            return Status.undefined;
        }
    }

    /**
     * Returns the type of this field.
     *
     * @return An enumeration value, describing the field type.
     */
    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.undefined;
        } catch (IllegalArgumentException e) {
            return Type.undefined;
        }
    }

    /**
     * Tries to locally add the given value to the current field. The changes are NOT updated on the
     * servers by this method call.
     *
     * @param value
     *         The value domain object to set.
     */
    public abstract void addValue(T value);

    public abstract void setValues(List<T> values);

    /**
     * Returns the value at the given position for this field.
     *
     * @return A value object specific for this field type.
     */
    public abstract T getValue(int index);

    /**
     * Returns all the values for this field.
     *
     * @return A list of value objects specific for this field type.
     */
    public abstract List<T> getValues();

    /**
     * Returns the number of values for this field.
     *
     * @return The size of the values list.
     */
    public abstract int valuesCount();

    /**
     * Tries to locally clear the given value from the current field. The changes are NOT updated on
     * the servers by this method call.
     *
     * @param value
     *         The value domain object to clear.
     */
    public abstract void removeValue(T value);

    /**
     * Clears the values array and thus make it an empty array. Useful when user clears the field.
     * The changes are NOT updated on the servers by this method call.
     */
    public abstract void clearValues();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (!field_id.equals(field.field_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return field_id.hashCode();
    }
}

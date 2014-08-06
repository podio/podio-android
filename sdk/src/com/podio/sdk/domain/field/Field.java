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

import java.util.ArrayList;
import java.util.List;

import com.podio.sdk.internal.Utils;

/**
 * @author László Urszuly
 */
public abstract class Field implements Pushable {

    /**
     * The field status enumeration. Each field can have exactly one of these
     * status values.
     * 
     * @author László Urszuly
     */
    public static enum Status {
        active, deleted, undefined
    }

    /**
     * The field type enumeration. Each field can have exactly one of these type
     * values.
     * 
     * @author Christian Holm
     */
    public static enum Type {
        app(RelationshipField.class),
        calculation(CalculationField.class),
        category(CategoryField.class),
        contact(ContactField.class),
        date(DateField.class),
        duration(DurationField.class),
        embed(LinkField.class),
        image(ImageField.class),
        location(MapField.class),
        money(MoneyField.class),
        number(NumberField.class),
        progress(ProgressField.class),
        text(TextField.class),
        undefined(EmptyField.class);

        private final Class<? extends Field> fieldClass;

        private Type(Class<? extends Field> fieldClass) {
            this.fieldClass = fieldClass;
        }

        public Class<? extends Field> getFieldClass() {
            return fieldClass;
        }
    }

    private final Long field_id = null;
    private final String external_id;
    private final String label = null;
    private final Status status = null;
    private final Type type = null;

    /**
     * Creates a client side field instance. This field is not automatically
     * sent to the API.
     * 
     * @param externalId
     *        The desired external id for this field. The API reserves the right
     *        of ignoring this hint.
     */
    public Field(String externalId) {
        this.external_id = externalId;
    }

    @Override
    public Object getPushData() {
        List<Object> pushData = new ArrayList<Object>();
        List<? extends Pushable> values = getPushables();

        if (values != null) {
            for (Pushable value : values) {
                Object data = value != null ? value.getPushData() : null;

                if (data != null) {
                    pushData.add(data);
                }
            }
        }

        return pushData;
    }

    /**
     * Tries to locally add the given value to the current field. The changes
     * are NOT updated on the servers by this method call.
     * 
     * @param value
     *        The value domain object to set.
     * @throws FieldTypeMismatchException
     *         If the value can't be applied to this field type.
     */
    public void addValue(Object value) throws FieldTypeMismatchException {
        throw new FieldTypeMismatchException();
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
        return status != null ? status : Status.undefined;
    }

    /**
     * Returns the type of this field.
     * 
     * @return An enumeration value, describing the field type.
     */
    public Type getType() {
        return type != null ? type : Type.undefined;
    }

    /**
     * Returns the value at the given position for this field.
     * 
     * @return A value object specific for this field type.
     */
    public Object getValue(int index) {
        throw new FieldTypeMismatchException();
    }

    /**
     * Tries to locally clear the given value from the current field. The
     * changes are NOT updated on the servers by this method call.
     * 
     * @param value
     *        The value domain object to clear.
     * @throws FieldTypeMismatchException
     *         If the value can't be applied to this field type.
     */
    public void removeValue(Object value) throws FieldTypeMismatchException {
        throw new FieldTypeMismatchException();
    }

    /**
     * Get the values that can be pushed back to the API.
     * 
     * @return A field specific list of values.
     */
    protected abstract List<? extends Pushable> getPushables();

}

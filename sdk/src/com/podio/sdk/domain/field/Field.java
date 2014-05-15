//@formatter:off

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

//@formatter:on

package com.podio.sdk.domain.field;

public abstract class Field implements Pushable {

    public static enum Status {
        active, deleted, undefined
    }

    public static enum Type {
        app, calculation, category, contact, date, duration, embed, image, location, money, number,
        progress, text, title, undefined
    }

    public final Integer field_id = null;
    public final String label = null;
    public final Status status = null;
    public final Type type = null;

    public final String external_id;

    public Field(String externalId) {
        this.external_id = externalId;
    }

    /**
     * Tries to locally clear the given value from the current field. The
     * changes are NOT updated on the servers by this method call.
     * 
     * @param value
     *        The value domain object to clear.
     * @return Always boolean true.
     * @throws FieldTypeMismatchException
     *         If the value can't be applied to this field type.
     */
    public abstract boolean clear(Object value) throws FieldTypeMismatchException;

    /**
     * Tries to locally set the given value to the current field. The changes
     * are NOT updated on the servers by this method call.
     * 
     * @param value
     *        The value domain object to set.
     * @return Always boolean true.
     * @throws FieldTypeMismatchException
     *         If the value can't be applied to this field type.
     */
    public abstract boolean set(Object value) throws FieldTypeMismatchException;

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Field && field_id == ((Field) o).field_id;
    }

    @Override
    public int hashCode() {
        return field_id;
    }
}

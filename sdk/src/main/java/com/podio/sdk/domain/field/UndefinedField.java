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

import java.util.List;

/**
 * Fall back Podio field when new fields are introduced and we want to not get a null pointer
 * exception in the parsing.
 *
 * @author Tobias Lindberg
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
    public int valuesCount() {
        return 0;
    }

    public Configuration getConfiguration() {
        return null;
    }
}

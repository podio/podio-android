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

package com.podio.sdk.domain.field.configuration;

/**
 * @author László Urszuly
 */
public abstract class AbstractConfiguration {
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

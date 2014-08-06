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

package com.podio.sdk.domain.field.value;

import java.util.HashMap;

import com.podio.sdk.domain.File;

/**
 * @author László Urszuly
 */
public final class ImageValue extends AbstractValue {
    private final File value;

    public ImageValue(File file) {
        this.value = file;
    }

    public ImageValue(long fileId) {
        this.value = new File(fileId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageValue) {
            ImageValue other = (ImageValue) o;

            if (other.value != null && this.value != null) {
                return other.value.getId() == this.value.getId();
            }
        }

        return false;
    }

    @Override
    public Object getPushData() {
        HashMap<String, Long> data = null;

        if (value != null) {
            long fileId = value.getId();

            if (fileId > 0L) {
                data = new HashMap<String, Long>();
                data.put("value", fileId);
            }
        }

        return data;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

}

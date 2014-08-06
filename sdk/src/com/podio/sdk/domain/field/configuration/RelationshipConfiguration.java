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

import java.util.ArrayList;
import java.util.List;

import com.podio.sdk.domain.Application;
import com.podio.sdk.domain.field.value.RelationshipValue;

/**
 * @author László Urszuly
 */
public final class RelationshipConfiguration extends AbstractConfiguration {

    public final class RelationshipSettings {
        private List<Application> apps = null;
        private List<Long> referencable_types = null;

        public List<Application> getApps() {
            return new ArrayList<Application>(apps);
        }

        public List<Long> getReferencableTypes() {
            return new ArrayList<Long>(referencable_types);
        }

    }

    private final RelationshipValue default_value = null;
    private final RelationshipSettings settings = null;

    public RelationshipValue getDefaultValue() {
        return default_value;
    }

    public RelationshipSettings getSettings() {
        return settings;
    }

}

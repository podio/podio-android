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

import java.util.ArrayList;
import java.util.HashMap;

public final class Category extends Field {

    public static final class Option implements Pushable {
        public final String status = null;
        public final String text = null;
        public final Integer id = null;
        public final String color = null;

        private boolean isPicked = false;

        public boolean isPicked() {
            return isPicked;
        }

        public void setPicked(boolean isPicked) {
            this.isPicked = isPicked;
        }

        @Override
        public Object getPushData() {
            HashMap<String, Integer> pushData = new HashMap<String, Integer>();
            pushData.put("value", id);
            return pushData;
        }
    }

    public static final class Config {

        public static final class Settings {
            public final String display = null;
            public final Boolean multiple = null;
            public final Option[] options = null;
        }

        public final Settings settings = null;
    }

    public static final class Value {
        public final Option value = null;
    }

    public final Config config = null;
    public final Value[] values = null;

    @Override
    public Object getPushData() {
        ArrayList<Object> pushData = new ArrayList<Object>();

        if (config != null && config.settings != null && config.settings.options != null) {
            for (Option option : config.settings.options) {
                if (option.isPicked) {
                    pushData.add(option.getPushData());
                }
            }
        }

        return pushData;
    }
}
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

import com.podio.sdk.domain.field.value.CalculationValue;

/**
 * @author László Urszuly
 */
public final class CalculationConfiguration extends AbstractConfiguration {

    public static final class CalculationSettings {
        private final Integer decimals = null;
        private final String return_type = null;
        private final String script = null;
        private final String time = null;
        private final String unit = null;

        // FIXME: Decide on the extent of script field support. Should Android
        // users be allowed to modify the script e.g.?
        // public final Object expression = null;

        /**
         * Returns the number of decimals to show for this field.
         * 
         * @return A count value.
         */
        public int getNumberOfDecimals() {
            return decimals != null ? decimals.intValue() : 0;
        }

        /**
         * Returns the type of output from this script.
         * 
         * @return A status string.
         */
        public String getReturnType() {
            return return_type;
        }

        /**
         * Returns the actual script for this field.
         * 
         * @return A script string.
         */
        public String getScript() {
            return script;
        }

        /**
         * @return A status string.
         */
        public String getTimeState() {
            return time;
        }

        /**
         * Returns the user-facing unit of the calculated value.
         * 
         * @return A unit name.
         */
        public String getUnit() {
            return unit;
        }
    }

    private final CalculationValue default_value = null;
    private final CalculationSettings settings = null;

    public CalculationValue getDefaultValue() {
        return default_value;
    }

    public CalculationSettings getSettings() {
        return settings;
    }

}

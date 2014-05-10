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

public final class Category extends Field {

    public static final class Config {

        public static final class Settings {

            public static final class Option {
                public final String status = null;
                public final String text = null;
                public final Integer id = null;
                public final String color = null;
            }

            public final Boolean multiple = null;
            public final Settings.Option[] options = null;
            public final String display = null;
        }

        public final Config.Settings settings = null;
    }

    public static final class Value {

        public static final class Data {
            public final String status = null;
            public final String text = null;
            public final Integer id = null;
            public final String color = null;
        }

        public final Value.Data value = null;
    }

    public final Category.Config config = null;
    public final Category.Value[] values = null;
}

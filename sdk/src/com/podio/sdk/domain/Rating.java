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

package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

/**
 * @author Daniel Franch
 */
public class Rating {

    public class Count {
        private final Integer total = null;
        private final List<User> users = null;

        public int getTotal() {
            return Utils.getNative(total, 0);
        }

        public List<User> getUsers() {
            return new ArrayList<User>(users);
        }
    }

    private class Counts {
        @SerializedName("1")
        private final Count one = null;

        @SerializedName("2")
        private final Count two = null;

        @SerializedName("3")
        private final Count three = null;

        @SerializedName("4")
        private final Count four = null;

        @SerializedName("5")
        private final Count five = null;
    }

    private class Fivestar {
        private final Counts counts = null;
    }

    private class Like {
        private final Counts counts = null;
    }

    private final Fivestar fivestar = null;
    private final Like like = null;

    public Count getOneStars() {
        return fivestar != null && fivestar.counts != null ? fivestar.counts.one : null;
    }

    public Count getTwoStars() {
        return fivestar != null && fivestar.counts != null ? fivestar.counts.two : null;
    }

    public Count getThreeStars() {
        return fivestar != null && fivestar.counts != null ? fivestar.counts.three : null;
    }

    public Count getFourStars() {
        return fivestar != null && fivestar.counts != null ? fivestar.counts.four : null;
    }

    public Count getFiveStars() {
        return fivestar != null && fivestar.counts != null ? fivestar.counts.five : null;
    }

    public Count getLikes() {
        return like != null && like.counts != null ? like.counts.one : null;
    }

}
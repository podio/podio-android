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

import android.os.Parcel;
import android.os.Parcelable;

import com.podio.sdk.internal.Utils;

public class Push implements Parcelable {
    public static final Push EMPTY = new Push();

    public static final Parcelable.Creator<Push> CREATOR = new Parcelable.Creator<Push>() {
        public Push createFromParcel(Parcel in) {
            return new Push(in);
        }

        public Push[] newArray(int size) {
            return new Push[size];
        }
    };

    private final Long timestamp;
    private final Integer expires_in;
    private final String channel;
    private final String signature;

    private Push(Parcel parcel) {
        this.timestamp = parcel.readLong();
        this.expires_in = parcel.readInt();
        this.channel = parcel.readString();
        this.signature = parcel.readString();
    }

    private Push() {
        this.timestamp = null;
        this.expires_in = null;
        this.channel = null;
        this.signature = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Utils.getObject(timestamp, -1L));
        dest.writeInt(Utils.getObject(expires_in, 1));
        dest.writeString(Utils.getObject(channel, ""));
        dest.writeString(Utils.getObject(signature, ""));
    }

    public long getTimestamp() {
        return Utils.getNative(timestamp, -1L);
    }

    public int getExpiresIn() {
        return Utils.getNative(expires_in, -1);
    }

    public String getChannel() {
        return channel;
    }

    public String getSignature() {
        return signature;
    }

}

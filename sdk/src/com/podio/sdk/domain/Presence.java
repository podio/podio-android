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

public class Presence implements Parcelable {

    public static final Parcelable.Creator<Presence> CREATOR = new Parcelable.Creator<Presence>() {
        public Presence createFromParcel(Parcel in) {
            return new Presence(in);
        }

        public Presence[] newArray(int size) {
            return new Presence[size];
        }
    };

    private final Long ref_id;
    private final Long user_id;
    private final String ref_type;
    private final String signature;

    private Presence(Parcel parcel) {
        this.ref_id = parcel.readLong();
        this.user_id = parcel.readLong();
        this.ref_type = parcel.readString();
        this.signature = parcel.readString();
    }

    private Presence() {
        this.ref_id = null;
        this.user_id = null;
        this.ref_type = null;
        this.signature = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ref_id);
        dest.writeLong(user_id);
        dest.writeString(ref_type);
        dest.writeString(signature);
    }

    public Type getRefType() {
        try {
            return Type.valueOf(ref_type);
        } catch (NullPointerException e) {
            return Type.unknown;
        } catch (IllegalArgumentException e) {
            return Type.unknown;
        }
    }

    public long getRefId() {
        return Utils.getNative(ref_id, -1L);
    }

    public long getUserId() {
        return Utils.getNative(user_id, -1L);
    }

    public String getSignature() {
        return signature;
    }

}

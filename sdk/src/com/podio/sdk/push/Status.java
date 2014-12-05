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
package com.podio.sdk.push;

import com.podio.sdk.internal.Utils;

import java.util.List;

class Status {

    static final class Advice {
        private final String reconnect = null;
        private final Integer interval = null;
        private final Integer timeout = null;

        Approach reconnectApproach() {
            try {
                return Approach.valueOf(reconnect);
            } catch (NullPointerException e) {
                return Approach.unknown;
            } catch (IllegalArgumentException e) {
                return Approach.unknown;
            }
        }

        int reconnectInterval() {
            return Utils.getNative(interval, -1);
        }

        int reconnectTimeout() {
            return Utils.getNative(timeout, -1);
        }

    }

    static enum Approach {
        retry, handshake, unknown
    }

    private final String id = null;
    private final String clientId = null;
    private final String channel = null;
    private final Boolean successful = null;
    private final String version = null;
    private final List<String> supportedConnectionTypes = null;
    private final String error = null;
    private final String subscription = null;
    private final Advice advice = null;

    String id() {
        return id;
    }

    String clientId() {
        return clientId;
    }

    String channel() {
        return channel;
    }

    String version() {
        return version;
    }

    String error() {
        return error;
    }

    String subscription() {
        return subscription;
    }

    boolean hasAdvice() {
        return advice != null;
    }

    Advice advice() {
        return advice;
    }

    boolean isSuccess() {
        return Utils.getNative(successful, false);
    }

    boolean hasError() {
        return Utils.notEmpty(error);
    }

    boolean hasSupportForConnectionType(String connectionType) {
        return supportedConnectionTypes != null && supportedConnectionTypes.contains(connectionType);
    }
}

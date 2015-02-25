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

/**
 * A Java representation of the BatchDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Batch {

    public static enum PluginTypes {
        app_import,
        app_export,
        connection_load,
        unknown // Custom value to handle errors.
    }

    private final String plugin = null;

    private final Integer completed = null;

    private final Integer skipped = null;

    private final Long batch_id = null;

    public long getBatchId() {
        return Utils.getNative(batch_id, -1L);
    }

    public PluginTypes getPluginType() {
        try {
            return PluginTypes.valueOf(plugin);
        } catch (NullPointerException e) {
            return PluginTypes.unknown;
        } catch (IllegalArgumentException e) {
            return PluginTypes.unknown;
        }
    }

    public int getSkipped() {
        return Utils.getNative(skipped, 0);
    }

    public int getCompleted() {
        return Utils.getNative(completed, 0);
    }

    // TODO Add missing attributes
}

/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * This class describes a push event sent by the API when someone is viewing a target object.
 *
 * @author László Urszuly
 */
public class PushEventViewing extends PushEvent {

    private final Long[] data = null;

    public long get(int index) throws IndexOutOfBoundsException {
        return Utils.getNative(data[index], -1);
    }

    public int size() {
        return Utils.notEmpty(data) ? data.length : 0;
    }

}

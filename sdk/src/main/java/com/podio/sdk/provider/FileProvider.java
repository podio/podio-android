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
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Request;
import com.podio.sdk.domain.File;
import com.podio.sdk.volley.VolleyProvider;

/**
 * This class provides methods to access {@link com.podio.sdk.domain.File} objects from the API.
 *
 * @author László Urszuly
 */
public class FileProvider extends VolleyProvider {

    static class Path extends Filter {

        protected Path() {
            super("file/v2");
        }

    }

    /**
     * Fetches the currently logged in user data.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<File> uploadFile(java.io.File file, String name, String mimeType) {
        Path filter = new Path();
        return post(filter, new File.PushData(file, name, mimeType), File.class);
    }

}

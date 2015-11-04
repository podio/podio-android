package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.File;

/**
 * This class is used when the activity is of type "file".
 *
 */
public class FileEventActivity extends EventActivity {

    private final File data = null;

    public File getFile() {
        return data;
    }

}

package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.File;

/**
 * This class is used when the stream object is of type "file".
 *
 * @author Tobias Lindbergﬁ
 */
public class FileEventContext extends EventContext {

    private final File data = null;

    public File getFile() {
        return data;
    }
}

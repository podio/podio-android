package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Status;

/**
 * This class is used when the stream object is of type "status".
 *
 * @author Tobias Lindberg
 */
public class StatusEventContext extends EventContext {

    private final Status data = null;

    public Status getStatus() {
        return data;
    }
}

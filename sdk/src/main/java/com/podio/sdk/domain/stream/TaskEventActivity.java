package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.Task;

/**
 * This class is used when the activity is of type "task".
 *
 * @author Tobias Lindberg
 */
public class TaskEventActivity extends EventActivity {

    private final Task data = null;

    public Task getTask() {
        return data;
    }

}

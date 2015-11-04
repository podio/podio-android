package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.TaskAction;

/**
 * This class is used when the activity is of type "task_action".
 *
 */
public class TaskActionEventActivity extends EventActivity {

    private final TaskAction data = null;

    public TaskAction getTaskAction() {
        return data;
    }

}

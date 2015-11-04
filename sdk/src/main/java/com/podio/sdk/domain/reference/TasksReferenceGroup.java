
package com.podio.sdk.domain.reference;

import com.podio.sdk.domain.Task;

import java.util.List;

/**
 * When the reference search group name is "tasks" you will get this object.
 *
 */
public class TasksReferenceGroup extends ReferenceGroup{
    private final List<Task> contents = null;

    public List<Task> getContents() {
        return contents;
    }
}

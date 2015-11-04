
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the TaskActionDTO API domain object.
 *
 */
public abstract class TaskAction {

    public static enum TaskActionType {
        creation,
        start,
        stop,
        assign,
        complete,
        incomplete,
        update_text,
        update_description,
        update_due_date,
        update_private,
        delete,
        update_ref,
        unknown; // Custom value to handle errors.

        public static TaskActionType getType(String type) {
            try {
                return TaskActionType.valueOf(type);
            } catch (NullPointerException e) {
                return TaskActionType.unknown;
            } catch (IllegalArgumentException e) {
                return TaskActionType.unknown;
            }
        }
    }

    private final Long task_action_id = null;

    private final String type = null;

    public long getTaskActionId() {
        return Utils.getNative(task_action_id, -1L);
    }

    public TaskActionType getType() {
        return TaskActionType.getType(type);
    }
}

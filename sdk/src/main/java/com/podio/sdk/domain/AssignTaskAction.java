
package com.podio.sdk.domain;

/**
 * A Java representation of the TaskActionDTO API domain object when the action type is of type
 * "assign".
 *
 */
public class AssignTaskAction extends TaskAction {

    Profile changed = null;

    public Profile getProfile() {
        return changed;
    }
}

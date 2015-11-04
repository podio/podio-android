
package com.podio.sdk.domain;

/**
 * A Java representation of the TaskActionDTO API domain object when the action type is of any type
 * but "assign".
 *
 */
public class TextTaskAction extends TaskAction{

    String changed = null;

    public String getText() {
        return changed;
    }
}

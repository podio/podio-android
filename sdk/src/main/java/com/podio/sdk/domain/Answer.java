
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the AnswerDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Answer {


    private final String text = null;
    private final Long answer_id = null;

    public String getText() {
        return text;
    }

    public long getAnswerId() {
        return Utils.getNative(answer_id,-1L);
    }

}

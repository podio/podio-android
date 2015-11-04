
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the QuestionOptionDTO API domain object.
 *
 */
public class QuestionOption {

    private final Long question_option_id = null;

    private final String text = null;

    public long getQuestionOptionId() {
        return Utils.getNative(question_option_id, -1L);
    }

    public String getText() {
        return text;
    }
}

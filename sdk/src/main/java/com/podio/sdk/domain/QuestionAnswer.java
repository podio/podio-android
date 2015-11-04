
package com.podio.sdk.domain;

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the QuestionAnswerDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class QuestionAnswer {

    private final Long question_option_id = null;

    private final QuestionOption question_option = null;

    public long getQuestionOptionId() {
        return Utils.getNative(question_option_id, -1L);
    }

    public QuestionOption getQuestionOption() {
        return question_option;
    }
}

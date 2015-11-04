package com.podio.sdk.domain.stream;

import com.podio.sdk.domain.QuestionAnswer;

/**
 * This class is used when the activity is of type "question_answer".
 *
 */
public class QuestionAnswerEventActivity extends EventActivity {

    private final QuestionAnswer data = null;

    public QuestionAnswer getQuestionAnswer() {
        return data;
    }

}

package com.example.backend.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionEnum {
    LISTENCHECK (0),
    REALCHECK(1),
    WRITE(2);

    private final int ValueQuestion;
}

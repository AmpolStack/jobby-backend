package com.jobby.authorization.domain.result;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Error {
    private String message;
    private ErrorCode code;
}

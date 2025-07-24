package com.jobby.authorization.domain.result;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Error {
    private ErrorType code;
    private Field[] fields;
}

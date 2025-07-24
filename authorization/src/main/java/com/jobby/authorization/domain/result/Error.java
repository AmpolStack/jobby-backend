package com.jobby.authorization.domain.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Error {
    private ErrorType code;
    private Field[] fields;
}

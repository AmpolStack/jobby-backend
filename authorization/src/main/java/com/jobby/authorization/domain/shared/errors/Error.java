package com.jobby.authorization.domain.shared.result;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Error {
    private ErrorType code;
    private Field[] fields;
}

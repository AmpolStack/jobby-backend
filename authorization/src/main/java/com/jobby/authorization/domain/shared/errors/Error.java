package com.jobby.authorization.domain.shared.errors;

import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
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

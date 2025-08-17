package com.jobby.authorization.domain.shared.errors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Field {
    @Setter
    private String instance;
    private String reason;
}

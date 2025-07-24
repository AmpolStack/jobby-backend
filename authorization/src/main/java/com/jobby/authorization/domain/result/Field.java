package com.jobby.authorization.domain.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Field {
    private String instance;
    private String reason;
}

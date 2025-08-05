package com.jobby.authorization.domain.shared;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenData {
    private int id;
    private String email;
    private String phone;
}

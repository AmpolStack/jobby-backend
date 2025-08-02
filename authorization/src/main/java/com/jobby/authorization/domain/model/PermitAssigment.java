package com.jobby.authorization.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class PermitAssigment {
    private Permit permit;
    private Date assignedAt;
}

package com.jobby.authorization.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Date;

@Getter
@Setter
public class PermitAssigment {
    private Permit permit;
    @Name("assigned_at")
    private Date assignedAt;
}

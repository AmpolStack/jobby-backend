package com.jobby.employee.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class EmployeeStatus {
    private int id;
    private String name;
    private Instant createdAt;
}

package com.jobby.authorization.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class Permit {
    private String name;
    private String visible;
    private String description;
    private Date modifiedAt;
    private Date createdAt;
}

package com.jobby.authorization.infraestructure.response.implementation.problem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobby.domain.mobility.Field;
import lombok.Getter;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetails {
    private URI type;
    private String title;
    private int status;
    private String detail;
    private Field[] errors;
    private final Instant timestamp;
    private Map<String, Object> context;

    public ProblemDetails() {
        this.timestamp = Instant.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ProblemDetails problem = new ProblemDetails();

        public Builder type(String type) {
            problem.type = URI.create(type);
            return this;
        }

        public Builder title(String title) {
            problem.title = title;
            return this;
        }

        public Builder status(int status) {
            problem.status = status;
            return this;
        }

        public Builder detail(String detail) {
            problem.detail = detail;
            return this;
        }

        public Builder errors(Field[] errors) {
            problem.errors = errors;
            return this;
        }

        public Builder addContext(String key, Object value) {
            if (problem.context == null) {
                problem.context = new java.util.HashMap<>();
            }
            problem.context.put(key, value);
            return this;
        }

        public ProblemDetails build() {
            return problem;
        }
    }

}

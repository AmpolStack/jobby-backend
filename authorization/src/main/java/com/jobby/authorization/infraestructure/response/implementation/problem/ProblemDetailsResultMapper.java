package com.jobby.authorization.infraestructure.response.implementation.problem;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.authorization.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProblemDetailsResultMapper implements ApiResponseMapper {
    
    public <T> ResponseEntity<?> map(Result<T, Error> result, HttpStatus successStatus) {
        if (result.isFailure()) {
            return ProblemDetailsMapper.toProblemDetails(result.getError());
        }
        return ResponseEntity.status(successStatus).body(result);
    }

    public <T> ResponseEntity<?> map(Result<T, Error> result) {
        return map(result, HttpStatus.OK);
    }

}

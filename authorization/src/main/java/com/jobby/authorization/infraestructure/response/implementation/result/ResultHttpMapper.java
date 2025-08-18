package com.jobby.authorization.infraestructure.response.implementation.result;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.infraestructure.response.definition.ErrorTypeHttpCollection;
import com.jobby.authorization.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultHttpMapper implements ApiResponseMapper {
    public <T> ResponseEntity<Result<T, Error>> map(
            Result<T, Error> result, HttpStatus successState)
    {

        if(result.isFailure()){
            var statusCode = ErrorTypeHttpCollection.toHttpStatus(result.getError().getCode());
            var error = ErrorTypeHttpCollection.toResponseError(result.getError());
            return ResponseEntity.status(statusCode).body(Result.failure(error));
        }

        return ResponseEntity.status(successState).body(result);
    }

    public <T> ResponseEntity<Result<T, Error>> map(
            Result<T, Error> result)
    {
        return map(result, HttpStatus.OK);
    }

}

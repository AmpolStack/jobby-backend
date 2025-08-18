package com.jobby.authorization.infraestructure.response.definition;

import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ApiResponseMapper {
    <T> ResponseEntity<?> map(Result<T, Error> result, HttpStatus successStatus);
    <T> ResponseEntity<?> map(Result<T, Error> result);
}

package com.jobby.authorization.infraestructure.response.implementation.problem;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.authorization.infraestructure.response.definition.ErrorTypeHttpCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.EnumMap;
import java.util.Map;

public class ProblemDetailsMapper {
    private static final String BASE_URI = "https://api.jobby.com/problems/";
    private static final Map<ErrorType, String> TITLES = new EnumMap<>(ErrorType.class);
    private static final Map<ErrorType, String> DETAILS = new EnumMap<>(ErrorType.class);
    
    static {
        // Visible errors
        TITLES.put(ErrorType.AUTHENTICATION_FAILED, "Authentication Failed");
        TITLES.put(ErrorType.VALIDATION_ERROR, "Validation Error");
        TITLES.put(ErrorType.TOKEN_EXPIRED, "Token Expired");
        TITLES.put(ErrorType.PERMISSION_DENIED, "Permission Denied");
        TITLES.put(ErrorType.USER_NOT_FOUND, "User Not Found");
        TITLES.put(ErrorType.NOT_FOUND, "Resource Not Found");
        
        DETAILS.put(ErrorType.AUTHENTICATION_FAILED, "The provided credentials are invalid");
        DETAILS.put(ErrorType.VALIDATION_ERROR, "The request data failed validation");
        DETAILS.put(ErrorType.TOKEN_EXPIRED, "The authentication token has expired");
        DETAILS.put(ErrorType.PERMISSION_DENIED, "You do not have permission to access this resource");
        DETAILS.put(ErrorType.USER_NOT_FOUND, "The requested user could not be found");
        DETAILS.put(ErrorType.NOT_FOUND, "The requested resource could not be found");
    }

    public static ResponseEntity<ProblemDetails> toProblemDetails(Error error) {
        ErrorType errorType = error.getCode();
        HttpStatus status = ErrorTypeHttpCollection.toHttpStatus(errorType);

        Error sanitizedError = ErrorTypeHttpCollection.toResponseError(error);
        
        ProblemDetails problem = ProblemDetails.builder()
                .type(BASE_URI + errorType.name().toLowerCase().replace('_', '-'))
                .title(TITLES.getOrDefault(errorType, "Internal Server Error"))
                .status(status.value())
                .detail(DETAILS.getOrDefault(errorType, "An unexpected error occurred"))
                .errors(sanitizedError.getFields())
                .addContext("errorCode", errorType.name())
                .build();

        return ResponseEntity.status(status)
                .header("Content-Type", "application/problem+json")
                .body(problem);
    }
}

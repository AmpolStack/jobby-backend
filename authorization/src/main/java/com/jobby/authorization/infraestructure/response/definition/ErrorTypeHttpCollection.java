package com.jobby.authorization.infraestructure.response.definition;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
import org.springframework.http.HttpStatus;
import java.util.EnumMap;
import java.util.Map;

public class ErrorTypeHttpCollection {
    private static final Map<ErrorType, HttpStatus> ERROR_HTTP_MAP = new EnumMap<>(ErrorType.class);
    static {
        // Errors visible to the user
        ERROR_HTTP_MAP.put(ErrorType.INVALID_INPUT, HttpStatus.BAD_REQUEST);
        ERROR_HTTP_MAP.put(ErrorType.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_HTTP_MAP.put(ErrorType.NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_HTTP_MAP.put(ErrorType.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED);
        ERROR_HTTP_MAP.put(ErrorType.PERMISSION_DENIED, HttpStatus.FORBIDDEN);
        ERROR_HTTP_MAP.put(ErrorType.RESOURCE_ALREADY_EXISTS, HttpStatus.CONFLICT);
        ERROR_HTTP_MAP.put(ErrorType.VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_ENTITY);
        ERROR_HTTP_MAP.put(ErrorType.UNSUPPORTED_OPERATION, HttpStatus.METHOD_NOT_ALLOWED);
        ERROR_HTTP_MAP.put(ErrorType.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        ERROR_HTTP_MAP.put(ErrorType.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        ERROR_HTTP_MAP.put(ErrorType.EMPLOYEE_INACTIVE, HttpStatus.FORBIDDEN);

        // Internal validation errors
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_NULL, HttpStatus.BAD_REQUEST);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_BLANK, HttpStatus.BAD_REQUEST);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_EMPTY, HttpStatus.BAD_REQUEST);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_FORMAT, HttpStatus.UNPROCESSABLE_ENTITY);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_RANGE, HttpStatus.UNPROCESSABLE_ENTITY);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_TYPE, HttpStatus.UNPROCESSABLE_ENTITY);
        ERROR_HTTP_MAP.put(ErrorType.ITN_VALIDATION_CUSTOM, HttpStatus.UNPROCESSABLE_ENTITY);

        // Internal system errors
        ERROR_HTTP_MAP.put(ErrorType.ITS_INVALID_OPTION_PARAMETER, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_HTTP_MAP.put(ErrorType.ITS_OPERATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_HTTP_MAP.put(ErrorType.ITS_INVALID_STATE, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_HTTP_MAP.put(ErrorType.ITS_DB_CONNECTION_FAILED, HttpStatus.SERVICE_UNAVAILABLE);
        ERROR_HTTP_MAP.put(ErrorType.ITS_EXTERNAL_SERVICE_FAILURE, HttpStatus.BAD_GATEWAY);
        ERROR_HTTP_MAP.put(ErrorType.ITS_CONFIGURATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_HTTP_MAP.put(ErrorType.ITS_SERIALIZATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        ERROR_HTTP_MAP.put(ErrorType.ITS_UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static HttpStatus toHttpStatus(ErrorType errorType) {
        return ERROR_HTTP_MAP.getOrDefault(errorType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Error toResponseError(Error error){
        var errorTye = error.getCode().toString();

        if(errorTye.startsWith("ITN")){
            return new Error(error.getCode(), new Field[]{
                    new Field("Internal Validation Error", "Validation error in the entered data")
            });
        }
        if(errorTye.startsWith("ITS")){
            return new Error(error.getCode(), new Field[]{
                    new Field("Internal System Error", "An internal error has occurred. Please try again later.")
            });
        }

        return error;
    }
}

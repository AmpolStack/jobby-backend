package com.jobby.domain.mobility;

public enum ErrorType {
    // --- Errors visible to the end user ---
    INVALID_INPUT,               // The input provided is invalid.
    USER_NOT_FOUND,              // User not found.
    NOT_FOUND,                   // Not found Entity
    AUTHENTICATION_FAILED,       // Invalid credentials.
    PERMISSION_DENIED,           // No permission for this action.
    RESOURCE_ALREADY_EXISTS,     // The resource already exists.
    VALIDATION_ERROR,            // Validation error in submitted data.
    UNSUPPORTED_OPERATION,       // Operation not supported.
    TOKEN_EXPIRED,               // Token has expired.
    TOKEN_INVALID,               // Token is invalid or malformed.
    EMPLOYEE_INACTIVE,           // Employee account is inactive.

    // --- Internal validation errors (ITN - Internal) ---
    ITN_VALIDATION_NULL,         // Internal null validation failed.
    ITN_VALIDATION_BLANK,        // Internal blank validation failed.
    ITN_VALIDATION_EMPTY,        // Internal empty validation failed.
    ITN_VALIDATION_FORMAT,       // Internal format validation failed.
    ITN_VALIDATION_RANGE,        // Internal range validation failed.
    ITN_VALIDATION_TYPE,         // Internal type validation failed.
    ITN_VALIDATION_CUSTOM,       // Internal custom validation failed.

    // --- Internal system errors (ITS - Internal System) ---
    ITS_INVALID_OPTION_PARAMETER,    // Invalid environment parameter.
    ITS_OPERATION_ERROR,             // An internal error occurred.
    ITS_INVALID_STATE,               // The system entered an invalid state.
    ITS_DB_CONNECTION_FAILED,        // Failed to connect to the database.
    ITS_EXTERNAL_SERVICE_FAILURE,    // External service call failed.
    ITS_CONFIGURATION_ERROR,         // System configuration issue.
    ITS_SERIALIZATION_ERROR,         // Failed to serialize or deserialize data.
    ITS_UNKNOWN_ERROR               // An unknown error occurred.
}

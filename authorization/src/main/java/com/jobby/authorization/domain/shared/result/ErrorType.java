package com.jobby.authorization.domain.shared.result;

public enum ErrorType {
    // --- Errors visible to the end user ---
    INVALID_INPUT,               // The input provided is invalid.
    USER_NOT_FOUND,              // User not found.
    AUTHENTICATION_FAILED,       // Invalid credentials.
    PERMISSION_DENIED,           // No permission for this action.
    RESOURCE_ALREADY_EXISTS,     // The resource already exists.
    VALIDATION_ERROR,            // Validation error in submitted data.
    UNSUPPORTED_OPERATION,       // Operation not supported.

    // --- Internal errors (not exposed directly to the user) ---
    ITN_INVALID_OPTION_PARAMETER,    // Invalid environment parameter.
    ITN_OPERATION_ERROR,    // An internal error occurred.
    ITN_INVALID_STATE,      // The system entered an invalid state.
    ITN_DB_CONNECTION_FAILED,        // Failed to connect to the database.
    ITN_EXTERNAL_SERVICE_FAILURE,    // External service call failed.
    ITN_CONFIGURATION_ERROR,         // System configuration issue.
    ITN_SERIALIZATION_ERROR,         // Failed to serialize or deserialize data.
    ITS_UNKNOWN_ERROR;               // An unknown error occurred.
}

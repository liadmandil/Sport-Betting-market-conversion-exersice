package sportbet.errors;

/**
 * ErrorCode enumerates high-level failure categories used by domain exceptions.
 */
public enum ErrorCode {
    FILE_NOT_FOUND,        // input file is missing / not a regular file / not readable
    JSON_PARSE_ERROR,      // low-level JSON/IO parsing error
    JSON_INVALID_SHAPE,    // JSON structure is not as expected (e.g., root not an array)
    VALIDATION_ERROR,      // data validation error
    CONVERSION_ERROR,      // market conversion error
    UNKNOWN_MARKET_TYPE,   // unknown market type
    UNKNOWN_SELECTION_TYPE // unknown selection type
}

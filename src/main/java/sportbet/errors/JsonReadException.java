package sportbet.io;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;

/**
 * JsonReadException represents low-level failures while reading/parsing JSON
 * (e.g., malformed JSON, encoding issues, IO errors).
 */
public class JsonReadException extends DomainException {
    public JsonReadException(String message) {
        super(ErrorCode.JSON_PARSE_ERROR, message);
    }

    public JsonReadException(String message, Throwable cause) {
        super(ErrorCode.JSON_PARSE_ERROR, message, cause);
    }
}

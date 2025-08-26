package sportbet.errors;

/**
 * FileFormatException indicates the JSON structure is not as expected,
 * e.g., root is not a JSON array, or the deserialized object graph is null.
 */
public class FileFormatException extends DomainException {
    public FileFormatException(String message) {
        super(ErrorCode.JSON_INVALID_SHAPE, message);
    }
}

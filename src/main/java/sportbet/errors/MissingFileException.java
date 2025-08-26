package sportbet.errors;

/**
 * MissingFileException indicates that the provided path does not exist,
 * is not a regular file, or is not readable.
 */
public class MissingFileException extends DomainException {
    public MissingFileException(String message) {
        super(ErrorCode.FILE_NOT_FOUND, message);
    }

    public MissingFileException(String message, Throwable cause) {
        super(ErrorCode.FILE_NOT_FOUND, message, cause);
    }
}

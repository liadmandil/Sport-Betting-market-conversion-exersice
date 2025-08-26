package sportbet.errors;

/**
 * DomainException is a base unchecked exception that carries an ErrorCode.
 * Specific exceptions should extend this class and set an appropriate code.
 */
public class DomainException extends RuntimeException {
    private final ErrorCode code;

    public DomainException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public DomainException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}

package money.exception;

/**
 * Unchecked (Runtime) base exception representing domain rule violations.
 * Unchecked exceptions inherit from {@link RuntimeException}.
 * They represent bugs or invalid program states that the caller is not forced to handle explicitly.
 */
public abstract class MoneyDomainException extends RuntimeException {
    protected MoneyDomainException(String message) {
        super(message);
    }
}

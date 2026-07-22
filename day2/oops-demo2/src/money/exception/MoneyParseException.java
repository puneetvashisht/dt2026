package money.exception;

/**
 * Checked exception representing a failure to parse raw input into money.
 * Checked exceptions inherit directly from {@link Exception}.
 * They represent recoverable errors that a caller is forced to handle (either via try-catch or throws).
 */
public class MoneyParseException extends Exception {
    public MoneyParseException(String message) {
        super(message);
    }

    public MoneyParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

package money.exception;

/**
 * Custom unchecked exception thrown when currency input is null, empty, or structurally invalid.
 */
public class InvalidCurrencyException extends MoneyDomainException {
    private final String currency;

    public InvalidCurrencyException(String currency, String reason) {
        super("Invalid currency '" + currency + "': " + reason);
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}

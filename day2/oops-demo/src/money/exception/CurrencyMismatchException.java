package money.exception;

/**
 * Custom unchecked exception thrown when an operation is performed on mismatching currencies.
 */
public class CurrencyMismatchException extends MoneyDomainException {
    private final String expectedCurrency;
    private final String actualCurrency;

    public CurrencyMismatchException(String expectedCurrency, String actualCurrency) {
        super("Currency mismatch! Cannot perform operation between " + expectedCurrency + " and " + actualCurrency);
        this.expectedCurrency = expectedCurrency;
        this.actualCurrency = actualCurrency;
    }

    public String getExpectedCurrency() {
        return expectedCurrency;
    }

    public String getActualCurrency() {
        return actualCurrency;
    }
}

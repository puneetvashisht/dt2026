package money.exception;

import java.math.BigDecimal;

/**
 * Custom unchecked exception thrown when an amount is negative.
 */
public class NegativeAmountException extends MoneyDomainException {
    private final BigDecimal value;

    public NegativeAmountException(BigDecimal value) {
        super("Invalid money amount: " + value + ". Value cannot be negative.");
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}

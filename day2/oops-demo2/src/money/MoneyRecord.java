package money;

import java.math.BigDecimal;
import money.exception.CurrencyMismatchException;
import money.exception.InvalidCurrencyException;
import money.exception.NegativeAmountException;

/**
 * A record representing Money.
 * Records are transparent carriers for immutable data, introduced in Java 14 (standard in 16).
 * They automatically generate:
 * - Private final fields (value and currency)
 * - Canonical constructor
 * - Getters (value() and currency() - note: no 'get' prefix)
 * - equals() implementation comparing all fields
 * - hashCode() implementation based on all fields
 * - toString() representation
 * 
 * We use a "compact constructor" here for validation and normalization.
 */
public record MoneyRecord(BigDecimal value, String currency) {

    /**
     * Compact Constructor.
     * It does not have a parameter list or parameter assignments (no "this.value = value;").
     * The compiler automatically runs this code at the start of the canonical constructor,
     * and then assigns the parameters to the final fields.
     */
    public MoneyRecord {
        // 1. Validation
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException(value);
        }
        if (currency == null || currency.isBlank()) {
            throw new InvalidCurrencyException(currency, "Currency cannot be null or empty");
        }

        // 2. Normalization (Data Sanitization)
        // Since records auto-generate equals() using BigDecimal.equals() (which considers scale),
        // we normalize the BigDecimal value here. This ensures that different scale representations
        // (like 10 and 10.00) are stored identically, allowing the generated equals() and hashCode()
        // to behave correctly for monetary equivalence.
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            value = BigDecimal.ZERO;
        } else {
            value = value.stripTrailingZeros();
        }
        // At the end of the compact constructor, the parameters (value, currency) 
        // are implicitly assigned to the fields: "this.value = value; this.currency = currency;"
    }

    // --- Static Factory 'of' Methods ---

    /**
     * Factory method to create a MoneyRecord with a BigDecimal value.
     */
    public static MoneyRecord of(BigDecimal value, String currency) {
        return new MoneyRecord(value, currency);
    }

    /**
     * Factory method to create a MoneyRecord with a double value.
     */
    public static MoneyRecord of(double value, String currency) {
        return new MoneyRecord(BigDecimal.valueOf(value), currency);
    }

    /**
     * Factory method to create a MoneyRecord with a String value representation.
     */
    public static MoneyRecord of(String value, String currency) {
        return new MoneyRecord(new BigDecimal(value), currency);
    }

    // --- Arithmetic Operations ---

    /**
     * Adds another MoneyRecord of the same currency to this one.
     * 
     * @param other the other MoneyRecord to add
     * @return a new MoneyRecord representing the sum
     * @throws CurrencyMismatchException if the currencies do not match
     */
    public MoneyRecord plus(MoneyRecord other) {
        if (other == null) {
            throw new NullPointerException("Cannot add null MoneyRecord");
        }
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(this.currency, other.currency);
        }
        return new MoneyRecord(this.value.add(other.value), this.currency);
    }

    /**
     * Multiplies this MoneyRecord value by a BigDecimal multiplier.
     * 
     * @param multiplier the multiplier
     * @return a new MoneyRecord representing the product
     */
    public MoneyRecord multiply(BigDecimal multiplier) {
        if (multiplier == null) {
            throw new NullPointerException("Multiplier cannot be null");
        }
        return new MoneyRecord(this.value.multiply(multiplier), this.currency);
    }

    /**
     * Multiplies this MoneyRecord value by a double multiplier.
     * 
     * @param multiplier the multiplier
     * @return a new MoneyRecord representing the product
     */
    public MoneyRecord multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }
}

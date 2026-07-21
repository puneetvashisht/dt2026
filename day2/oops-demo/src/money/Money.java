package money;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A user-defined class representing Money, showing how to implement equals() and hashCode() manually.
 */
public class Money {
    private final BigDecimal value;
    private final String currency;

    /**
     * Constructor for Money.
     * 
     * @param value    the amount of money, must not be null
     * @param currency the currency code, must not be null
     */
    public Money(BigDecimal value, String currency) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * The equals() method defines the equivalence relation on non-null object references.
     * It must be:
     * 1. Reflexive: x.equals(x) is true
     * 2. Symmetric: x.equals(y) is true iff y.equals(x) is true
     * 3. Transitive: if x.equals(y) and y.equals(z), then x.equals(z) is true
     * 4. Consistent: multiple invocations return the same result unless fields are modified
     * 5. Null-safe: x.equals(null) is false
     */
    @Override
    public boolean equals(Object o) {
        // 1. Reference check (Self-comparison)
        if (this == o) {
            return true;
        }

        // 2. Null and Type check
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // 3. Cast to the target class
        Money other = (Money) o;

        // 4. Compare fields.
        // NOTE ON BIGDECIMAL:
        // BigDecimal.equals() compares BOTH value and scale. 
        // Therefore, new BigDecimal("1.0").equals(new BigDecimal("1.00")) is FALSE.
        // For financial applications, $1.0 and $1.00 are mathematically and practically equal.
        // Hence, we use compareTo() for value comparison, and standard equals() for currency.
        return Objects.equals(this.currency, other.currency) && 
               (this.value.compareTo(other.value) == 0);
    }

    /**
     * The hashCode() contract states:
     * 1. Consistency: returns the same integer during an execution, if info used in equals is not modified.
     * 2. If two objects are equal according to equals(Object), they MUST produce the same hashCode.
     * 3. If two objects are unequal, they are NOT required to produce distinct hashCodes (though preferred to avoid collisions).
     */
    @Override
    public int hashCode() {
        // Because we used compareTo() for value comparison in equals(), we must ensure
        // that BigDecimal values with different scales (e.g., 10 and 10.00) produce the same hash code.
        // We do this by calling stripTrailingZeros() before hashing the value.
        // Note: For BigDecimal.ZERO, stripTrailingZeros() normalized it in modern Java versions.
        BigDecimal normalizedValue = (value.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : value.stripTrailingZeros();
        return Objects.hash(normalizedValue, currency);
    }

    @Override
    public String toString() {
        return value + " " + currency;
    }
}

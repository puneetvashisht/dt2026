package money;

import java.math.BigDecimal;

/**
 * Fluent Builder API for constructing immutable MoneyRecord instances.
 */
public class MoneyBuilder {
    private BigDecimal value;
    private String currency;

    // Package-private constructor so only MoneyFactory or our static builder can initialize it
    MoneyBuilder() {}

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    /**
     * Set the BigDecimal value and return the builder instance for chaining (Fluent API).
     */
    public MoneyBuilder value(BigDecimal value) {
        this.value = value;
        return this;
    }

    /**
     * Set the double value (converted to BigDecimal) and return the builder (Fluent API).
     */
    public MoneyBuilder value(double value) {
        this.value = BigDecimal.valueOf(value);
        return this;
    }

    /**
     * Set the String value (converted to BigDecimal) and return the builder (Fluent API).
     */
    public MoneyBuilder value(String valueStr) {
        if (valueStr != null) {
            this.value = new BigDecimal(valueStr.trim());
        }
        return this;
    }

    /**
     * Set the currency string and return the builder (Fluent API).
     */
    public MoneyBuilder currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Build the immutable MoneyRecord.
     * Validates that the builder has all required fields.
     */
    public MoneyRecord build() {
        if (value == null) {
            throw new IllegalStateException("Value must be specified in the builder");
        }
        if (currency == null) {
            throw new IllegalStateException("Currency must be specified in the builder");
        }
        return new MoneyRecord(value, currency);
    }
}

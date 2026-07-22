package money;

import java.math.BigDecimal;

/**
 * Sealed interface representing raw input to be processed.
 * Java 21 permits restricts the implementation to specific records, enabling exhaustive switch expressions.
 */
public sealed interface RawInput permits RawInput.Text, RawInput.Structured, RawInput.Numeric {

    /**
     * Text representation of raw input (e.g. "100.50 USD" or "EUR 25.00").
     */
    record Text(String inputStr) implements RawInput {}

    /**
     * Structured representation with BigDecimal.
     */
    record Structured(BigDecimal value, String currency) implements RawInput {}

    /**
     * Numeric representation with generic Number (e.g. double, int, float) and currency.
     */
    record Numeric(Number value, String currency) implements RawInput {}
}

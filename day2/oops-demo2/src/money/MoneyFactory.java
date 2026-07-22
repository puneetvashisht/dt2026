package money;

import java.math.BigDecimal;
import money.exception.MoneyParseException;

/**
 * MoneyFactory demonstrates Java 21 switch expressions with pattern matching for records.
 * It acts as the gateway to convert various RawInput formats into a MoneyBuilder.
 */
public class MoneyFactory {

    /**
     * Factory method that uses a Java 21 switch expression with record patterns 
     * to map raw inputs to a configured MoneyBuilder.
     * 
     * @param rawInput the raw input to process
     * @return a MoneyBuilder pre-populated with value and currency
     * @throws MoneyParseException if parsing raw text fails (checked exception)
     */
    public static MoneyBuilder createBuilder(RawInput rawInput) throws MoneyParseException {
        return switch (rawInput) {
            // Java 21 Record Pattern Matching: destructs the record on match
            case RawInput.Text(String inputStr) -> parseText(inputStr);
            
            case RawInput.Structured(BigDecimal value, String currency) -> 
                MoneyBuilder.builder().value(value).currency(currency);
                
            case RawInput.Numeric(Number value, String currency) -> 
                MoneyBuilder.builder().value(new BigDecimal(value.toString())).currency(currency);
        };
    }

    /**
     * Parses a raw text input (e.g. "100 USD" or "USD 100") and returns a builder.
     * Throws a checked exception if parsing fails.
     */
    private static MoneyBuilder parseText(String inputStr) throws MoneyParseException {
        if (inputStr == null || inputStr.isBlank()) {
            throw new MoneyParseException("Raw text input cannot be null or empty");
        }

        String[] parts = inputStr.trim().split("\\s+");
        if (parts.length != 2) {
            throw new MoneyParseException(
                "Invalid text format. Expected 'amount currency' or 'currency amount', but got: '" + inputStr + "'"
            );
        }

        String first = parts[0];
        String second = parts[1];

        // Scenario A: "100.50 USD" (First part is numeric)
        try {
            BigDecimal value = new BigDecimal(first);
            return MoneyBuilder.builder().value(value).currency(second);
        } catch (NumberFormatException e1) {
            // Scenario B: "USD 100.50" (Second part is numeric)
            try {
                BigDecimal value = new BigDecimal(second);
                return MoneyBuilder.builder().value(value).currency(first);
            } catch (NumberFormatException e2) {
                // Both failed to parse as a number
                throw new MoneyParseException(
                    "Parsing error: Neither '" + first + "' nor '" + second + "' is a valid numeric value.", 
                    e2
                );
            }
        }
    }
}

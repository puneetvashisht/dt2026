package money;

import java.math.BigDecimal;
import money.exception.*;

/**
 * Runner class to demonstrate Java 21 switch expressions, Fluent API Builder,
 * factory parsing, and custom exception handling hierarchies.
 */
public class MoneyAdvancedDemo {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     ADVANCED MONEY DEMO: BUILDERS, FACTORIES,");
        System.out.println("          JAVA 21 SWITCH & EXCEPTIONS");
        System.out.println("==================================================");

        // 1. Pipeline: Raw Input -> Factory -> Builder -> Immutable Money
        System.out.println("\n--- 1. Processing Raw Input Formats (Java 21 Pattern Matching) ---");
        
        RawInput[] inputs = {
            new RawInput.Text("250.75 USD"),
            new RawInput.Text("EUR 120.00"),
            new RawInput.Structured(new BigDecimal("500.0"), "INR"),
            new RawInput.Numeric(1000, "JPY")
        };

        for (RawInput input : inputs) {
            try {
                // RawInput -> Factory -> MoneyBuilder
                MoneyBuilder builder = MoneyFactory.createBuilder(input);
                
                // Fluent API addition/chaining demonstration
                MoneyRecord money = builder.build();
                
                System.out.printf("Successfully parsed input type [%s] into: %s (Value: %s, Currency: %s)%n",
                        input.getClass().getSimpleName(), money, money.value(), money.currency());
            } catch (MoneyParseException e) {
                System.err.println("Failed to parse raw input: " + e.getMessage());
            }
        }

        // 2. Fluent Builder Demo
        System.out.println("\n--- 2. Fluent Builder API Demo ---");
        MoneyRecord customMoney = MoneyBuilder.builder()
                .value("1500.99")
                .currency("AUD")
                .build();
        System.out.println("Created custom money using Fluent Builder: " + customMoney);


        // 3. Exception Handling Demonstration
        System.out.println("\n--- 3. Exception Handling Hierarchies Demo ---");
        
        // Scenario A: Recoverable/Checked Exception (parsing bad text)
        System.out.println("\n[Checked Exception Case] Parsing invalid raw text:");
        RawInput badInput = new RawInput.Text("InvalidTextNoValue");
        try {
            MoneyFactory.createBuilder(badInput);
        } catch (MoneyParseException e) {
            System.out.println("-> Caught CHECKED exception MoneyParseException: " + e.getMessage());
            System.out.println("   Root Cause: " + e.getCause());
            System.out.println("   (Checked exceptions represent recoverable inputs - we must handle them!)");
        }

        // Scenario B: Unchecked Exception (Negative amount)
        System.out.println("\n[Unchecked Domain Exception Case 1] Invalid business rule (Negative value):");
        try {
            MoneyRecord.of(-50.0, "USD");
        } catch (NegativeAmountException e) {
            System.out.println("-> Caught UNCHECKED custom exception NegativeAmountException: " + e.getMessage());
            System.out.println("   Failed value recorded in exception: " + e.getValue());
        }

        // Scenario C: Unchecked Exception (Blank currency)
        System.out.println("\n[Unchecked Domain Exception Case 2] Invalid currency parameter:");
        try {
            MoneyRecord.of(100, "   ");
        } catch (InvalidCurrencyException e) {
            System.out.println("-> Caught UNCHECKED custom exception InvalidCurrencyException: " + e.getMessage());
            System.out.println("   Failed currency recorded in exception: '" + e.getCurrency() + "'");
        }

        // Scenario D: Unchecked Exception (Currency Mismatch)
        System.out.println("\n[Unchecked Domain Exception Case 3] Math operation mismatch (USD + EUR):");
        try {
            MoneyRecord usd = MoneyRecord.of(10.0, "USD");
            MoneyRecord eur = MoneyRecord.of(20.0, "EUR");
            usd.plus(eur);
        } catch (CurrencyMismatchException e) {
            System.out.println("-> Caught UNCHECKED custom exception CurrencyMismatchException: " + e.getMessage());
            System.out.println("   Expected: " + e.getExpectedCurrency() + ", Actual: " + e.getActualCurrency());
        }

        // 4. Exception Hierarchy Summary
        System.out.println("\n==================================================");
        System.out.println("           EXCEPTION HIERARCHY SUMMARY");
        System.out.println("==================================================");
        System.out.println("Throwable");
        System.out.println(" ├── Exception (Checked Exceptions - must declare/catch)");
        System.out.println(" │    └── MoneyParseException (Thrown by factory parser)");
        System.out.println(" └── RuntimeException (Unchecked Exceptions - represent bugs/rules)");
        System.out.println("      └── MoneyDomainException (Base custom unchecked)");
        System.out.println("           ├── NegativeAmountException (Business rule violation)");
        System.out.println("           ├── InvalidCurrencyException (Bad initialization)");
        System.out.println("           └── CurrencyMismatchException (Operation violation)");
        System.out.println("==================================================");
    }
}

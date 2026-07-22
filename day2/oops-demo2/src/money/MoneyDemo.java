package money;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Demo class to run and demonstrate the behavior of equals(), hashCode() 
 * and Java Records using Money and MoneyRecord.
 */
public class MoneyDemo {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   DEMONSTRATING EQUALS & HASHCODE WITH MONEY");
        System.out.println("==================================================");

        // 1. The BigDecimal Scale Pitfall
        System.out.println("\n--- 1. BigDecimal Scale Pitfall ---");
        BigDecimal val1 = new BigDecimal("10.0");
        BigDecimal val2 = new BigDecimal("10.00");
        System.out.println("val1 = " + val1 + " (scale: " + val1.scale() + ")");
        System.out.println("val2 = " + val2 + " (scale: " + val2.scale() + ")");
        System.out.println("val1.equals(val2)      => " + val1.equals(val2) + "  <-- (Because scales are different)");
        System.out.println("val1.compareTo(val2)==0 => " + (val1.compareTo(val2) == 0) + "   <-- (Compares mathematical value)");

        // 2. Testing Custom Money Class (with compareTo-based equals)
        System.out.println("\n--- 2. Custom Money Class (Manual equals & hashCode) ---");
        Money m1 = new Money(new BigDecimal("10.0"), "USD");
        Money m2 = new Money(new BigDecimal("10.00"), "USD");
        Money m3 = new Money(new BigDecimal("10.0"), "EUR");

        System.out.println("m1 = " + m1);
        System.out.println("m2 = " + m2);
        System.out.println("m1.equals(m2) => " + m1.equals(m2) + " (Equal despite scale difference!)");
        System.out.println("m1.equals(m3) => " + m1.equals(m3));

        System.out.println("m1.hashCode() = " + m1.hashCode());
        System.out.println("m2.hashCode() = " + m2.hashCode());
        System.out.println("m1.hashCode() == m2.hashCode() => " + (m1.hashCode() == m2.hashCode()));

        // Put in a HashSet to check if it respects equals & hashCode
        Set<Money> moneySet = new HashSet<>();
        moneySet.add(m1);
        moneySet.add(m2); // Duplicate value (mathematically)
        moneySet.add(m3); // Different currency
        System.out.println("Set elements (Custom Money): " + moneySet);
        System.out.println("Set size (should be 2, not 3): " + moneySet.size());

        // 3. Testing MoneyRecord (with Compact Constructor Normalization)
        System.out.println("\n--- 3. MoneyRecord (Java Record with Compact Constructor) ---");
        MoneyRecord r1 = new MoneyRecord(new BigDecimal("10.0"), "USD");
        MoneyRecord r2 = new MoneyRecord(new BigDecimal("10.00"), "USD");
        MoneyRecord r3 = new MoneyRecord(new BigDecimal("10.0"), "EUR");

        System.out.println("r1 = " + r1);
        System.out.println("r2 = " + r2);
        System.out.println("r1.equals(r2) => " + r1.equals(r2) + " (Equal thanks to normalization!)");
        System.out.println("r1.equals(r3) => " + r1.equals(r3));

        System.out.println("r1.hashCode() = " + r1.hashCode());
        System.out.println("r2.hashCode() = " + r2.hashCode());
        System.out.println("r1.hashCode() == r2.hashCode() => " + (r1.hashCode() == r2.hashCode()));

        // Put in a HashSet
        Set<MoneyRecord> recordSet = new HashSet<>();
        recordSet.add(r1);
        recordSet.add(r2);
        recordSet.add(r3);
        System.out.println("Set elements (MoneyRecord): " + recordSet);
        System.out.println("Set size (should be 2, not 3): " + recordSet.size());

        // Demonstrate record getters (accessor methods without 'get' prefix)
        System.out.println("\n--- 4. Record Getters/Accessors ---");
        System.out.println("Record r1 value() accessor: " + r1.value());
        System.out.println("Record r1 currency() accessor: " + r1.currency());

        // Demonstrate compact constructor validation
        System.out.println("\n--- 5. Compact Constructor Validation ---");
        try {
            new MoneyRecord(null, "USD");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught for null value: " + e.getMessage());
        }
        try {
            new MoneyRecord(new BigDecimal("5.0"), "   ");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught for blank currency: " + e.getMessage());
        }
        try {
            new MoneyRecord(new BigDecimal("-10.00"), "USD");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught for negative value: " + e.getMessage());
        }

        // 6. Demonstrate Static Factory 'of' Builders
        System.out.println("\n--- 6. Static Factory 'of' Builders ---");
        MoneyRecord b1 = MoneyRecord.of(BigDecimal.valueOf(100.50), "USD");
        MoneyRecord b2 = MoneyRecord.of(50.25, "USD");
        MoneyRecord b3 = MoneyRecord.of("200.75", "USD");
        System.out.println("b1 (from BigDecimal): " + b1);
        System.out.println("b2 (from double):     " + b2);
        System.out.println("b3 (from String):     " + b3);

        // 7. Demonstrate Arithmetic Operations
        System.out.println("\n--- 7. Arithmetic Operations ---");
        MoneyRecord sum = b1.plus(b2);
        System.out.println("Sum: " + b1 +    " + " + b2 + " = " + sum);

        MoneyRecord productDouble = b2.multiply(2.0);
        MoneyRecord productBigDecimal = b2.multiply(new BigDecimal("3"));
        System.out.println("Product (double):     " + b2 + " * 2.0 = " + productDouble);
        System.out.println("Product (BigDecimal): " + b2 + " * 3   = " + productBigDecimal);

        try {
            System.out.println("Attempting currency mismatch addition (USD + EUR)...");
            b1.plus(r3); // b1 is USD, r3 is EUR
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught for currency mismatch: " + e.getMessage());
        }

        try {
            System.out.println("Attempting multiplication resulting in negative value (50.25 * -1)...");
            b2.multiply(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught for negative product: " + e.getMessage());
        }
    }
}

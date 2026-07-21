package enumdemo;

import java.math.BigDecimal;

/**
 * Runner class to demonstrate Java Enum constructors, properties,
 * and constant-specific abstract method overrides.
 */
public class EnumDemoRunner {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      DEMONSTRATING ENUMS WITH BEHAVIOUR");
        System.out.println("==================================================");

        // 1. Inspecting Enum Constants & Constructors
        System.out.println("\n--- 1. Inspecting Tiers & Properties ---");
        for (FinancialTolerance tier : FinancialTolerance.values()) {
            System.out.printf("Tier: %s | Basis Points: %d bps | Decimal Multiplier: %s%n",
                    tier.name(), tier.getBasisPoints(), tier.getMultiplier().toPlainString());
        }

        // 2. Tolerance calculations
        System.out.println("\n--- 2. Checking Transaction Tolerances ---");
        
        BigDecimal expectedValue = new BigDecimal("10000.00"); // $10,000 transaction
        
        // Let's test two different actual values
        BigDecimal actualCloseValue = new BigDecimal("10005.50"); // Diff: $5.50 (5.5 bps)
        BigDecimal actualFarValue = new BigDecimal("10035.00");   // Diff: $35.00 (35 bps)

        System.out.println("Expected Transaction Value: $" + expectedValue);
        System.out.println("Actual Value A: $" + actualCloseValue + " (Diff: $5.50)");
        System.out.println("Actual Value B: $" + actualFarValue + " (Diff: $35.00)");

        System.out.println("\nEvaluating checks against tiers:");
        for (FinancialTolerance tier : FinancialTolerance.values()) {
            BigDecimal maxAllowedDiff = tier.calculateToleranceAmount(expectedValue);
            System.out.printf("%s (Max allowed diff: $%s):%n", tier, maxAllowedDiff.toPlainString());
            
            boolean closeWithin = tier.isWithinTolerance(expectedValue, actualCloseValue);
            boolean farWithin = tier.isWithinTolerance(expectedValue, actualFarValue);
            
            System.out.printf("  -> Actual Value A ($5.50 diff) within tolerance? %b%n", closeWithin);
            System.out.printf("  -> Actual Value B ($35.00 diff) within tolerance? %b%n", farWithin);
        }

        // 3. Constant-Specific Behaviour (Polymorphic Enum Methods)
        System.out.println("\n--- 3. Risk-Adjusted Penalties (Enum Abstract Methods) ---");
        BigDecimal principal = new BigDecimal("500000.00"); // $500,000 portfolio
        
        System.out.println("Calculating penalties for principal: $" + principal);
        for (FinancialTolerance tier : FinancialTolerance.values()) {
            BigDecimal penalty = tier.calculateRiskAdjustedPenalty(principal);
            System.out.printf("  -> %-12s Penalty: $%s%n", tier.name(), penalty.toPlainString());
        }
    }
}

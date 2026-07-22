package enumdemo;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * FinancialTolerance is an Enum with behaviour.
 * It represents different tolerance tiers in financial calculations,
 * defined in basis points (1 bps = 0.01% = 0.0001).
 * 
 * Enums in Java are full-fledged classes that can have:
 * - Instance fields
 * - Constructors (always private/package-private)
 * - Concrete methods
 * - Abstract methods overridden by specific constants (constant-specific class bodies)
 */
public enum FinancialTolerance {

    // Enum constants with specific values in Basis Points (bps)
    // Low Risk: 10 bps (0.1%)
    LOW_RISK(10) {
        @Override
        public BigDecimal calculateRiskAdjustedPenalty(BigDecimal principal) {
            // Low risk has a minimal flat penalty
            return principal.multiply(new BigDecimal("0.0005")); // 5 bps penalty
        }
    },

    // Medium Risk: 50 bps (0.5%)
    MEDIUM_RISK(50) {
        @Override
        public BigDecimal calculateRiskAdjustedPenalty(BigDecimal principal) {
            return principal.multiply(new BigDecimal("0.0025")); // 25 bps penalty
        }
    },

    // High Risk: 250 bps (2.5%)
    HIGH_RISK(250) {
        @Override
        public BigDecimal calculateRiskAdjustedPenalty(BigDecimal principal) {
            return principal.multiply(new BigDecimal("0.0150")); // 150 bps penalty
        }
    };

    // Constant: 1 Basis Point = 0.0001 (0.01%)
    private static final BigDecimal ONE_BASIS_POINT_VALUE = new BigDecimal("0.0001");

    private final int basisPoints;
    private final BigDecimal multiplier;

    /**
     * Enum Constructor.
     * Constructors in enums are implicitly private.
     * They are executed when the enum constants are initialized at class loading time.
     */
    FinancialTolerance(int basisPoints) {
        this.basisPoints = basisPoints;
        // Convert basis points to a decimal multiplier: bps * 0.0001
        this.multiplier = BigDecimal.valueOf(basisPoints).multiply(new BigDecimal("0.0001"));
    }

    public int getBasisPoints() {
        return basisPoints;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    // --- Concrete Behaviour Methods ---

    /**
     * Calculates the absolute tolerance amount for a given principal.
     */
    public BigDecimal calculateToleranceAmount(BigDecimal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal cannot be null");
        }
        return principal.multiply(this.multiplier, MathContext.DECIMAL128);
    }

    /**
     * Verifies if the absolute difference between two financial values is within the tolerance range.
     * 
     * @param expected the expected value
     * @param actual   the actual value
     * @return true if the difference is within the allowed tolerance
     */
    public boolean isWithinTolerance(BigDecimal expected, BigDecimal actual) {
        if (expected == null || actual == null) {
            throw new IllegalArgumentException("Values to compare cannot be null");
        }
        
        // Difference = |expected - actual|
        BigDecimal difference = expected.subtract(actual).abs();
        
        // Allowed Tolerance limit based on expected amount
        BigDecimal allowedTolerance = calculateToleranceAmount(expected);
        
        // Difference <= Allowed Tolerance
        return difference.compareTo(allowedTolerance) <= 0;
    }

    // --- Abstract Behaviour Method ---
    // Every enum constant MUST override this method to provide constant-specific behavior.
    public abstract BigDecimal calculateRiskAdjustedPenalty(BigDecimal principal);

    @Override
    public String toString() {
        return name() + " (" + basisPoints + " bps / " + multiplier.multiply(new BigDecimal("100")) + "%)";
    }
}

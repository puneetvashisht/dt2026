package lambdasdemo;

// A custom functional interface (has exactly one abstract method)
@FunctionalInterface
interface DiscountCalculator {
    double calculate(double price);
}

// Approach 1: Traditional Concrete Subclass
class MemberDiscount implements DiscountCalculator {
    @Override
    public double calculate(double price) {
        return price * 0.90; // 10% discount for members
    }
}

public class LambdaIntro {
    public static void main(String[] args) {
        double originalPrice = 100.0;

        System.out.println("==========================================================");
        System.out.println("     EVOLUTION OF CODE CONCISENESS (SUBCLASS -> LAMBDA)   ");
        System.out.println("==========================================================\n");

        // ------------------------------------------------------------
        // APPROACH 1: Concrete Subclass (Requires declaring a separate class)
        // ------------------------------------------------------------
        DiscountCalculator memberDiscount = new MemberDiscount();
        System.out.printf("1. Concrete Subclass Price : $%.2f\n", memberDiscount.calculate(originalPrice));

        // ------------------------------------------------------------
        // APPROACH 2: Anonymous Inner Class (No separate class declaration, but high boilerplate)
        // ------------------------------------------------------------
        DiscountCalculator studentDiscount = new DiscountCalculator() {
            @Override
            public double calculate(double price) {
                return price * 0.80; // 20% discount for students
            }
        };
        System.out.printf("2. Anonymous Inner Class   : $%.2f\n", studentDiscount.calculate(originalPrice));

        // ------------------------------------------------------------
        // APPROACH 3: Lambda Expression (Concise, focus purely on behavior)
        // ------------------------------------------------------------
        // compiler infers type of price parameter and return type from functional interface signature
        DiscountCalculator holidayDiscount = price -> price * 0.70; // 30% discount for holiday
        System.out.printf("3. Lambda Expression       : $%.2f\n", holidayDiscount.calculate(originalPrice));

        System.out.println("\n----------------------------------------------------------");
        System.out.println("Why Functional Interfaces & Lambdas?");
        System.out.println("----------------------------------------------------------");
        System.out.println("- Prior to Java 8, passing behavior (code blocks) required wrapping it inside objects.");
        System.out.println("- Functional Interfaces act as the target types for lambda expressions.");
        System.out.println("- Lambdas allow us to treat functionality as a method argument, or code as data.");
        System.out.println("- They eliminate boilerplate syntax (class declaration, method signature, return keyword where simple).");
        System.out.println("==========================================================");
    }
}

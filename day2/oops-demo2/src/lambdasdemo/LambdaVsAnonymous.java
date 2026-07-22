package lambdasdemo;

public class LambdaVsAnonymous {

    private final String scopeIdentifier = "Outer Class Field";

    public void runComparison() {
        System.out.println("==========================================================");
        System.out.println("   DEMONSTRATION: LAMBDAS ARE NOT ANONYMOUS/ABSTRACT CLASSES");
        System.out.println("==========================================================\n");

        int localVal = 100; // Local variable

        // ------------------------------------------------------------
        // DIFFERENCE 1: Lexical Scope of 'this'
        // ------------------------------------------------------------
        System.out.println("--- 1. Lexical Scope (Value of 'this') ---");

        // Using Anonymous Class
        Runnable anonymousRunner = new Runnable() {
            private final String scopeIdentifier = "Anonymous Inner Class Instance";
            @Override
            public void run() {
                // 'this' refers to the anonymous class instance
                System.out.println("   [Anonymous Class] this points to: " + this.getClass().getName());
                System.out.println("   [Anonymous Class] scopeIdentifier: " + this.scopeIdentifier);
            }
        };
        anonymousRunner.run();

        // Using Lambda
        Runnable lambdaRunner = () -> {
            // 'this' refers to the surrounding LambdaVsAnonymous instance
            System.out.println("   [Lambda]          this points to: " + this.getClass().getName());
            System.out.println("   [Lambda]          scopeIdentifier: " + this.scopeIdentifier);
        };
        lambdaRunner.run();
        System.out.println();

        // ------------------------------------------------------------
        // DIFFERENCE 2: Variable Shadowing
        // ------------------------------------------------------------
        System.out.println("--- 2. Variable Shadowing ---");
        System.out.println("   [Anonymous Class] Can declare parameters/variables that shadow local scope.");
        
        @SuppressWarnings("unused")
        Runnable shadowedAnonymous = new Runnable() {
            @Override
            public void run() {
                // Anonymous class can declare a variable with the same name as the outer scope's local variable
                int localVal = 200; // compiles successfully (shadowing outer localVal)
                System.out.println("   [Anonymous Class] localVal (shadowed): " + localVal);
            }
        };
        shadowedAnonymous.run();

        System.out.println("   [Lambda]          Cannot shadow local variables in enclosing scope.");
        System.out.println("                     Writing: 'Consumer<Integer> l = localVal -> ...' causes a compiler error");
        System.out.println("                     because lambda scope is lexical and matches enclosing method scope.");
        System.out.println();

        // ------------------------------------------------------------
        // DIFFERENCE 3: Under the Hood Compilation (invokedynamic)
        // ------------------------------------------------------------
        System.out.println("--- 3. Under the Hood Compilation ---");
        System.out.println("   - Anonymous classes compile to a physical file named 'LambdaVsAnonymous$1.class'.");
        System.out.println("   - Lambdas DO NOT generate separate class files. Instead, the Java compiler");
        System.out.println("     inserts an 'invokedynamic' call which resolves at runtime to a private method");
        System.out.println("     inside this class. This avoids metadata bloat and speeds up startup.");
        System.out.println("==========================================================");
    }

    public static void main(String[] args) {
        new LambdaVsAnonymous().runComparison();
    }
}

package stringdemo.main;

// Package imports: Must import classes from other packages to use them
import stringdemo.processor.StringProcessor;

/**
 * StringDemoRunner executes the performance tests and demonstrates 
 * access modifier compile restrictions between packages.
 */
public class StringDemoRunner {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   STRING VS STRINGBUILDER: MEMORY & PERFORMANCE");
        System.out.println("==================================================");

        StringProcessor processor = new StringProcessor();

        // --- 1. Demonstrating Access Modifiers & Compilation Rules ---
        System.out.println("\n--- 1. Demonstrating Access Modifiers & Packages ---");
        
        // A. Public methods are accessible anywhere
        System.out.println("Calling public method: processor.concatenateWithStringBuilder(5)...");
        String testOutput = processor.concatenateWithStringBuilder(5);
        System.out.println("Result: " + testOutput);

        /*
         * UNCOMMENTING ANY OF THE CODE BLOCKS BELOW WILL CAUSE A COMPILATION ERROR.
         * They demonstrate how packages and access modifiers enforce encapsulation:
         */

        // B. PRIVATE ACCESS CHECK:
        // System.out.println(processor.contentToAppend); 
        // ERROR: contentToAppend has private access in stringdemo.processor.StringProcessor

        // C. PROTECTED ACCESS CHECK:
        // System.out.println(processor.getContent());
        // ERROR: getContent() has protected access in stringdemo.processor.StringProcessor
        // (Note: Since StringDemoRunner is in package 'stringdemo.main' and NOT a subclass 
        // of StringProcessor, it cannot access protected members).

        // D. PACKAGE-PRIVATE (DEFAULT) ACCESS CHECK:
        // processor.logIteration(100, 1000);
        // ERROR: logIteration(int,int) is not public in StringProcessor; 
        // cannot be accessed from outside package.

        System.out.println("Access modifier rules enforced successfully! (Check comments in source code)");


        // --- 2. String Concatenation vs StringBuilder Performance & Memory Comparison ---
        System.out.println("\n--- 2. Performance Comparison (50,000 iterations) ---");
        
        int iterations = 50000;
        
        // Force garbage collection to clean up heap before starting tests
        System.gc();
        long beforeStringBuilderMemory = getUsedMemory();
        long startTime = System.currentTimeMillis();
        
        // Execute StringBuilder
        String sbResult = processor.concatenateWithStringBuilder(iterations);
        long endTime = System.currentTimeMillis();
        long afterStringBuilderMemory = getUsedMemory();
        long sbTime = endTime - startTime;
        long sbMemoryUsed = Math.max(0, afterStringBuilderMemory - beforeStringBuilderMemory);
        
        System.out.printf("StringBuilder: %d ms | Est. Heap Allocated: %.2f KB%n", 
                sbTime, sbMemoryUsed / 1024.0);

        // Force GC again before the String run
        System.gc();
        try {
            Thread.sleep(100); // Give GC a moment
        } catch (InterruptedException ignored) {}

        long beforeStringMemory = getUsedMemory();
        startTime = System.currentTimeMillis();
        
        // Execute String + (Note: 50,000 is small enough to not OOM instantly, but will be slow)
        String stringResult = processor.concatenateWithString(iterations);
        endTime = System.currentTimeMillis();
        long afterStringMemory = getUsedMemory();
        long stringTime = endTime - startTime;
        long stringMemoryUsed = Math.max(0, afterStringMemory - beforeStringMemory);

        System.out.printf("String (+):    %d ms | Est. Heap Allocated: %.2f KB%n", 
                stringTime, stringMemoryUsed / 1024.0);

        System.out.println("\n--- 3. Analysis ---");
        double speedup = (double) stringTime / Math.max(1, sbTime);
        System.out.printf("StringBuilder was %.1fx faster than standard String concatenation!%n", speedup);
        System.out.println("If we increased iterations to 500,000, standard String concatenation");
        System.out.println("would trigger thousands of Garbage Collection cycles, resulting in");
        System.out.println("extremely high CPU thrashing, and eventually OutOfMemoryError.");
    }

    /**
     * Helper to get currently used memory.
     */
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}

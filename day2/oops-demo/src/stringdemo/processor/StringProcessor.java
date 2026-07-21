package stringdemo.processor;

/**
 * StringProcessor provides methods to process string concatenations.
 * It demonstrates packages, import statements, and access modifiers.
 */
public class StringProcessor {

    // 1. PRIVATE field: Accessible ONLY within this class.
    // Encapsulates internal configuration from other classes.
    private final String contentToAppend = "Java2026";

    // 2. PROTECTED method: Accessible within the same package (stringdemo.processor)
    // AND by subclasses in other packages.
    protected String getContent() {
        return contentToAppend;
    }

    // 3. PACKAGE-PRIVATE (Default, no modifier) method:
    // Accessible ONLY within classes in the same package (stringdemo.processor).
    // Attempts to call this from outside this package will fail to compile.
    void logIteration(int current, int total) {
        if (current % 10000 == 0) {
            System.out.println("Processing iteration " + current + " of " + total);
        }
    }

    // 4. PUBLIC methods: Accessible from ANY class in any package.
    // These define the public API of our processor.

    /**
     * Inefficiently concatenates strings in a loop using the '+' operator.
     * This creates many intermediate String and StringBuilder objects in the heap.
     */
    public String concatenateWithString(int iterations) {
        String result = "";
        for (int i = 0; i < iterations; i++) {
            // Under the hood, this translates to:
            // result = new StringBuilder().append(result).append(contentToAppend).toString();
            // This instantiates a new StringBuilder and a new String on EVERY iteration!
            result += contentToAppend;

            // Call package-private logging method (permitted because we are in the same class)
            logIteration(i, iterations);
        }
        return result;
    }

    /**
     * Efficiently concatenates strings in a loop using a single StringBuilder instance.
     * Modifies the internal buffer in-place without creating garbage objects.
     */
    public String concatenateWithStringBuilder(int iterations) {
        // Creates a single mutable character buffer
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            // Appends in-place to the same buffer, resizing only when capacity is exceeded
            sb.append(contentToAppend);

            logIteration(i, iterations);
        }
        // Creates exactly one final String object at the end
        return sb.toString();
    }
}

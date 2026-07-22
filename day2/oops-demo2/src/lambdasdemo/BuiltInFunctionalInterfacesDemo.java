package lambdasdemo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltInFunctionalInterfacesDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("    BUILT-IN FUNCTIONAL INTERFACES & STREAMS API DEMO     ");
        System.out.println("==========================================================\n");

        // 1. Predicate<T> -> Represents a boolean-valued function of one argument.
        // Signature: boolean test(T t)
        System.out.println("--- 1. Predicate<T> ---");
        Predicate<String> isLongWord = word -> word.length() > 5;
        System.out.println("   Is 'Java' > 5 chars?     : " + isLongWord.test("Java"));
        System.out.println("   Is 'Stream' > 5 chars?   : " + isLongWord.test("Stream"));
        System.out.println();

        // 2. Function<T, R> -> Transforms input of type T into output of type R.
        // Signature: R apply(T t)
        System.out.println("--- 2. Function<T, R> ---");
        Function<String, Integer> wordLength = word -> word.length();
        System.out.println("   Length of 'Functional': " + wordLength.apply("Functional"));
        System.out.println();

        // 3. Consumer<T> -> Accepts a single input argument and returns no result.
        // Signature: void accept(T t)
        System.out.println("--- 3. Consumer<T> ---");
        Consumer<String> printer = message -> System.out.println("   [Consumer Output]: " + message);
        printer.accept("Hello, Lambda!");
        System.out.println();

        // 4. Supplier<T> -> Represents a supplier of results (no input arguments).
        // Signature: T get()
        System.out.println("--- 4. Supplier<T> ---");
        Supplier<Integer> randomNumberSupplier = () -> new Random().nextInt(100);
        System.out.println("   Random Number from Supplier: " + randomNumberSupplier.get());
        System.out.println();

        // 5. BiFunction<T, U, R> -> Takes two arguments and returns a result.
        // Signature: R apply(T t, U u)
        System.out.println("--- 5. BiFunction<T, U, R> ---");
        BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;
        System.out.println("   Sum of 15 + 27 from BiFunction: " + adder.apply(15, 27));
        System.out.println();

        // ------------------------------------------------------------
        // INTEGRATION WITH STREAMS API
        // ------------------------------------------------------------
        System.out.println("----------------------------------------------------------");
        System.out.println("How they map to Streams API operations");
        System.out.println("----------------------------------------------------------");
        
        List<String> frameworkNames = Arrays.asList("Spring", "Hibernate", "Quarkus", "Micronaut", "Struts");
        System.out.println("Input list: " + frameworkNames);
        System.out.println();

        System.out.println("Pipeline Flow:");
        System.out.println("   frameworkNames.stream()");
        System.out.println("       .filter(name -> name.startsWith(\"M\") || name.startsWith(\"Q\")) <-- Accepts Predicate");
        System.out.println("       .map(name -> name.toUpperCase())                              <-- Accepts Function");
        System.out.println("       .forEach(name -> System.out.println(\"        -> \" + name));    <-- Accepts Consumer");
        System.out.println();
        System.out.println("Execution Results:");

        // Explicitly mapping functional interfaces to variables to show direct application in streams:
        Predicate<String> startsWithMOrQ = name -> name.startsWith("M") || name.startsWith("Q");
        Function<String, String> upperCaseConverter = String::toUpperCase;
        Consumer<String> itemPrinter = name -> System.out.println("      - Processed item: " + name);

        frameworkNames.stream()
            .filter(startsWithMOrQ)       // passing the Predicate variable
            .map(upperCaseConverter)     // passing the Function variable
            .forEach(itemPrinter);       // passing the Consumer variable

        System.out.println("==========================================================");
    }
}

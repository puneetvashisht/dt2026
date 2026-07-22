package paralleldemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureDemo {

    // Simulating mock database/API of stock prices
    private static final Map<String, Double> PRICE_DATABASE = new HashMap<>();
    static {
        PRICE_DATABASE.put("AAPL", 152.50);
        PRICE_DATABASE.put("MSFT", 260.00);
        PRICE_DATABASE.put("TSLA", 725.00);
        PRICE_DATABASE.put("GOOG", 2800.00);
        PRICE_DATABASE.put("AMZN", 3300.00);
    }

    // Mock service to fetch price with artificial delay
    private static double fetchPriceFromAPI(String symbol) {
        Random rand = new Random();
        int delay = 500 + rand.nextInt(1000); // Random delay between 500ms and 1500ms
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return PRICE_DATABASE.getOrDefault(symbol, 0.0);
    }

    public static void main(String[] args) {
        List<String> symbols = Arrays.asList("AAPL", "MSFT", "TSLA", "GOOG", "AMZN");
        double usdToEurRate = 0.92;

        System.out.println("==========================================================");
        System.out.println("     ASYNC PARALLELISM WITH CompletableFuture DEMO       ");
        System.out.println("==========================================================\n");

        System.out.println("Starting parallel tasks for: " + symbols);
        System.out.println("Each task runs in the ForkJoinPool common pool asynchronously.\n");

        long startTime = System.currentTimeMillis();

        // 1. Trigger Async execution for each symbol (supplyAsync) & chain transformations (thenApply)
        List<CompletableFuture<String>> futures = symbols.stream()
            .map(symbol -> {
                // Task 1: Fetch raw price asynchronously (Runs in ForkJoinPool)
                CompletableFuture<Double> fetchFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.printf("   [SupplyAsync] Started fetching price for %-4s (Thread: %s)\n", 
                            symbol, Thread.currentThread().getName());
                    double price = fetchPriceFromAPI(symbol);
                    System.out.printf("   [SupplyAsync] Completed fetch for %-4s: $%.2f (Thread: %s)\n", 
                            symbol, price, Thread.currentThread().getName());
                    return price;
                });

                // Task 2: Apply currency conversion on the result of the fetch task (thenApply)
                CompletableFuture<String> convertedFuture = fetchFuture.thenApply(price -> {
                    double eurPrice = price * usdToEurRate;
                    System.out.printf("   [ThenApply]   Converted %-4s: $%.2f USD -> €%.2f EUR (Thread: %s)\n", 
                            symbol, price, eurPrice, Thread.currentThread().getName());
                    return String.format("%-4s: €%.2f EUR (USD: $%.2f)", symbol, eurPrice, price);
                });

                return convertedFuture;
            })
            .collect(Collectors.toList());

        // 2. Combine all futures into a single future using CompletableFuture.allOf
        // This future will complete only when all individual futures inside the list have completed.
        System.out.println("\nRegistering CompletableFuture.allOf to orchestrate execution...");
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        // 3. Chain thenApply on the allOfFuture to collect and aggregate the results
        // Since allOf completed, calling .join() on the individual futures is non-blocking.
        CompletableFuture<List<String>> aggregatedResultsFuture = allOfFuture.thenApply(v -> {
            System.out.printf("\n   [AllOf -> ThenApply] All tasks complete! Aggregating results. (Thread: %s)\n", 
                    Thread.currentThread().getName());
            return futures.stream()
                .map(CompletableFuture::join) // safe & non-blocking because allOf is done
                .collect(Collectors.toList());
        });

        // 4. block main thread to get results and measure total time taken
        System.out.println("Main thread is waiting for execution to finish (using join)...");
        List<String> finalResults = aggregatedResultsFuture.join();

        long endTime = System.currentTimeMillis();

        System.out.println("\n--- FINAL RESULTS ---");
        finalResults.forEach(res -> System.out.println("   " + res));

        System.out.println();
        System.out.printf("Total Execution Time: %d ms\n", (endTime - startTime));
        System.out.println("(Notice that fetching 5 symbols took ~1 to 1.5 seconds in total,");
        System.out.println(" rather than 5+ seconds, proving parallel execution.)");
        System.out.println("==========================================================");
    }
}

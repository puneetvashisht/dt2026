package financedemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FinanceDemo {

    // Helper class to accumulate metrics per symbol in a single pass
    public static class PositionTracker {
        private int netPosition = 0;
        private double netCashFlow = 0;       // Cash flow: Negative for BUY, Positive for SELL
        private double totalTradeValue = 0;   // Sum of (Price * Qty)
        private long totalTradeQuantity = 0;  // Sum of Qty

        // Accumulates a trade transaction
        public void accumulate(Trade t) {
            double tradeVal = t.getPrice() * t.getQuantity();
            totalTradeValue += tradeVal;
            totalTradeQuantity += t.getQuantity();

            if ("BUY".equalsIgnoreCase(t.getSide())) {
                netPosition += t.getQuantity();
                netCashFlow -= tradeVal; // Cash goes out
            } else if ("SELL".equalsIgnoreCase(t.getSide())) {
                netPosition -= t.getQuantity();
                netCashFlow += tradeVal; // Cash comes in
            }
        }

        // Combines state with another tracker (needed for parallel stream aggregation)
        public PositionTracker combine(PositionTracker other) {
            PositionTracker combined = new PositionTracker();
            combined.netPosition = this.netPosition + other.netPosition;
            combined.netCashFlow = this.netCashFlow + other.netCashFlow;
            combined.totalTradeValue = this.totalTradeValue + other.totalTradeValue;
            combined.totalTradeQuantity = this.totalTradeQuantity + other.totalTradeQuantity;
            return combined;
        }

        // VWAP = Sum(Price * Qty) / Sum(Qty)
        public double getVwap() {
            return totalTradeQuantity == 0 ? 0.0 : totalTradeValue / totalTradeQuantity;
        }

        public int getNetPosition() {
            return netPosition;
        }

        public double getNetCashFlow() {
            return netCashFlow;
        }

        // Realized + Unrealized P&L = CashFlow + (Net Position * Current Market Price)
        public double calculatePL(double currentMarketPrice) {
            return netCashFlow + (netPosition * currentMarketPrice);
        }
    }

    public static void main(String[] args) {
        // 1. Setup sample trade list
        List<Trade> trades = Arrays.asList(
            new Trade("AAPL", 150.00, 100, "BUY"),
            new Trade("MSFT", 250.00, 50, "BUY"),
            new Trade("AAPL", 155.00, 50, "BUY"),
            new Trade("AAPL", 160.00, 80, "SELL"), // Partially realize profit
            new Trade("TSLA", 700.00, 10, "BUY"),
            new Trade("MSFT", 255.00, 20, "SELL"),
            new Trade("TSLA", 710.00, 15, "BUY"),
            new Trade("TSLA", 730.00, 20, "SELL"), // Realize profit on TSLA
            new Trade("AAPL", 148.00, 40, "BUY")
        );

        // 2. Setup current market prices for P&L calculations
        Map<String, Double> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", 152.50);
        marketPrices.put("MSFT", 260.00);
        marketPrices.put("TSLA", 725.00);

        System.out.println("==========================================================");
        System.out.println("     FINANCIAL DATA PROCESSING WITH STREAMS & COLLECTORS  ");
        System.out.println("==========================================================\n");

        System.out.println("--- 1. ACTIVE TRADES INVENTORY ---");
        trades.forEach(System.out::println);
        System.out.println();

        System.out.println("--- 2. CURRENT MARKET PRICES (MARK-TO-MARKET) ---");
        marketPrices.forEach((symbol, price) -> System.out.printf("   Symbol: %-4s -> Current Price: $%.2f\n", symbol, price));
        System.out.println();

        // ------------------------------------------------------------
        // DEMONSTRATING: groupingBy with custom collector for VWAP and P&L
        // ------------------------------------------------------------
        System.out.println("----------------------------------------------------------");
        System.out.println("Applying Stream Aggregation (groupingBy + Custom Collector)");
        System.out.println("----------------------------------------------------------");
        System.out.println("1. Grouping by Symbol: 'Collectors.groupingBy(Trade::getSymbol, ...)'");
        System.out.println("2. Accumulating metrics per symbol using Collector.of(...) to track:");
        System.out.println("   - Net Inventory Position");
        System.out.println("   - Cash Balance");
        System.out.println("   - VWAP (Volume Weighted Average Price)");
        System.out.println("3. Evaluating P&L based on Mark-To-Market pricing.");
        System.out.println();

        // Custom collector to calculate trade metrics in a single pass per symbol
        Collector<Trade, PositionTracker, PositionTracker> trackerCollector = Collector.of(
            PositionTracker::new,        // Supplier of initial state accumulator
            PositionTracker::accumulate, // Accumulator logic (runs per item)
            PositionTracker::combine     // Combiner logic (merges state for parallel streams)
        );

        // Perform grouping and collection
        Map<String, PositionTracker> portfolio = trades.stream()
            .collect(Collectors.groupingBy(
                Trade::getSymbol, // Key mapper
                trackerCollector  // Downstream collector
            ));

        // Display results
        System.out.println("[RESULTS PER SYMBOL]:");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-8s | %-12s | %-12s | %-12s | %-12s | %-10s\n", 
                "Symbol", "Net Position", "VWAP", "Net Cashflow", "Market Value", "P&L");
        System.out.println("----------------------------------------------------------------------------------");

        portfolio.forEach((symbol, tracker) -> {
            double currentPrice = marketPrices.getOrDefault(symbol, 0.0);
            double marketValue = tracker.getNetPosition() * currentPrice;
            double pl = tracker.calculatePL(currentPrice);

            System.out.printf("%-8s | %-12d | $%-11.2f | $%-11.2f | $%-11.2f | $%-10.2f (%s)\n",
                    symbol,
                    tracker.getNetPosition(),
                    tracker.getVwap(),
                    tracker.getNetCashFlow(),
                    marketValue,
                    pl,
                    (pl >= 0 ? "PROFIT" : "LOSS")
            );
        });
        System.out.println("----------------------------------------------------------------------------------");
        
        // Overall Portfolio P&L calculation
        double totalPortfolioPL = portfolio.entrySet().stream()
            .mapToDouble(entry -> {
                String symbol = entry.getKey();
                PositionTracker tracker = entry.getValue();
                double currentPrice = marketPrices.getOrDefault(symbol, 0.0);
                return tracker.calculatePL(currentPrice);
            })
            .sum();

        System.out.printf("\n>>> TOTAL PORTFOLIO P&L: $%.2f (%s) <<<\n", 
                totalPortfolioPL, (totalPortfolioPL >= 0 ? "PROFIT" : "LOSS"));
        System.out.println("==========================================================");
    }
}

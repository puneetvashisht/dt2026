package financedemo;

public class Trade {
    private final String symbol;
    private final double price;
    private final int quantity;
    private final String side; // "BUY" or "SELL"

    public Trade(String symbol, double price, int quantity, String side) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSide() {
        return side;
    }

    @Override
    public String toString() {
        return String.format("Trade[Symbol=%-4s, Side=%-4s, Qty=%-4d, Price=$%-7.2f]", 
                symbol, side, quantity, price);
    }
}

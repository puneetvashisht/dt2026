package streamsdemo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) {
        // 1. Initialize sample dataset
        List<Book> books = Arrays.asList(
            new Book(1, "Clean Code", 42.50, "978-0132350884", "Prentice Hall", 2008),
            new Book(2, "Design Patterns", 55.00, "978-0201633610", "Addison-Wesley", 1994),
            new Book(3, "Effective Java", 47.99, "978-0134685991", "Addison-Wesley", 2018),
            new Book(4, "Refactoring", 49.50, "978-0134757599", "Addison-Wesley", 2018),
            new Book(5, "Java Concurrency in Practice", 52.00, "978-0321349606", "Addison-Wesley", 2006),
            new Book(6, "Introduction to Algorithms", 89.99, "978-0262033848", "MIT Press", 2009),
            new Book(7, "The Pragmatic Programmer", 39.99, "978-0135957059", "Addison-Wesley", 2019),
            new Book(8, "Head First Design Patterns", 38.50, "978-0596007126", "O'Reilly Media", 2004),
            new Book(9, "Designing Data-Intensive Applications", 45.00, "978-1449373320", "O'Reilly Media", 2017)
        );

        System.out.println("==========================================================");
        System.out.println("            JAVA STREAMS VS SQL DEMONSTRATION            ");
        System.out.println("==========================================================\n");

        // printSampleData(books);

        // --- DEMO 1: Filtering & Projection (WHERE & SELECT) ---
        // demoFilteringAndProjection(books);

        // --- DEMO 2: Sorting & Limiting (ORDER BY & LIMIT) ---
        demoSortingAndLimiting(books);

        // --- DEMO 3: Unique Values & Counting (DISTINCT & COUNT) ---
        // demoDistinctAndCount(books);

        // --- DEMO 4: Aggregation Functions (SUM, AVG, MIN, MAX) ---
        demoAggregations(books);

        // --- DEMO 5: Grouping & Aggregating (GROUP BY) ---
        demoGroupBy(books);

        // --- DEMO 6: Matching & Searching (EXISTS / ALL) ---
        demoMatching(books);

        // --- DEMO 7: Collecting to Map ---
        demoToMap(books);
    }

    private static void printSampleData(List<Book> books) {
        System.out.println("--- SAMPLE DATASET ---");
        books.forEach(System.out::println);
        System.out.println();
    }

    private static void demoFilteringAndProjection(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 1: Filtering & Projection (WHERE and SELECT)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT title, price FROM books WHERE price > 45.00;");
        System.out.println();
        System.out.println("[Java Streams Flow]:");
        System.out.println("   books.stream()");
        System.out.println("        .filter(b -> b.getPrice() > 45.00)                <-- Intermediate (Filter)");
        System.out.println("        .map(b -> b.getTitle() + \" ($\" + b.getPrice() + \")\") <-- Intermediate (Map/Project)");
        System.out.println("        .forEach(System.out::println);                     <-- Terminal (Action)");
        System.out.println();
        System.out.println("[Results]:");

   

        // Execution
        books.stream()
            .filter(b -> b.getPrice() > 45.00)
             .map(b -> b.getTitle() + " ($" + b.getPrice() + ")")
             .forEach(result -> System.out.println("   " + result));
        System.out.println();
    }

    private static void demoSortingAndLimiting(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 2: Sorting & Limiting (ORDER BY and LIMIT)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT * FROM books ORDER BY price DESC LIMIT 3;");
        System.out.println();
        System.out.println("[Java Streams Flow]:");
        System.out.println("   books.stream()");
        System.out.println("        .sorted(Comparator.comparingDouble(Book::getPrice).reversed()) <-- Intermediate (Sort)");
        System.out.println("        .limit(3)                                                      <-- Intermediate (Slice)");
        System.out.println("        .collect(Collectors.toList());                                 <-- Terminal (Collect)");
        System.out.println();
        System.out.println("[Results]:");

        // Execution
        List<Book> top3Expensive = books.stream()
             .sorted(Comparator.comparingDouble(Book::getPrice).reversed())
             .limit(3)
             .collect(Collectors.toList());
        
        top3Expensive.forEach(b -> System.out.println("   " + b));
        System.out.println();
    }

    private static void demoDistinctAndCount(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 3: Unique Values & Counting (DISTINCT and COUNT)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT DISTINCT publisher FROM books;");
        System.out.println();
        System.out.println("[Java Streams Flow]:");
        System.out.println("   books.stream()");
        System.out.println("        .map(Book::getPublisher)   <-- Intermediate (Map)");
        System.out.println("        .distinct()                <-- Intermediate (Distinct Check)");
        System.out.println("        .forEach(System.out::println);");
        System.out.println();
        System.out.println("[Results - Unique Publishers]:");
        
        // Execution
        books.stream()
             .map(Book::getPublisher)
             .distinct()
             .forEach(pub -> System.out.println("   " + pub));
             
        System.out.println("\n[SQL Equivalent]:");
        System.out.println("   SELECT COUNT(*) FROM books WHERE publicationYear >= 2010;");
        System.out.println();
        System.out.println("[Java Streams Flow]:");
        System.out.println("   long count = books.stream()");
        System.out.println("                      .filter(b -> b.getPublicationYear() >= 2010)");
        System.out.println("                      .count();    <-- Terminal (Count operation returning primitive long)");
        
        long count = books.stream()
                          .filter(b -> b.getPublicationYear() >= 2010)
                          .count();
        System.out.println("\n[Results]:");
        System.out.println("   Count of books published from 2010 onwards: " + count);
        System.out.println();
    }

    private static void demoAggregations(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 4: Aggregation Functions (SUM, AVG, MIN, MAX)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT SUM(price), AVG(price), MIN(price), MAX(price) FROM books;");
        System.out.println();
        
        // 1. Sum
        double sumPrice = books.stream()
                               .mapToDouble(Book::getPrice)
                               .sum(); // terminal operation for DoubleStream

        // 2. Average
        double avgPrice = books.stream()
                               .mapToDouble(Book::getPrice)
                               .average() // returns OptionalDouble
                               .orElse(0.0);

        // 3. Min
        Optional<Book> cheapestBook = books.stream()
                                           .min(Comparator.comparingDouble(Book::getPrice)); // terminal operation returning Optional

        // 4. Max
        Optional<Book> priciestBook = books.stream()
                                           .max(Comparator.comparingDouble(Book::getPrice)); // terminal operation returning Optional

        System.out.println("[Results]:");
        System.out.printf("   Total Inventory Cost (SUM) : $%.2f\n", sumPrice);
        System.out.printf("   Average Book Price (AVG)   : $%.2f\n", avgPrice);
        cheapestBook.ifPresent(b -> System.out.printf("   Cheapest Book (MIN)        : %s (Price: $%.2f)\n", b.getTitle(), b.getPrice()));
        priciestBook.ifPresent(b -> System.out.printf("   Most Expensive Book (MAX)  : %s (Price: $%.2f)\n", b.getTitle(), b.getPrice()));
        System.out.println();
    }

    private static void demoGroupBy(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 5: Grouping and Aggregating (GROUP BY)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT publisher, COUNT(*), AVG(price) FROM books GROUP BY publisher;");
        System.out.println();
        System.out.println("[Java Streams Flow for Count by Publisher]:");
        System.out.println("   Map<String, Long> countByPub = books.stream().collect(");
        System.out.println("       Collectors.groupingBy(Book::getPublisher, Collectors.counting())");
        System.out.println("   );");
        System.out.println();
        System.out.println("[Java Streams Flow for Avg Price by Publisher]:");
        System.out.println("   Map<String, Double> avgPriceByPub = books.stream().collect(");
        System.out.println("       Collectors.groupingBy(Book::getPublisher, Collectors.averagingDouble(Book::getPrice))");
        System.out.println("   );");
        System.out.println();
        System.out.println("[Results]:");

        // Execution: Grouping by Publisher to count books
        Map<String, Long> bookCountByPublisher = books.stream()
            .collect(Collectors.groupingBy(Book::getPublisher, Collectors.counting()));

        // Execution: Grouping by Publisher to find average price
        Map<String, Double> avgPriceByPublisher = books.stream()
            .collect(Collectors.groupingBy(Book::getPublisher, Collectors.averagingDouble(Book::getPrice)));

        System.out.println("   Book Count by Publisher:");
        bookCountByPublisher.forEach((pub, count) -> System.out.printf("     - %-20s : %d book(s)\n", pub, count));

        System.out.println("\n   Average Price by Publisher:");
        avgPriceByPublisher.forEach((pub, avgPrice) -> System.out.printf("     - %-20s : $%.2f\n", pub, avgPrice));
        System.out.println();
    }

    private static void demoMatching(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 6: Matching and Short-circuiting (EXISTS & ALL)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent (EXISTS / ALL / NONE)]:");
        System.out.println("   - EXISTS (SELECT 1 FROM books WHERE price > 80.00)");
        System.out.println("   - NOT EXISTS (SELECT 1 FROM books WHERE price <= 0.00)");
        System.out.println();
        System.out.println("[Java Streams Matching]:");
        System.out.println("   - anyMatch(b -> b.getPrice() > 80.00)  <-- Terminal (checks if any match)");
        System.out.println("   - allMatch(b -> b.getPrice() > 0.00)   <-- Terminal (checks if all match)");
        System.out.println("   - noneMatch(b -> b.getPrice() < 10.00) <-- Terminal (checks if none match)");
        System.out.println();
        System.out.println("[Results]:");

        // Execution
        boolean hasExpensiveBook = books.stream().anyMatch(b -> b.getPrice() > 80.00);
        boolean allBooksHavePrice = books.stream().allMatch(b -> b.getPrice() > 0.00);
        boolean hasSuperCheapBook = books.stream().noneMatch(b -> b.getPrice() < 10.00);

        System.out.println("   Are there any books costing over $80.00?     : " + (hasExpensiveBook ? "Yes" : "No"));
        System.out.println("   Do all books have a valid price (> $0)?      : " + (allBooksHavePrice ? "Yes" : "No"));
        System.out.println("   Are there no books costing less than $10.00? : " + (hasSuperCheapBook ? "Yes" : "No"));
        System.out.println("==========================================================\n");
    }

    private static void demoToMap(List<Book> books) {
        System.out.println("----------------------------------------------------------");
        System.out.println("DEMO 7: Collecting to a Map (Key-Value Dictionary)");
        System.out.println("----------------------------------------------------------");
        System.out.println("[SQL Equivalent]:");
        System.out.println("   SELECT isbn, title FROM books;");
        System.out.println("   -- (Or in JSON-supporting SQL: SELECT json_object_agg(isbn, title) FROM books;)");
        System.out.println();
        System.out.println("[Java Streams Flow]:");
        System.out.println("   Map<String, String> isbnToTitleMap = books.stream()");
        System.out.println("        .collect(Collectors.toMap(");
        System.out.println("             Book::getIsbn,  <-- Key Mapper (Function)");
        System.out.println("             Book::getTitle  <-- Value Mapper (Function)");
        System.out.println("        ));");
        System.out.println();
        System.out.println("[Results]:");

        // Execution 1: Map ISBN to Title
        Map<String, String> isbnToTitleMap = books.stream()
            .collect(Collectors.toMap(Book::getIsbn, Book::getTitle));

        isbnToTitleMap.forEach((isbn, title) -> System.out.printf("   ISBN: %-15s -> Title: %s\n", isbn, title));

        System.out.println("\n[Advanced: Map ID to Book object itself (Function.identity())]:");
        System.out.println("   Map<Integer, Book> idToBookMap = books.stream()");
        System.out.println("        .collect(Collectors.toMap(Book::getId, b -> b));");
        System.out.println();
        
        // Execution 2: Map ID to Book object
        Map<Integer, Book> idToBookMap = books.stream()
            .collect(Collectors.toMap(Book::getId, java.util.function.Function.identity()));

        idToBookMap.forEach((id, book) -> System.out.printf("   ID: %d -> %s\n", id, book.getTitle()));
        System.out.println("==========================================================");
    }
}

package streamsdemo;

public class Book {
    private int id;
    private String title;
    private double price;
    private String isbn;
    private String publisher;
    private int publicationYear;

    public Book(int id, String title, double price, String isbn, String publisher, int publicationYear) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public String toString() {
        return String.format("Book[ID=%d, Title='%s', Price=$%.2f, ISBN='%s', Publisher='%s', Year=%d]",
                id, title, price, isbn, publisher, publicationYear);
    }
}

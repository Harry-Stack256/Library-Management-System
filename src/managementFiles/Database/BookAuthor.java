package managementFiles.Database;



public class BookAuthor {
    private int id;
    private int bookId;
    private String authorName;

    // Constructors
    public BookAuthor() {}

    public BookAuthor(int id, int bookId, String authorName) {
        this.id = id;
        this.bookId = bookId;
        this.authorName = authorName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}

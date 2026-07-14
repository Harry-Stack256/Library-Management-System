package managementFiles.Database;


import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String isbn;
    
    // Here are your graph edges connecting the child subsets!
    private List<String> authorsString = new ArrayList<>();
    private List<BookAuthor>authors= new ArrayList<>();
    private List<BookCopy> copies = new ArrayList<>();

    // Constructors
    public Book() {}

    public Book(int id, String title, String isbn) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public List<BookAuthor> getAuthors() { return authors; }
    public void setAuthors(List<String> authorsList) { this.authorsString = authorsList; }

    public List<String> getAuthorsString() {
		return authorsString;
	}

	public void setAuthorsString(List<String> authorsString) {
		this.authorsString = authorsString;
	}

	public List<BookCopy> getCopies() { return copies; }
    public void setCopies(List<BookCopy> copies) { this.copies = copies; }
    
    
    // Convenience methods to add items to the lists
    public void addAuthor(BookAuthor author) { this.authors.add(author); }
    public void addCopy(BookCopy copy) { this.copies.add(copy); }

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", isbn=" + isbn + ", authors=" + authors + ", copies=" + copies
				+ "]";
	}
    
    
}
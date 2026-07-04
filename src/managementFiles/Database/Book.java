package managementFiles.Database;

public class Book {
	private int ID;
	private String title;
	private boolean isAvailable;
	private String isbn;
	public Book(String title, boolean isAvaliable, String isbn) {
		
		this.title = title;
		this.isAvailable = isAvaliable;
		this.isbn = isbn;
	}
	public Book () {
		
		
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	@Override
	public String toString() {
		return "Book [ID=" + ID + ", title=" + title + ", isAvailable=" + isAvailable + ", isbn=" + isbn + "]";
	}
	
	
	
	
	

}

package managementFiles.Database;



public class BookCopy {
    private int copyId;
    private int bookId;
    private String barcode;
    private boolean isAvailable;

    // Constructors
    public BookCopy() {}

    public BookCopy(int copyId, int bookId, String barcode, boolean isAvailable) {
        this.copyId = copyId;
        this.bookId = bookId;
        this.barcode = barcode;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public int getCopyId() { return copyId; }
    public void setCopyId(int copyId) { this.copyId = copyId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
}

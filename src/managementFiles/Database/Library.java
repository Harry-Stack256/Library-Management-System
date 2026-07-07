package managementFiles.Database;


import java.util.Scanner;

import managementFiles.Database.BookDAO;

public class Library {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        // Point straight to your unified SQLite node
        final String URL = "jdbc:sqlite:LIBRARY_DB.db";
        // Instantiate the DAO once out here—it doesn't need an empty book anymore!
        BookDAO dao = new BookDAO(URL);

        while (running) {
            System.out.println("\n=================================");
            System.out.println("    STAFF LIBRARY SYSTEM (CRUD)   ");
            System.out.println("=================================");
            System.out.println("1. Add New Book      (Create)");
            System.out.println("2. View a Book       (Read)");
            System.out.println("3. Update Copy Status(Update)");
            System.out.println("4. Delete a Book      (Delete)");
            System.out.println("5. Exit System");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine().trim();
            Integer id;
            
            switch(choice) {
                case "1":
                    System.out.println("\n--- Adding New Book Blueprint ---");
                    String title = validString("Title of the Book", scanner);
                    
                    String isbn;
                    while (true) {
                        isbn = validString("ISBN Number", scanner);
                        if (isNumber(isbn)) {
                            break;
                        }
                        System.out.println("ISBN must be numeric characters only.");
                    }
                    
                    // Core book profile initialized
                    Book newBook = new Book();
                    newBook.setTitle(title);
                    newBook.setIsbn(isbn);
                    
                    // Collect Authors (Handles multi-author scenario)
                    System.out.println("\n--- Add Authors ---");
                    boolean addingAuthors = true;
                    while (addingAuthors) {
                        String authorName = validString("Author Name", scanner);
                        BookAuthor author = new BookAuthor();
                        author.setAuthorName(authorName);
                        newBook.addAuthor(author);
                        
                        addingAuthors = validAnswer("Add another author for this book?", scanner);
                    }
                    
                    // Collect Physical Copies (Handles inventory subset)
                    System.out.println("\n--- Add Physical Copies Inventory ---");
                    boolean addingCopies = true;
                    while (addingCopies) {
                        String barcode = validString("Physical Copy Barcode Sticker", scanner);
                        boolean available = validAnswer("Is this physical copy on the shelf right now?", scanner);
                        
                        BookCopy copy = new BookCopy();
                        copy.setBarcode(barcode);
                        copy.setAvailable(available);
                        newBook.addCopy(copy);
                        
                        addingCopies = validAnswer("Add another physical copy inventory row?", scanner);
                    }
                    
                    // Run transactional write operation across all three tables
                    dao.add(newBook);
                    break;
                    
                case "2":
                    System.out.println("\n--- Viewing Book Graph Details ---");
                    id = getID(scanner);
                    if (id != null) {
                        // This returns the clean custom-built JSON graph payload!
                        String jsonResponse = dao.readByID(id);
                        System.out.println("\n[JSON API Payload Output Sent to Console/Server]:");
                        System.out.println(jsonResponse);
                    }
                    break;
                    
                case "3":
                    System.out.println("\n--- Update Physical Copy Status ---");
                    String barcodeToUpdate = validString("Enter the Barcode of the physical item", scanner);
                    boolean newAvailability = validAnswer("Is this copy now available?", scanner);
                    
                    // Updates the specific physical row status, leaving the parent blueprint intact
                    dao.updateCopyAvailability(barcodeToUpdate, newAvailability);
                    break;
                    
                case "4":
                    System.out.println("\n--- Delete Book Core and Child Leaves ---");
                    id = getID(scanner);
                    if (id != null) {
                        // Triggers the cascade database clear
                        dao.delete(id);
                    }
                    break;
                    
                case "5":
                    scanner.close();
                    running = false;
                    System.out.println("System shutting down gracefully.");
                    break;
                    
                default:
                    System.out.println("Invalid selection. Pick choices 1-5.");
                    break;
            }
        }
    }

    private static Integer getID(Scanner scanner) {
        String idInput = validString("Give me the ID of the book record you need", scanner);
        if (!isNumber(idInput)) {
            System.out.println("Error: Input contains non-numeric structural characters.");
            return null;
        }
        try {
            return Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println("Error: Value out of integer bit bounds.");
        }
        return null;
    }

    private static boolean validAnswer(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt + " (yes/no): ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("yes")) {
                return true;
            }
            if (choice.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Invalid input. Please respond with explicit 'yes' or 'no'.");
        }
    }

    public static boolean isNumber(String number) {
        String numberCleaned = number.trim();
        if (numberCleaned.isBlank()) {
            return false;
        }
        for (int x = 0; x < numberCleaned.length(); x++) {
            char asc = numberCleaned.charAt(x);
            if (asc < 48 || asc > 57) {
                return false;
            }
        }
        return true;
    }

    private static String validString(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt + ": ");
            String choice = scanner.nextLine().trim();
            if (!choice.isBlank()) {
                return choice;
            }
            System.out.println("Input cannot be left empty.");
        }
    }
}

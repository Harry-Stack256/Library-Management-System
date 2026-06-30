package managementFiles;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

public class Library {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        

        while (running) {
            System.out.println("\n=================================");
            System.out.println("   STAFF LIBRARY SYSTEM (CRUD)   ");
            System.out.println("=================================");
            System.out.println("1. Add New Book      (Create)");
            System.out.println("2. View All Books    (Read)");
            System.out.println("3. Update Book Status(Update)");
            System.out.println("4. Delete a Book     (Delete)");
            System.out.println("5. Exit System");
		System.out.print("Select an option: ");
		//says go to exact paths to get to library.db 
		
		
		
		
		//This says no matter where you are JUST LOOK FOR THIS
		
		final String URL = "jdbc:sqlite:LIBRARY_DB.db";
		
		//How I see is the first URL is this logic a->b_>c '
		
		//the second URL is this logic no matter what node you start at just like for the jdbc:sqlite:library.db node 
		
	            
		String choice= scanner.nextLine().trim();
		Book book;
		BookDAO DAO;
		Integer ID;
		boolean Available;
		switch(choice){
			case"1":
			System.out.println("Adding book");
			
			String Title = validString("Title of the Book",scanner);
			 Available= validAnswer("Is this book available",scanner);
			String bookNum;
			while(true) {
		    bookNum= validString("Book Number",scanner);
			if(isNumber(bookNum)) {
				break;
			}
			
			}
			
			
			 book = new Book(Title,Available,bookNum);
			System.out.println(book);
			
		        BookDAO bookStorage= new BookDAO(book, URL);
		        
		        
		        	
		        	bookStorage.add(book);
		        	
		        
			
			
			break;
			case"2":
			System.out.println("Veiwing books");
			book = new Book();
			  DAO = new BookDAO(book,URL);
			
			
			ID = ID(scanner);
			if(ID!=null) {
			DAO.read(ID);
			
			}
			break;
			
			
			
			
			case "3":
			System.out.println("Update Books");
			
			
			ID = ID(scanner);
			Available= validAnswer("Is this book Avaliable",scanner);
			if(ID!=null) {
			book = new Book();
			book.setID(ID);
			book.setAvailable(Available);
			DAO = new BookDAO(book,URL);
		     DAO.update(book);
		     
			}
			break;
			case "4":
			System.out.println("Delete Book");
			break;
			 case "5":
				 scanner.close();
			 running = false;
			 break;
			 
			 default:
			 System.out.println("Pick a choice 1-4");
			 break;
			
			
			
			
			
		}
		
		



	}
	}
	private static Integer ID( Scanner scanner ) {
		String ID = validString("Give me the ID of the book you need",scanner);
		boolean hasID = isNumber(ID);
		if(!hasID) {
		System.out.println("Not a number ");
		return null;
		}
		
		
		try {
		int  userID= Integer.parseInt(ID);
		return userID;
		
		
		}catch(NumberFormatException e) {
			System.out.println("Not a number ");
			
		}
		return null;
		
	}
	private static boolean validAnswer(String prompt, Scanner scanner) {
		
		
		while(true) {
		System.out.println(prompt+": ");
		String choice = scanner.nextLine().trim();
		if(choice.trim().equalsIgnoreCase("yes")) {
			return true;
			
		}
		
		if(choice.trim().equalsIgnoreCase("no")) {
			return false;
			
		}
		
		
	
			
		}
		
	}
	private static boolean isNumber(String number) {
		String numberCleaned= number.trim();
		if(numberCleaned.isBlank()) {
			
			return false;
		}
		for(int x=0;x<numberCleaned.length();x++) {
			char asc = numberCleaned.charAt(x);
			int ascNumber = (int)asc;
			if(ascNumber<48||ascNumber>57) {
				
				return false;
			}
			
			
		}
		
		return true;
		
	}
	private static String validString(String prompt ,Scanner scanner) {
		
		
		while(true) {
		
		System.out.println(prompt+": ");
		
		String choice= scanner.nextLine().trim();
		
		if(!choice.isBlank()) {
			
		return choice;
		}
		
	}
}
}



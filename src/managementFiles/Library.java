package managementFiles;

import java.util.Scanner;

public class Library {

	public static void main(String[] args) {
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
		 final String URL = "jdbc:sqlite:library.db";
		
		//How I see is the first URL is this logic a->b_>c '
		
		//the second URL is this logic no matter what node you start at just like for the jdbc:sqlite:library.db node 
		
		
		String choice= scanner.nextLine().trim();
		switch(choice){
			case"1":
			System.out.println("Adding book");
			break;
			case"2":
			System.out.println("Veiwing books");
			break;
			case "3":
			System.out.println("Update Books");
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
}



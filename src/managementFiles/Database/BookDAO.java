package managementFiles.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BookDAO{
	
private Book book;
private String URL;


private   Connection getConnection() throws Exception {
	DatabaseConnection  connection = new DatabaseConnection(URL);
    return connection.getConnection(); 
}
	public BookDAO(Book  book,String url) {
		
		this.book = book;
		this.URL= url;
		
		
		
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	
	public void add(Book book) throws Exception {
		String sql = "INSERT INTO books (title, isAvailable, isbn) VALUES (?, ?, ?)";

        // Using try-with-resources automatically closes connections and statements to prevent memory leaks
        try {
        
        Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Bind the parameters to the '?' placeholders
            pstmt.setString(1, book.getTitle());
            
            int bool ;
            
            
            //bool is the variable for book that's a int but works like a boolean 
            if(book.isAvailable()==true) {
            	bool=1;
            }else {
            	
            	bool=0;
            }
            pstmt.setInt(2, bool);
            pstmt.setString(3, book.getIsbn());

            // Execute the insert statement
            System.out.println("Hello");         
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Book inserted successfully! Rows affected: " + rowsAffected);
            System.out.println("Done");
            
             }
         catch (SQLException e) {
            System.err.println("Error inserting book: " + e.getMessage());
        }
    }
		
	

	//This will have Integer agurments later 
	public String read(int id) {
		
		try {
			
			boolean anyBooks=false;
		String sql ="SELECT * FROM books WHERE ID=?";
		 Connection conn = getConnection();
		 
		 PreparedStatement pstmt = conn.prepareStatement(sql);
		 pstmt.setInt(1, id);
		 ResultSet rs = pstmt.executeQuery();
		 
		 while(rs.next()) {
			 anyBooks = true;
			 int rowNum = rs.getRow();
			 
			 int ID = rs.getInt("ID");
			 String title = rs.getString("title");
			 int isAvailable= rs.getInt("isAvailable");
			 String isbn = rs.getString("isbn");
			 
			 String bool;
			 boolean bookAvailable;
			 if(isAvailable==1) {
				 
				 bool="True";
				 bookAvailable=true;
			 }else {
				 
				 bool="False";
				 bookAvailable=false;
			 }
			 
			 
			 System.out.printf("   %d | %d  |   %s  |    %s   |   %s \n",rowNum,ID,title,bool,isbn);
			 Book book = new Book(title,bookAvailable,isbn);
			return book.toString(); 
			 
			 
		 }
		 
		 
		 if(!anyBooks) {
			 System.out.println("There where no Books with that ID");
			 return  "No book with that ID";
		 }
		 
		 
		 
		 
		
		}catch(Exception e) {
			 
			 System.out.println("Exception");
			 return "Book";
			 
		 }
		 
		 return "Book";
		// TODO Auto-generated method stub
		
	}

	

	
	public void update(Book book) throws Exception {
		
		
		
		String sql = "UPDATE books SET isAvailable = ? WHERE id = ?";
		 Connection conn = getConnection();
		 
		 PreparedStatement pstmt = conn.prepareStatement(sql);
		 
		 int bool;
		 if( book.isAvailable()==true) {
			 bool= 1;
		 }else {
			 bool=0;
		 }
		 
		 pstmt.setInt(1, bool);
		 pstmt.setInt(2, book.getID());
		 int  rs = pstmt.executeUpdate();
		 
		 System.out.println("Rows affected "+rs);
		 
		 if(rs>0) {
			
			read(book.getID());
			 
		 }else {
			 System.out.print("There is no book with this ID");
		 }
		
		// TODO Auto-generated method stub
		
	}

	
	public void delete(int id) throws Exception {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM books WHERE id = ?";
		Connection conn = getConnection();
		 
		 PreparedStatement pstmt = conn.prepareStatement(sql);
		 pstmt.setInt(1, id);
		 
		 
		 System.out.println("Row deleted");
		 read(id);
		 
		 int rs = pstmt.executeUpdate();
		 
		 
		 
		 
		
		
	}

}


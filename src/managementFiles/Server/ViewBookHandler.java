package managementFiles.Server;

import java.io.IOException;
import java.io.OutputStream;

import managementFiles.Database.Book;
import managementFiles.Database.BookDAO;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class ViewBookHandler implements HttpHandler {
	String  bookData;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. BRIDGE: Catch the incoming request and look at the query (e.g., /viewBook?id=1)
        String query = exchange.getRequestURI().getQuery(); 
        //gets the url and gets the query from the get request 
        
        // 2. VALIDATE: Parse out the ID and check if it's valid
        int id = Integer.parseInt(query.split("=")[1]); 
        
        //so it spilts the query find index 1 

        // 3. HANDOFF: Call your exact CRUD app logic!
        Book book = new Book();
        final String URL = "jdbc:sqlite:LIBRARY_DB.db";
        BookDAO dao = new BookDAO(book, URL);
        
        // (Note: You'd modify your read method to return a String instead of just System.out.println)
       String bookData = dao.read(id); 
       System.out.println(bookData);
       

        // 4. RESPONSE: Send the database results back to the browser
        exchange.sendResponseHeaders(200, bookData.length());
        OutputStream os = exchange.getResponseBody();
        os.write(bookData.getBytes());
        os.close();
    }
	public String getBookData() {
		return bookData;
	}
	public void setBookData(String bookData) {
		this.bookData = bookData;
	}
    
}

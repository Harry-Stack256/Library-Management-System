package managementFiles.Server;

import java.io.IOException;
import java.io.OutputStream;

import managementFiles.Database.Book;
import managementFiles.Database.BookDAO;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import managementFiles.Database.BookDAO;



public class ViewBookHandler implements HttpHandler {
    
    // Core database URL node anchor
    private static final String URL = "jdbc:sqlite:LIBRARY_DB.db";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set CORS headers so your upcoming React frontend can talk to this endpoint safely
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        
        String responseText;
        int statusCode = 200;

        try {
            // 1. BRIDGE: Extract the incoming URL query string
            String query = exchange.getRequestURI().getQuery(); 
            
            // 2. VALIDATE: Explicit null guard and regex format check
            if (query == null || !query.contains("=")) {
                statusCode = 400;
                responseText = "{\"error\": \"Missing query parameters. Format should be ?id=VALUE\"}";
            } else {
                String[] queryParts = query.split("=");
                if (queryParts.length < 2) {
                    statusCode = 400;
                    responseText = "{\"error\": \"Malformed query parameter. Missing value after '='\"}";
                } else {
                    String Value = queryParts[1].trim();
                   
                    BookDAO dao = new BookDAO(URL);
                    
                    // Pull the unified JSON graph payload directly out of the database data stream
                    responseText = dao.readByName(Value);
                    
                    if(responseText==null) {
                    	
                    	
                    	if(isNumber(Value)) {
                    	 // Hand off to parsing logic
                        int id = Integer.parseInt(Value); 
                        responseText= dao.readByID(id);
                        if(responseText==null) {
                        	responseText= "{\"error\" :\" Book or Book ID was not found.\"}";
                        	statusCode=400;
                        }
                        
                    	}
                    	
                    }
                    
                    // 3. HANDOFF: Single clean DAO instantiation (No stateful local models passed inside)
                  
                    	
                    	
                    	
                    	
                    
                }
            }
        } catch (NumberFormatException e) {
            statusCode = 400;
            responseText = "{\"error\": \"ID parameter must be a structural integer string.\"}";
        } catch (Exception e) {
            statusCode = 500;
            responseText = "{\"error\": \"Internal server network error: " + e.getMessage() + "\"}";
        }

        // 4. RESPONSE: Stream the payload back to the network pipeline
        byte[] responseBytes = responseText.getBytes("UTF-8");
        
        for(int x =0; x<responseBytes.length;x++) {
        	
        	System.out.println(responseBytes[x]);
        	
        }
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
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

    
}	
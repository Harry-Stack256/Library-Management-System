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
                    String idValue = queryParts[1].trim();
                    
                    // Hand off to parsing logic
                    int id = Integer.parseInt(idValue); 
                    
                    // 3. HANDOFF: Single clean DAO instantiation (No stateful local models passed inside)
                    BookDAO dao = new BookDAO(URL);
                    
                    // Pull the unified JSON graph payload directly out of the database data stream
                    responseText = dao.readByID(id);
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
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        }
    }
}
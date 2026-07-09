package managementFiles.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import managementFiles.Database.Book;
import managementFiles.Database.BookDAO;


public  class DeleteBookHandler implements HttpHandler{
private static final String URL = "jdbc:sqlite:LIBRARY_DB.db";

@Override
public void handle(HttpExchange exchange) throws IOException {
    // Set headers for JSON API delivery and allow React CORS communication
    exchange.getResponseHeaders().set("Content-Type", "application/json");
    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
    
    // Guard rail: Only allow POST or PUT requests to pass through here
    if (!exchange.getRequestMethod().equalsIgnoreCase("Delete")) {
        
        String err = "{\"error\": \"Method not allowed. Use DELETE.\"}";
        exchange.sendResponseHeaders(405, err.length());
        try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
        return;
    }

    String responseText= "{\"message\": \"Book blueprint deleted successfully!\"}";
    int statusCode = 200;

    try {
        // 1. READ THE ENVELOPE: Open the input stream carrying the JSON body
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonBuilder.append(line);
        }
        
        String rawJson = jsonBuilder.toString();
        System.out.println("Received payload to delete: " + rawJson);
        
        Book book = new Book();
        String maybeID= getJsonField(rawJson.toString(),"book_id");
        int ID=-1;
        if(isNumber(maybeID)) {
         ID = Integer.parseInt(getJsonField(rawJson.toString(),"book_id"));
         
        }

         // 3. HANDOFF: Update the database records
         BookDAO dao = new BookDAO(URL);
         boolean available=true;
         boolean didDelete =false;
        
         
         		
         	
         	
         	
      didDelete=   dao.delete(ID);
         
         
       if(didDelete) {
         responseText = "{\"message\": \"Book blueprint deleted successfully!\"}";
         statusCode=200;
         }else {
         	
         	  responseText = "{\"message\": \"Book blueprint not deleted  nothing was found\"}";
         	statusCode= 404;
         }
        
    
    }catch(Exception e ) {
    	
    	 responseText = "{\"message\": \"500  Internal Server Error\"}";
    	statusCode = 200;
    
    }
    byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
    exchange.sendResponseHeaders(statusCode, responseBytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
        os.write(responseBytes);
    }
    }
public static String getJsonField(String json, String fieldName) {
    String target = "\"" + fieldName + "\"";
    int startIdx = json.indexOf(target);
    if (startIdx == -1) return "";

    // Advance past the key name and find the colon identifier
    int colonIdx = json.indexOf(":", startIdx);
    if (colonIdx == -1) return "";

    String remainder = json.substring(colonIdx + 1).trim();

    // Case 1: The value is a String wrapped in quotes
    if (remainder.startsWith("\"")) {
        int endQuote = remainder.indexOf("\"", 1);
        if (endQuote != -1) {
            return remainder.substring(1, endQuote);
        }
    } 
    // Case 2: The value is a primitive number or boolean (terminated by a comma, array bracket, or brace)
    else {
        int endIdx = remainder.length();
        for (int i = 0; i < remainder.length(); i++) {
            char c = remainder.charAt(i);
            if (c == ',' || c == '}' || c == ']' || c == ' ') {
                endIdx = i;
                break;
            }
        }
        return remainder.substring(0, endIdx).trim();
    }
    return "";
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
package managementFiles.Server;




import com.sun.net.httpserver.HttpHandler;

import managementFiles.Database.BookDAO;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import managementFiles.Database.*;





public class UpdateBookHandler implements HttpHandler {
    
    private static final String URL = "jdbc:sqlite:LIBRARY_DB.db";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set headers for JSON API delivery and allow React CORS communication
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        
        // Guard rail: Only allow POST or PUT requests to pass through here
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST") && 
            !exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
            
            String err = "{\"error\": \"Method not allowed. Use POST or PUT.\"}";
            exchange.sendResponseHeaders(405, err.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
            return;
        }

        String responseText;
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
            System.out.println("Received payload to update: " + rawJson);

            // 2. PARSE THE DATA
            // Since we aren't using Jackson/Gson yet, let's manually pull properties 
            // out of a standard flat JSON string format: {"id":3, "title":"1984 New", "isbn":"1234"}
           

            // Build our state model from the parsed payload
            Book book = new Book();
           String maybeID= getJsonField(rawJson.toString(),"book_id");
           int ID=-1;
           if(isNumber(maybeID)) {
            ID = Integer.parseInt(getJsonField(rawJson.toString(),"book_id"));
            
           }

            // 3. HANDOFF: Update the database records
            BookDAO dao = new BookDAO(URL);
            boolean available=true;
            boolean didUpdate =true;
           
            if(dao.readByID(ID)!=null) {
            	String updatedBook = dao.readByID(ID);
            	
            	String barcode= getJsonField(updatedBook,"barcode");
            	String isAvailable=  getJsonField(rawJson.toString(),"isAvailable");
            	System.out.println(isAvailable);
            	if(isAvailable.equalsIgnoreCase("true")) {
            		
            		available = true;
            	}else if(isAvailable.equalsIgnoreCase("false")) {
            		available = false;
            		
            	}
            		
            	
            	
            	
         didUpdate=   dao.updateCopyAvailability(barcode,available);
            
            }
          if(didUpdate) {
            responseText = "{\"message\": \"Book blueprint updated successfully!\"}";
            statusCode=200;
            }else {
            	
            	  responseText = "{\"message\": \"Book blueprint not updated nothing was found\"}";
            	statusCode= 404;
            }
            
        } catch (Exception e) {
            statusCode = 500;
            responseText = "{\"error\": \"Server failed to process update: " + e.getMessage() + "\"}";
        }

        // 4. RESPONSE: Ship the status back
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    // Crude, dependencies-free helper to extract strings from simple JSON strings
    private String extractJsonValue(String json, String key) {
        String target = "\"" + key + "\"";
        int startIdx = json.indexOf(target);
        if (startIdx == -1) return "";
        
        // Move past key name and its following colon
        int colonIdx = json.indexOf(":", startIdx);
        int valueStart = colonIdx + 1;
        
        // Find boundaries if it's a string value (wrapped in quotes) or primitive number
        String remaining = json.substring(valueStart).trim();
        if (remaining.startsWith("\"")) {
            return remaining.substring(1, remaining.indexOf("\"", 1));
        } else {
            // It's a number, break at comma or closing bracket
            int commaIdx = remaining.indexOf(",");
            int bracketIdx = remaining.indexOf("}");
            int endIdx = (commaIdx != -1 && commaIdx < bracketIdx) ? commaIdx : bracketIdx;
            return remaining.substring(0, endIdx).trim();
        }
    }
    /**
     * Direct native parser to extract a field's value from a flat or parent JSON segment.
     * Works perfectly for extracting top-level primitive properties like id, title, and isbn.
     */
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

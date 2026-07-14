package managementFiles.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import managementFiles.Database.Book;
import managementFiles.Database.BookCopy;
import managementFiles.Database.BookDAO;



public class CreateBookHandler implements HttpHandler {
	
	
	private static final String URL = "jdbc:sqlite:LIBRARY_DB.db";
	@Override
	public void handle (HttpExchange exchange)throws IOException {
		
	  String responseText;
	  int statusCode=200;
		 exchange.getResponseHeaders().set("Content-Type", "application/json");
		 exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		String err;
		 
		 if(!(exchange.getRequestMethod().equalsIgnoreCase("POST"))) {
			 
			 err = "{\"error\": \"Method not allowed. Use POST .\"}";
		       
					exchange.sendResponseHeaders(405, err.length());
				
		        try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
		        
		        return;

}
		 

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
		    String title=    getJsonField(rawJson,"title");
		    String isBn=    getJsonField(rawJson,"isbn");
		     String copyCountString=   getJsonField(rawJson,"copyCount");
		     String authorArray=  getJsonArrayBlock(rawJson, "authors");
		     //The list of individual authors 
		     List<String>authors= parseSimpleStringArray(authorArray);
		     
		     
		     
		     
		     
		       
		         // 3. HANDOFF: Update the database records
		         BookDAO dao = new BookDAO(URL);
		         boolean available=true;
		         boolean didAdd =false;
		        
		         if(!(authors.size()==0)&&!(isBn.isBlank())&&!(copyCountString.isBlank())&&!(authorArray.isBlank())) {
		         		if(isNumber(copyCountString)) {
		         	
		         	int copyCount= Integer.parseInt(copyCountString);
		         	
	      didAdd = dao.createBookComplete(title,isBn,authors,copyCount); 
	      
		         		
		         		}else {
		         			
		         			responseText = "{\"message\": \"copyCount formatted wrong\"}";
		         			statusCode=404;
		         		}
		    }else {
		    	responseText = "{\"message\": \"Json not formated correctly\"}";
		    	
		    	statusCode=404;
		    	
		    }
		         
		       if(didAdd) {
		    	   responseText = "{\"message\": \"Book blueprint created successfully!\"}";
		         statusCode=200;
		         }else {
		         	
		        	 responseText = "{\"message\": \"Book could not be created\"}";
		         	statusCode= 404;
		         }
		        
		    
		    }catch(SQLException e) {
		    	
		    	responseText= "{\"message\": \"500 Query Not successful\"}";
		    	statusCode= 500;
		    	
		    }
		    
		    
		    
		    catch(Exception e ) {
		    	
		    	responseText= "{\"message\": \"500  Internal Server Error\"}";
		    	statusCode = 500;
		    
		    }
		    
		    
		    
		    
		    
		    
		    
		    byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
		    exchange.sendResponseHeaders(statusCode, responseBytes.length);
		    try (OutputStream os = exchange.getResponseBody()) {
		        os.write(responseBytes);
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
	
	
	// Helper to slice out the array block between the outer brackets [ and ]
	private static String getJsonArrayBlock(String json, String fieldName) {
	    String key = "\"" + fieldName + "\":";
	    int startIdx = json.indexOf(key);
	    if (startIdx == -1) return "";
	    
	    int arrayStart = json.indexOf("[", startIdx);
	    // Basic bracket matching or finding the corresponding close bracket
	    int arrayEnd = json.indexOf("]", arrayStart); 
	    
	    if (arrayStart != -1 && arrayEnd != -1) {
	        return json.substring(arrayStart + 1, arrayEnd).trim();
	    }
	    return "";
	}

	// Helper to split a raw string array like "George Orwell", "Other Author"
	private static List<String> parseSimpleStringArray(String arrayContent) {
	    List<String> items = new ArrayList<>();
	    if (arrayContent.isEmpty()) return items;
	    
	    String[] elements = arrayContent.split(",");
	    for (String el : elements) {
	        items.add(el.replace("\"", "").trim());
	    }
	    return items;
	}

	// Helper to isolate individual { } objects inside a raw copies array block
	private static List<String> splitJsonObjectsInArray(String arrayContent) {
	    List<String> objects = new ArrayList<>();
	    int len = arrayContent.length();
	    int braceCount = 0;
	    StringBuilder currentObject = new StringBuilder();
	    
	    for (int i = 0; i < len; i++) {
	        char c = arrayContent.charAt(i);
	        if (c == '{') {
	            braceCount++;
	        }
	        
	        if (braceCount > 0) {
	            currentObject.append(c);
	        }
	        
	        if (c == '}') {
	            braceCount--;
	            if (braceCount == 0) {
	                objects.add(currentObject.toString().trim());
	                currentObject.setLength(0); // Reset buffer
	            }
	        }
	    }
	    return objects;
	}
	
}

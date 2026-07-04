package managementFiles.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
	String URL;
public DatabaseConnection(String url) {
	this.URL=url;
	
}
    public  Connection getConnection() throws Exception{
    	
            try {
            	Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(URL);
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                throw e;
            }
        
    }
    
}

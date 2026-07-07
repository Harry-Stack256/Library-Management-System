package managementFiles.Server;



import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {
        try {
            // 1. Open port 8080 on your local machine
            HttpServer server = HttpServer.create(new InetSocketAddress(8084), 0);
            System.out.println("Backend server is live on http://localhost:8080");

            // 2. Tie your handler to a specific URL path
            // This means http://localhost:8080/viewBook will route to your handler
            server.createContext("/viewBook", new ViewBookHandler());
            
            server.createContext("/updateBook", new UpdateBookHandler());

            // 3. Start the execution loop
            server.setExecutor(null); // Use the default system executor
            server.start();
            System.out.println("Listening for HTTP requests...");

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}

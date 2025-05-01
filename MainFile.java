import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainFile {
    public static Connection databaseConnection;
    public static HomeScreen hm;
    public static void main(String[] args) {
        createConnection();
        hm = new HomeScreen();
    }
     private static void createConnection()  {
        
        try {
            databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bilmartdb", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}

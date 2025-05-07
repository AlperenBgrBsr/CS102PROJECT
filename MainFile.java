import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainFile {

   
    public static void main(String[] args) {
        Database.createConnection();
        HomeScreen.hm = new HomeScreen();
    }
}

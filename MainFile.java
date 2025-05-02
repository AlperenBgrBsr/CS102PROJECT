import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainFile {

    public static HomeScreen hm;
    public static void main(String[] args) {
        Database.createConnection();
        hm = new HomeScreen();
    }
}

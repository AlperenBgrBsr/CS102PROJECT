import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainFile {
   public static User currentUserForAll = new User("Hyper10nBtw", "m.a@ug.bilmart.edu.tr","e"); //This is just for debugging purposses, can delete it or change if needed on your own
    
   public static void main(String[] args) {
        Database.createConnection();
        HomeScreen.hm = new HomeScreen();
    }
}

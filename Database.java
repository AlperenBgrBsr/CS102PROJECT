import java.util.*;

public class Database {
    public static ArrayList<User> users;

    public Database() {
        this.users = new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }


}
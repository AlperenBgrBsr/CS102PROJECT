

public class MainFile {
    public static User currentUserForAll; //This is just for debugging purposses, can delete it or change if needed on your own
    public static LoginScreen log;
    
    public static void main(String[] args) {
    
    Database.createConnection();
    log = new LoginScreen();
    RegisterScreen reg = new RegisterScreen(log);
    reg.setVisible(false);
    log.connect(reg);
    
}
}

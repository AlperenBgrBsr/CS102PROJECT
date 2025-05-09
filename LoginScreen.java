import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginScreen extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton dontHaveAnAccountButton;
    private JLabel welcomeLabel;
    private JButton loginButton;
    public static Database database = new Database();
    private static User currentUser; //Added for now (Mete)

    public LoginScreen() {
        this.setTitle("Login");
        this.add(this.createLabelsAndFields());
        this.handleListeners();
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    
    public JPanel createLabelsAndFields() {
        
        this.welcomeLabel = new JLabel("Welcome to BILMART! ");

       
        this.usernameLabel = new JLabel("Username: ");
        this.usernameField = new JTextField(15);
        
        
        this.passwordLabel = new JLabel("Password: ");
        this.passwordField = new JPasswordField(15);
        

        this.dontHaveAnAccountButton = new JButton("Don't have an account?");
        
        this.loginButton = new JButton("Login ");
        
        JPanel panel = new JPanel(new GridLayout(5,5,10,10));
        
        panel.add(welcomeLabel);
        panel.add(new JLabel());
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(dontHaveAnAccountButton);
        
        return panel;
    }
    public void handleListeners() {
        loginButton.addActionListener(new ActionListener() {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Username or password cannot be empty!", 
                        "Input Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return; 
                }
                for (User user : Database.getAllUsersForRegisterAndLogin()) {
                    if ((  user.getUsername().equals(usernameField.getText())) 
                         && user.getPassword().equals(passwordField.getText())) {
                        MainFile.currentUserForAll = getUserWithUsername(usernameField.getText());
                        HomeScreen.hm = new HomeScreen();
                        LoginScreen.this.setVisible(false);//later for logout button
                        usernameField.setText("");
                        passwordField.setText("");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid username/email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                
            }
        });
        dontHaveAnAccountButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterScreen registerScreen = new RegisterScreen();
                registerScreen.setVisible(true);
                LoginScreen.this.setVisible(false);
            }
            
        });
    }

    public boolean isEmail(String string) {
        return string.contains("@");
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void main(String[] args) {
        new LoginScreen();
    }
}

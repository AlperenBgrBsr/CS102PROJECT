import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegisterScreen extends JFrame {
    private JLabel welcomeLabel;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton confirmCodeButton;
    private JLabel codeLabel;
    private JTextField codeField;
    private String code = "123";
    
    public RegisterScreen() {
        this.setTitle("Register");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(this.createLabelsAndFields());
        this.handleListeners();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JPanel createLabelsAndFields() {
        JPanel panel = new JPanel(new GridLayout(6, 5, 10, 10));

        welcomeLabel = new JLabel("Welcome to BILMART! Register Below");

        emailLabel = new JLabel("Email: ");
        emailField = new JTextField(15);

        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(15);

        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(15);

        registerButton = new JButton("Register");
        confirmCodeButton = new JButton("Confirm Code");

        codeLabel = new JLabel("Enter your code: ");
        codeField = new JTextField(15);
        codeLabel.setVisible(false);
        codeField.setVisible(false);
        confirmCodeButton.setVisible(false);

        panel.add(welcomeLabel);
        panel.add(new JLabel());
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(codeLabel);
        panel.add(codeField);
        panel.add(confirmCodeButton);

        return panel;
    }

    public void handleListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isValidEmail(emailField.getText())) {
                    String validity = isValidUsernameAndEmail (usernameField.getText(), emailField.getText());
                    if ( validity.equalsIgnoreCase("EmailAndUsername")){
                        JOptionPane.showMessageDialog(null,
                            "There is already an account registered with that username and email!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    }
                    else if ( validity.equalsIgnoreCase("Username")){
                        JOptionPane.showMessageDialog(null,
                            "There is already an account registered with that username!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    }
                    else if (validity.equalsIgnoreCase("Email")){
                        JOptionPane.showMessageDialog(null,
                            "There is already an account registered with that email!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    }
                    else if ( validity.equalsIgnoreCase("Okay")){
                        JOptionPane.showMessageDialog(null,
                                "We have sent a confirmation code to your email",
                                "Confirmation Code",
                                JOptionPane.WARNING_MESSAGE);
                        codeLabel.setVisible(true);
                        codeField.setVisible(true);
                        confirmCodeButton.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid email!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        confirmCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (codeField.getText().equals(code)) {
                    String email = emailField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    User newUser = new User(username, email, password);
                    // LoginScreen.database.addUser(newUser);
                    Database.addToDatabase(newUser); // şimdilik comment atıyorum ama doğru hali bu

                    JOptionPane.showMessageDialog(null,
                            "You have been successfully registered, redirecting to login screen",
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);

                    new LoginScreen();
                    RegisterScreen.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Incorrect confirmation code!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        return email.endsWith("@ug.bilkent.edu.tr");
    }

    public String isValidUsernameAndEmail(String username, String email){ // checks if the username and the email has registered before

        String validity = "";// 4 Types --> Both email and username is not valid, only email is not valid, only username is not valid, and okay
        boolean isValid = true;
        ArrayList<User> allUsers = Database.getAllUsersForRegisterAndLogin();
        for (int i = 0; i < allUsers.size() && isValid ; i++){

            if ( allUsers.get(i).getUsername().equalsIgnoreCase(username) && allUsers.get(i).getEmail().equalsIgnoreCase(email)){

                isValid = false;
                validity = "EmailAndUsername";

            }
            else if ( allUsers.get(i).getUsername().equalsIgnoreCase(username) ){

                isValid = false;
                validity = "Username";

            }
            else if ( allUsers.get(i).getEmail().equalsIgnoreCase(email) ){

                isValid = false;
                validity = "Email";

            }


        }

        if ( isValid ){
            validity = "Okay";
        }

        return validity;

    }

   
}

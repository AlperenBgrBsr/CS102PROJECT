import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                    JOptionPane.showMessageDialog(null,
                            "We have sent a confirmation code to your email",
                            "Confirmation Code",
                            JOptionPane.WARNING_MESSAGE);
                    codeLabel.setVisible(true);
                    codeField.setVisible(true);
                    confirmCodeButton.setVisible(true);
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
                    LoginScreen.database.addUser(newUser);

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

   
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
public class RegisterScreen extends JFrame {
    private User currUser = new User("Alperen", "bugra.basar@ug.bilkent.edu.tr","Apsdlfapd");
    private Random rand = new Random();
    private final Color BLUE_COLOR = new Color(21,50,80);
    private JButton forgotPasswordButton;
    private JButton loginButton;
    private JTextField newPasswordField;
    private JTextField confirmCodeFieldPassword;
    private ImageIcon bilmartIcon;
    private ImageIcon bilmartTitle;
    private JLabel bilmartLogo;
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
        bilmartIcon = new ImageIcon("icons/BilMartIcon.png");
        bilmartTitle = new ImageIcon("icons/BilMart.png");
        this.setTitle("Register");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(this.createLabelsAndFields());
        this.add(createTopPanel());
        /*this.add(createForgotPasswordButton());
        this.add(createLoginRegisterButtons());
        this.handleListeners();
        */

        this.setSize(new Dimension(1000,1000));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        currUser = new User(username, email,password);
        
    }

    
    public JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BLUE_COLOR);
        bilmartLogo = new JLabel();
        welcomeLabel = new JLabel();
        bilmartLogo.setPreferredSize(new Dimension(100,100));
        bilmartLogo.setIcon(bilmartIcon);
        welcomeLabel.setIcon(bilmartTitle);
        panel.add(bilmartLogo);
        panel.add(welcomeLabel);

        return panel;
    }

    public JButton createForgotPasswordButton() {
        forgotPasswordButton = new JButton("Forgot your password?  ");
        return forgotPasswordButton;
    }

    public JPanel createLoginRegisterButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setPreferredSize(new Dimension(300, 50));

        // Create buttons
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Style buttons to look unified
        Color bgColor = new Color(220, 220, 220);
        loginButton.setBackground(bgColor);
        registerButton.setBackground(bgColor);
        loginButton.setOpaque(true);
        registerButton.setOpaque(true);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        registerButton.setBorder(BorderFactory.createEmptyBorder());

        // Create a vertical divider
        

        // Add components to the panel
        panel.add(loginButton);
       
        panel.add(registerButton);

        return panel;
    }

    public JPanel createLabelsAndFields() {
        JPanel panel = new JPanel(new GridLayout(6, 5, 10, 10));

        welcomeLabel = new JLabel("Bilmart");
        welcomeLabel.

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


        confirmCodeFieldPassword = new JTextField("Enter your code here",15);
        confirmCodeFieldPassword.setVisible(false);

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

    public User getUser() {
        return currUser;
    }

    public void handleListeners() {
        forgotPasswordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (e.getSource()==forgotPasswordButton) {
                    JOptionPane.showMessageDialog(null,
                            "We have sent a reset code to your email",
                            "Password Reset Code",
                            JOptionPane.WARNING_MESSAGE);
                triggerForgotPasswordFrame();
                setVisibility(false);
                }
            }

        });
        registerButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.setBackground(BLUE_COLOR);
                if (isValidEmail(emailField.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "We have sent a confirmation code to your email",
                            "Confirmation Code",
                            JOptionPane.WARNING_MESSAGE);
                    EmailSender.sendVerificationEmail(currUser);
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
                    LoginScreen.database.addUser(currUser);
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
    
    public void setVisibility(boolean flag) {
        this.setVisible(flag);
    }
    
   
        
    public void triggerForgotPasswordFrame() {
    currUser.setUsername("Alperen");
    currUser.setEmail("bugra.basar@ug.bilkent.edu.tr");
    currUser.setPassword("Alperewn"); // Optional

    // Make sure email is not empty or invalid
    if (currUser.getEmail() == null || currUser.getEmail().isEmpty() || !isValidEmail(currUser.getEmail())) {
        JOptionPane.showMessageDialog(null,
                "Please enter a valid email before resetting your password.",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE);
               
        return;
    }

    ForgotPasswordFrame frame = new ForgotPasswordFrame(this);
    frame.setVisible(true);
    }
    public boolean isValidEmail(String email) {
        return email.endsWith("@ug.bilkent.edu.tr");
    }

   
}

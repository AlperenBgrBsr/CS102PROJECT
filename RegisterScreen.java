import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;                    
import java.awt.*;                       
import java.awt.event.*;                
import java.util.Random;                

public class RegisterScreen extends JFrame {
    private User currUser;
    private Random rand = new Random();
    private final Color BLUE_COLOR = new Color(21,50,80);
    private JButton loginButton;
    private JTextField newPasswordField;
    private LoginScreen logScreen;
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
    private int code;
    
    public RegisterScreen(LoginScreen log) {
    currUser = new User(null,null,null);
    this.logScreen = log;
    this.setTitle("Register");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout()); 

    initializeIcons(); 

    
    this.add(createTopPanel(), BorderLayout.NORTH);
    this.add(createFormPanel(), BorderLayout.CENTER);
    this.add(createBottomPanel(), BorderLayout.SOUTH);

    this.setSize(new Dimension(500, 600));
    this.setLocationRelativeTo(null);
    this.setVisible(true);

    handleListeners(); 
}


     public void initializeIcons() {
    bilmartIcon = resizeIcon(new ImageIcon("icons/BilMartIcon.png"), 60, 60);
    bilmartTitle = new ImageIcon("icons/BilMart.png");
}

public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
    return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
}

    public JPanel createTopPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(BLUE_COLOR);
    panel.setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(BLUE_COLOR);
    contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

    bilmartLogo = new JLabel();
    bilmartLogo.setPreferredSize(new Dimension(60, 60));
    bilmartLogo.setIcon(bilmartIcon);

    welcomeLabel = new JLabel("BILMART");
    welcomeLabel.setPreferredSize(new Dimension(120, 120));
    welcomeLabel.setForeground(Color.WHITE);
    welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

    contentPanel.add(bilmartLogo);
    contentPanel.add(welcomeLabel);

    panel.add(contentPanel, BorderLayout.CENTER);
    return panel;
}


    

    // public JPanel createLoginRegisterButtons() {
    //     JPanel panel = new JPanel(new GridLayout(1, 2));
    //     panel.setPreferredSize(new Dimension(300, 50));

    //     // Create buttons
    //     loginButton = new JButton("Login");
    //     registerButton = new JButton("Register");

    //     // Style buttons to look unified
    //     Color bgColor = new Color(220, 220, 220);
    //     loginButton.setBackground(bgColor);
    //     registerButton.setBackground(bgColor);
    //     loginButton.setOpaque(true);
    //     registerButton.setOpaque(true);
    //     loginButton.setBorder(BorderFactory.createEmptyBorder());
    //     registerButton.setBorder(BorderFactory.createEmptyBorder());

    //     // Create a vertical divider
    //     //bugra.basar@ug.bilkent.edu.tr
    //     //Alperen
    //     //123

    //     // Add components to the panel
    //     panel.add(loginButton);
       
    //     panel.add(registerButton);

    //     return panel;
    // }
        
    public JPanel createFormPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(5, 1, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    emailField = new JTextField();
    usernameField = new JTextField();
    passwordField = new JPasswordField();
    codeField = new JTextField();

    addPlaceholderBehavior(emailField, "Email (@ug.bilkent.edu.tr)");
    addPlaceholderBehavior(usernameField, "Username");
    addPlaceholderBehavior(passwordField, "Password");
    addPlaceholderBehavior(codeField, "Enter verification code");
    

    codeField.setVisible(false); 

    panel.add(emailField);
    panel.add(usernameField);
    panel.add(passwordField);
    panel.add(codeField);

    return panel;
}

public void addPlaceholderBehavior(JTextField field, String placeholder) {
    field.setText(placeholder);
    field.setForeground(Color.WHITE);

    field.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(Color.WHITE);
            }
        }

        public void focusLost(FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
            }
        }
    });
}


    public JPanel createBottomPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    registerButton = new JButton("Register");
    confirmCodeButton = new JButton("Confirm Code");
    loginButton = new JButton("Back to Login");

    
    JButton[] buttons = {registerButton, confirmCodeButton, loginButton};
    for (JButton btn : buttons) {
        btn.setBackground(BLUE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(btn.getFont().deriveFont(14f));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    loginButton.setFont(loginButton.getFont().deriveFont(13f));
    confirmCodeButton.setVisible(false);

    panel.add(registerButton);
    panel.add(confirmCodeButton);
    panel.add(loginButton);
    

    return panel;
    }

    public User getCurrentUser() {
        return currUser;
    }

    public void handleListeners() {
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                            "Redirecting to login screen",
                            "Redirection",
                            JOptionPane.INFORMATION_MESSAGE);
                RegisterScreen.this.logScreen.setVisibility(true);
                RegisterScreen.this.setVisibility(false);
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.setBackground(BLUE_COLOR);
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
                        
                        if ( passwordField.getText().length()>= 8){
                            codeField.setVisible(true);
                            confirmCodeButton.setVisible(true);
                            currUser.setEmail(emailField.getText());
                            currUser.setPassword(passwordField.getText());
                            currUser.setUsername(usernameField.getText());
                            code = rand.nextInt(100000,1000000);
                            EmailSender.sendVerificationEmail(currUser,code);
                            JOptionPane.showMessageDialog(null,
                                "We have sent a confirmation code to your email",
                                "Confirmation Code",
                                JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                           
                            JOptionPane.showMessageDialog(null,
                                "Your password should be at least 8 characters!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid email type!",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        confirmCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Integer.parseInt(codeField.getText()) == code) {
                    
                    // LoginScreen.database.addUser(currUser);
                    JOptionPane.showMessageDialog(null,
                            "You have been successfully registered, redirecting to login screen",
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);

                    logScreen.setVisibility(true);
                    RegisterScreen.this.setVisibility(false);
                    Database.addToDatabase(new User(currUser.getUsername(), currUser.getEmail(), currUser.getPassword()));

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
    if (currUser.getEmail() == null || currUser.getEmail().isEmpty() || !isValidEmail(currUser.getEmail())) {
        JOptionPane.showMessageDialog(null,
                "Please enter a valid email before resetting your password.",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE);
               
        return;
    }

    ForgotPasswordFrame frame = new ForgotPasswordFrame(logScreen);
    frame.setVisible(true);
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

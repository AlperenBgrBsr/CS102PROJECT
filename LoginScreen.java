import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private final Color BLUE_COLOR = new Color(21,50,80);
    private JLabel usernameLabel;
    private RegisterScreen registerScreen;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton dontHaveAnAccountButton;
    private JLabel bilmartLogo;
    private ImageIcon bilmartIcon;
    private ImageIcon bilmartTitle;
    private JLabel welcomeLabel;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    public static Database database = new Database();
    
    public LoginScreen() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(400, 480));
        this.setTitle("Login");
        this.initializeIcons();
        this.setLayout(new BorderLayout());
        this.add(createTopPanel(), BorderLayout.NORTH);
        this.add(createFieldsPanel(), BorderLayout.CENTER);
        this.handleListeners();
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    public void connect(RegisterScreen reg) {
        this.registerScreen = reg;
    }
    public void initializeIcons() {
        bilmartIcon = new ImageIcon("icons/BilMartIcon.png");
        bilmartTitle = new ImageIcon("icons/BilMart.png");
    }
    public JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BLUE_COLOR);
        panel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        contentPanel.setBackground(BLUE_COLOR);

        bilmartLogo = new JLabel();
        bilmartLogo.setPreferredSize(new Dimension(60, 60));
        bilmartLogo.setIcon(resizeIcon(bilmartIcon, 60, 60));

        welcomeLabel = new JLabel("BILMART");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        contentPanel.add(bilmartLogo);
        contentPanel.add(welcomeLabel);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }


    public User getCurrentUser() {
        return this.registerScreen.getCurrentUser();
    }
    public void addPlaceholder(JTextField field, String text) {
    field.setText(text);
    field.setForeground(Color.GRAY);
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent e) {
            if (field.getText().equals(text)) {
                field.setText("");
                field.setForeground(Color.BLACK);
            }
        }
        public void focusLost(java.awt.event.FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setForeground(Color.GRAY);
                field.setText(text);
            }
        }
    });
}

public void styleButton(JButton button) {
    button.setFocusPainted(false);
    button.setContentAreaFilled(true);
    button.setBorderPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
}

   
    public JPanel createFieldsPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    usernameField = new JTextField();
    addPlaceholder(usernameField, "Username");

    passwordField = new JPasswordField();
    addPlaceholder(passwordField, "Password");

    loginButton = new JButton("Login");
    loginButton.setBackground(BLUE_COLOR);
    loginButton.setForeground(Color.WHITE);
    loginButton.setOpaque(true);
    styleButton(loginButton);

    dontHaveAnAccountButton = new JButton("Don't have an account?");
    styleButton(dontHaveAnAccountButton);

    forgotPasswordButton = new JButton("Forgot your password?");
    styleButton(forgotPasswordButton);

    panel.add(usernameField);
    panel.add(passwordField);
    panel.add(loginButton);
    panel.add(dontHaveAnAccountButton);
    panel.add(forgotPasswordButton);

    return panel;
}

    public void connectScreens(RegisterScreen reg) {
        this.registerScreen = reg;
    }
    public void handleListeners() {
        loginButton.addActionListener(new ActionListener() {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.setBackground(BLUE_COLOR);
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Username or password cannot be empty!", 
                        "Input Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return; 
                }
                for (User user : Database.getAllUsersForRegisterAndLogin()) {
                    if (user.getUsername().equals(usernameField.getText()) 
                         && user.getPassword().equals(passwordField.getText())) {
                        MainFile.currentUserForAll = Database.getUserWithUsername(usernameField.getText());
                        HomeScreen.hm = new HomeScreen();
                        LoginScreen.this.setVisible(false);
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
        registerScreen.setVisible(true); 
        LoginScreen.this.setVisible(false);
    }
});
forgotPasswordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (e.getSource()==forgotPasswordButton) {
                triggerForgotPasswordFrame();
                setVisibility(false);
                }
            }
        });
    }
    public void triggerForgotPasswordFrame() {
        new ForgotPasswordFrame(this);
    }

    public void setVisibility(boolean flag) {
        this.setVisible(flag);
    }
    public boolean isValidEmail(String email) {
        return email.endsWith("@ug.bilkent.edu.tr");
    }

    public boolean isEmail(String string) {
        return string.contains("@");
    }

    
}

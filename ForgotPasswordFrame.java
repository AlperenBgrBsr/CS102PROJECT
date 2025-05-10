import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ForgotPasswordFrame extends JFrame {
    private Random rand;
    private JLabel bilmartLogo;
    private JLabel welcomeLabel;
    private final Color BLUE_COLOR = new Color(21,50,80);
    private RegisterScreen registerScreen;
    private JTextField newPasswordField;
    private ImageIcon bilmartIcon;
    private ImageIcon bilmartTitle;
    private JTextField enterVerificationCodeField;
    private JButton resetPassword;
    private JTextField repeatNewPassword;
    private JTextField usernameField;
    private int code;

    public ForgotPasswordFrame(RegisterScreen registerScreen) {
        rand = new Random();
        this.registerScreen = registerScreen;

        
        this.setLayout(new BorderLayout());

        
        this.initializeImages();

        
        code = generateCode();
        //EmailSender.sendForgotPasswordEmail(registerScreen.getUser(), code);

        this.add(createTopPanel(), BorderLayout.NORTH); 
        this.add(createFieldsPanel(), BorderLayout.CENTER); 
        this.handleListeners();
        this.setSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null); 
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
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
        welcomeLabel.setPreferredSize(new Dimension(120,120));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));

    
        contentPanel.add(bilmartLogo);
        contentPanel.add(welcomeLabel);
    
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }
    

    public int generateCode() {
        code = rand.nextInt(100000, 1000000);
        return code;
    }

    public void initializeImages() {
        // FIX: Ensure these paths are valid relative to the working directory or use getClass().getResource() if in resources
        bilmartIcon = resizeIcon(new ImageIcon("icons/BilMartIcon.png"), 60, 60); // Resize here
        bilmartTitle = new ImageIcon("icons/BilMart.png");
    }
    public void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
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
    

    public JPanel createFieldsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, vertical stacking
        panel.setBackground(Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
        usernameField = new JTextField();
        addPlaceholderBehavior(usernameField, "Username");
    
        newPasswordField = new JTextField();
        addPlaceholderBehavior(newPasswordField, "New Password");
    
        repeatNewPassword = new JTextField();
        addPlaceholderBehavior(repeatNewPassword, "Repeat New Password");
    
        enterVerificationCodeField = new JTextField();
        addPlaceholderBehavior(enterVerificationCodeField, "Enter verification code");
    
        resetPassword = new JButton("Update Password");
        resetPassword.setBackground(BLUE_COLOR); 
        resetPassword.setForeground(Color.WHITE);
        resetPassword.setFocusPainted(false);
        resetPassword.setFont(resetPassword.getFont().deriveFont(14f));
        resetPassword.setOpaque(true);
        resetPassword.setBorderPainted(false);
        resetPassword.setFocusPainted(false);    

        panel.add(usernameField);
        panel.add(newPasswordField);
        panel.add(repeatNewPassword);
        panel.add(enterVerificationCodeField);
        panel.add(resetPassword);
    
        return panel;
    }
    

    public void handleListeners() {
        resetPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean passwordSame = repeatNewPassword.getText().equals(newPasswordField.getText());
                boolean matchingCode = false;
                boolean samePassword = newPasswordField.getText().equals(registerScreen.getUser().getPassword());
                try {
                    matchingCode = Integer.parseInt(enterVerificationCodeField.getText()) == code;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "You haven't entered a number",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                boolean correctUsername = usernameField.getText().equals(registerScreen.getUser().getUsername());

                if (passwordSame && matchingCode && correctUsername && !samePassword) {
                    registerScreen.getUser().setPassword(newPasswordField.getText());
                    JOptionPane.showMessageDialog(null,
                            "Your password has been set, redirecting to login",
                            "Password Reset",
                            JOptionPane.INFORMATION_MESSAGE);
                    registerScreen.setVisibility(true);
                    disposePasswordFrame();
                    
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input. Please check your username, verification code, and passwords.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void disposePasswordFrame() {
        this.dispose();
    }
}

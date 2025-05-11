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
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private LoginScreen loginScreen;
    private JTextField newPasswordField;
    private ImageIcon bilmartIcon;
    private ImageIcon bilmartTitle;
    private JTextField enterVerificationCodeField;
    private JButton resetPassword;
    private JButton verifyCode;
    private JTextField repeatNewPassword;
    private JTextField usernameField;
    private int code;

    public ForgotPasswordFrame(LoginScreen log) {
        rand = new Random();
        this.loginScreen = log;
        this.setLayout(new BorderLayout());
        this.initializeImages();
        this.add(createTopPanel(), BorderLayout.NORTH); 
        this.add(createFieldsPanel(), BorderLayout.CENTER); 
        this.handleListeners();
        this.setSize(new Dimension(400, 450));
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
        
        bilmartIcon = resizeIcon(new ImageIcon("icons/BilMartIcon.png"), 60, 60); 
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
        panel.setLayout(new GridLayout(6, 1, 10, 10)); // 5 rows, vertical stacking
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
        enterVerificationCodeField.setVisible(false);
    
        resetPassword = new JButton("Update Password");
        resetPassword.setBackground(BLUE_COLOR); 
        resetPassword.setForeground(Color.WHITE);
        resetPassword.setFocusPainted(false);
        resetPassword.setFont(resetPassword.getFont().deriveFont(14f));
        resetPassword.setOpaque(true);
        resetPassword.setBorderPainted(false);
        resetPassword.setFocusPainted(false);   
        
        verifyCode = new JButton("Verify Code");
        verifyCode.setBackground(BLUE_COLOR); 
        verifyCode.setForeground(Color.WHITE);
        verifyCode.setFocusPainted(false);
        verifyCode.setFont(resetPassword.getFont().deriveFont(14f));
        verifyCode.setOpaque(true);
        verifyCode.setBorderPainted(false);
        verifyCode.setFocusPainted(false); 
        verifyCode.setVisible(false);
        

        panel.add(usernameField);
        panel.add(newPasswordField);
        panel.add(repeatNewPassword);
        panel.add(enterVerificationCodeField);
        panel.add(resetPassword);
        panel.add(verifyCode);
    
        return panel;
    }
    

    public void handleListeners() {
        resetPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                User passwordChangeUser = Database.getUserWithUsername(usernameField.getText());
                if (  passwordChangeUser == null){
                    JOptionPane.showMessageDialog(null,
                            "Enter a valid username!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else{

                    boolean passwordSame = repeatNewPassword.getText().equals(newPasswordField.getText());
                    
                    if ( passwordSame ){
                        if ( newPasswordField.getText().length() >= 8){
                            code = generateCode();
                            EmailSender.sendForgotPasswordEmail(passwordChangeUser, code);
                            JOptionPane.showMessageDialog(null,
                                "You have been sent an email with a code!",
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                            verifyCode.setVisible(true);
                            enterVerificationCodeField.setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(null,
                                "Your password should be at least 8 characters!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }

                    }
                    else{
                        JOptionPane.showMessageDialog(null,
                            "Repeat your new password correctly!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }

                }


            
                
            }
        });

        verifyCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                boolean matchingCode = false;
                try {
                    matchingCode = Integer.parseInt(enterVerificationCodeField.getText()) == code;
                    if ( matchingCode ){
                        changePassword(usernameField.getText(),newPasswordField.getText());
                        loginScreen.getCurrentUser().setPassword(newPasswordField.getText());
                        JOptionPane.showMessageDialog(null,
                            "Your password has been set, redirecting to login",
                            "Password Reset",
                            JOptionPane.INFORMATION_MESSAGE);
                        loginScreen.setVisibility(true);
                        disposePasswordFrame();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,
                            "Wrong code!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "You haven't entered a number",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }


            }
            
        });

    }

    private void changePassword(String username, String newPassword) {
        
        try {
            PreparedStatement changePasswordStatement = Database.databaseConnection.prepareStatement("UPDATE users SET user_password = ? WHERE username = ?");
            changePasswordStatement.setString(1, newPassword);
            changePasswordStatement.setString(2, username);
            changePasswordStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

    }

    public void disposePasswordFrame() {
        this.dispose();
    }
}

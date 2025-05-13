import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Properties;

public class Kaptcha extends JFrame {

    private String captchaText;
    private JTextField inputField;
    private JLabel imageLabel;
    private JLabel resultLabel;
    private DefaultKaptcha kaptcha;
    private JLabel bilmartLogo;
    private ImageIcon bilmartIcon;
    private ImageIcon bilmartTitle;
    private JLabel welcomeLabel;
    private final Color BLUE_COLOR = new Color(21, 50, 80);
    private LoginScreen log;

    public Kaptcha(LoginScreen log) {
        setTitle("CAPTCHA");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Color.white);
        this.setResizable(false);
        initializeIcons();
        this.log = log;
        // Add custom top banner
        add(createTopPanel(), BorderLayout.NORTH);

        // Create Kaptcha generator
        kaptcha = createKaptcha();

        // Center panel (captcha image + input + buttons)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        // CAPTCHA image
        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(100,60));
        regenerateCaptcha();

        // Input + Buttons
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        inputField.setPreferredSize(new Dimension(80,30));
        JButton verifyButton = new JButton("Verify");
        JButton refreshButton = new JButton("Refresh");
        styleButton(verifyButton);
        styleButton(refreshButton);
        verifyButton.setPreferredSize(new Dimension(300, 50));
        refreshButton.setPreferredSize(new Dimension(300,50));
        JLabel label = new JLabel("Enter CAPTCHA: ");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));

        label.setPreferredSize(new Dimension(180,50));
        inputPanel.add(label);
        inputPanel.add(inputField);
        inputPanel.add(verifyButton);
        inputPanel.add(refreshButton);

        // Result label
        resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Button actions
        verifyButton.addActionListener(e -> {
            String userInput = inputField.getText().trim();
            if (userInput.equalsIgnoreCase(captchaText)) {
                resultLabel.setText("✅ Correct!");
                SwingUtilities.invokeLater(() -> correctAnswer());
            } else {
                resultLabel.setText("❌ Incorrect. Try again.");
            }
        });

        refreshButton.addActionListener(e -> {
            regenerateCaptcha();
            resultLabel.setText(" ");
            inputField.setText("");
        });

        // Assemble center panel
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(inputPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(resultLabel);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void initializeIcons() {
        bilmartIcon = new ImageIcon("icons/BilMartIcon.png");
        bilmartTitle = new ImageIcon("icons/BilMart.png");
        this.setIconImage(bilmartIcon.getImage());
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

    public void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private DefaultKaptcha createKaptcha() {
        Properties props = new Properties();
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.font.color", "black");
        props.put("kaptcha.image.width", "250");
        props.put("kaptcha.image.height", "80");
        props.put("kaptcha.textproducer.char.length", "5");
        props.put("kaptcha.textproducer.font.size", "40");
        Config config = new Config(props);
        DefaultKaptcha k = new DefaultKaptcha();
        k.setConfig(config);
        return k;
    }

    private void regenerateCaptcha() {
        captchaText = kaptcha.createText();
        BufferedImage image = kaptcha.createImage(captchaText);
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void correctAnswer() {
        this.dispose();
        log.resetNumOfTries();
        log.setEnabled(true);
        log.toFront();
        log.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Kaptcha(new LoginScreen()).setVisible(true));
    }
}

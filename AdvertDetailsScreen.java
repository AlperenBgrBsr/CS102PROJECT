package BilMart;
import javax.swing.*;
import java.awt.*;

public class AdvertDetailsScreen extends JFrame {

    public AdvertDetailsScreen(String title, String description, String imagePath) {
        setTitle("Advert Details");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(img));
        JPanel imagePanel = new JPanel();

        imagePanel.add(imageLabel);
        add(imagePanel, BorderLayout.WEST);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Title: " + title + "\n\n" + description);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        
    }
}
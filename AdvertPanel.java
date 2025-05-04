package BilMart;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvertPanel extends JPanel {
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JButton bookmarkButton;
    private JButton reachButton;
    private boolean isBookmarked = false;
    private String imagePath;
    private String description;

    public AdvertPanel(String title, double price, String description, String imagePath) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setPreferredSize(new Dimension(550, 120));

        // Sol: Resim
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(img));
        add(imageLabel, BorderLayout.WEST);

        // Orta: Başlık + fiyat
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel = new JLabel("₺" + price);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(titleLabel);
        infoPanel.add(priceLabel);
        add(infoPanel, BorderLayout.CENTER);

        // Sağ: Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        bookmarkButton = new JButton("☆ Bookmark");
        reachButton = new JButton("Reach Seller");

        buttonPanel.add(bookmarkButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(reachButton);
        add(buttonPanel, BorderLayout.EAST);

        // Bookmark toggle
        bookmarkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBookmarked = !isBookmarked;
                bookmarkButton.setText(isBookmarked ? "★ Bookmarked" : "☆ Bookmark");
            }
        });

        // Reach seller message
        reachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdvertPanel.this, "Seller has been contacted!");
            }
        });

        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new AdvertDetailsScreen(titleLabel.getText(), description, imagePath);
            }
        });

    }

}
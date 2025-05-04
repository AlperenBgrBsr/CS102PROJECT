
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * BookmarkedAdvertsScreen displays all adverts that a user has bookmarked.
 * Users can view advert details by clicking on the title and remove adverts from bookmarks.
 */
public class BookmarkedAdvertsScreen extends JFrame {
    private JPanel mainPanel;
    private JPanel advertsPanel;
    private JButton returnButton;
    private ArrayList<BookmarkedAdvertPanel> bookmarkedAdverts;

    /**
     * @param bookmarkedAdverts list of adverts that the user has bookmarked
     */
    public BookmarkedAdvertsScreen(ArrayList<Advert> bookmarkedAdverts) {
        setTitle("Bookmarked Adverts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents(bookmarkedAdverts);
        
        setVisible(true);
    }
    
    /**
     * @param adverts 
     */
    private void initComponents(ArrayList<Advert> adverts) {
        mainPanel = new JPanel(new BorderLayout());
        
        advertsPanel = new JPanel();
        advertsPanel.setLayout(new BoxLayout(advertsPanel, BoxLayout.Y_AXIS));
        
        bookmarkedAdverts = new ArrayList<>();
        for (Advert advert : adverts) {
            BookmarkedAdvertPanel panel = new BookmarkedAdvertPanel(
                advert.title, 
                advert.price, 
                advert.description, 
                advert.imagePath
            );
            bookmarkedAdverts.add(panel);
            advertsPanel.add(panel);
            advertsPanel.add(Box.createVerticalStrut(10)); // Add spacing between adverts
        }
        
        JScrollPane scrollPane = new JScrollPane(advertsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel returnButtonPanel = new JPanel();
        returnButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        returnButton = new JButton("RETURN");
        returnButton.setBackground(new Color(165, 42, 42)); // Dark red color
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton.setPreferredSize(new Dimension(100, 30));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
        
        returnButtonPanel.add(returnButton);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(returnButtonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public void refreshScreen() {
        advertsPanel.removeAll();
        for (BookmarkedAdvertPanel panel : bookmarkedAdverts) {
            advertsPanel.add(panel);
            advertsPanel.add(Box.createVerticalStrut(10));
        }
        advertsPanel.revalidate();
        advertsPanel.repaint();
    }
    
    private class BookmarkedAdvertPanel extends JPanel {
        private JLabel imageLabel;
        private JLabel titleLabel;
        private JLabel priceLabel;
        private JButton deleteBookmarkButton;
        private String description;
        private String imagePath;

        public BookmarkedAdvertPanel(String title, double price, String description, String imagePath) {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            setPreferredSize(new Dimension(750, 80));
            setMaximumSize(new Dimension(750, 80));
            
            this.description = description;
            this.imagePath = imagePath;

            JPanel imagePanel = new JPanel();
            imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            imagePanel.setPreferredSize(new Dimension(100, 60));
            
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setText("IMAGE");
            imageLabel.setHorizontalTextPosition(JLabel.CENTER);
            imageLabel.setVerticalTextPosition(JLabel.CENTER);
            imagePanel.add(imageLabel);
            add(imagePanel, BorderLayout.WEST);

            JPanel titlePanel = new JPanel();
            titlePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            titlePanel.setLayout(new BorderLayout());
            titleLabel = new JLabel("Name and the title of the advert", JLabel.CENTER);
            titleLabel.setText(title);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            add(titlePanel, BorderLayout.CENTER);
            
            JPanel pricePanel = new JPanel();
            pricePanel.setPreferredSize(new Dimension(80, 60));
            pricePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            priceLabel = new JLabel("PRICE", JLabel.CENTER);
            priceLabel.setText(String.format("%.2f", price));
            pricePanel.add(priceLabel);
            add(pricePanel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setPreferredSize(new Dimension(120, 60));
            buttonPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            deleteBookmarkButton = new JButton("Delete Bookmark");
            deleteBookmarkButton.setPreferredSize(new Dimension(115, 30));
            buttonPanel.add(deleteBookmarkButton);
            add(buttonPanel, BorderLayout.EAST);

            titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new AdvertDetailsScreen(titleLabel.getText(), description, imagePath);
                }
            });
            
            deleteBookmarkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int response = JOptionPane.showConfirmDialog(
                        BookmarkedAdvertPanel.this,
                        "Are you sure you want to remove this advert from bookmarks?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (response == JOptionPane.YES_OPTION) {
                        bookmarkedAdverts.remove(BookmarkedAdvertPanel.this);
                        refreshScreen();
                        JOptionPane.showMessageDialog(
                            BookmarkedAdvertPanel.this,
                            "Advert removed from bookmarks successfully!"
                        );
                    }
                }
            });
        }
    }
    
    /**
     * Test method to demonstrate the BookmarkedAdvertsScreen
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create sample bookmarked adverts
            ArrayList<Advert> adverts = new ArrayList<>();
            adverts.add(new Advert("Java Book", 90.0, "An in-depth book about Java programming.", "book.png", "Lecture Materials"));
            adverts.add(new Advert("Sweatshirt", 120.0, "Comfortable and soft, lightly used.", "sweat.png", "Clothing"));
            adverts.add(new Advert("Linear Algebra Notes", 30.0, "Detailed notes for MATH 225 with diagrams.", "notes.png", "Lecture Materials"));
            adverts.add(new Advert("Basys3 Board", 450.0, "Used in CS223 labs, fully functional.", "board.png", "Lecture Materials"));
            
            new BookmarkedAdvertsScreen(adverts);
        });
    }
}
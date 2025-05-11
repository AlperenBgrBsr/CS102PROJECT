import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class HelpScreen extends JFrame {
    private final Color BLUE_COLOR = new Color(21, 50, 80);
    private final Color RED_COLOR = new Color(151, 12, 16);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 18);
    private final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private JTabbedPane tabbedPane;
    
    public HelpScreen() {
        setTitle("BilMart Help Center");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("icons\\BilMartIcon.png").getImage());
        
        JPanel topPanel = createTopPanel();
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        tabbedPane.addTab("Getting Started", createGettingStartedPanel());
        tabbedPane.addTab("Advertisements", createAdvertisementsPanel());
        tabbedPane.addTab("User Profiles", createProfilesPanel());
        tabbedPane.addTab("Messages & Reviews", createMessagesPanel());
        tabbedPane.addTab("Bookmarks", createBookmarksPanel());
        tabbedPane.addTab("FAQ", createFAQPanel());
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BLUE_COLOR);
        panel.setPreferredSize(new Dimension(800, 80));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        ImageIcon logo = new ImageIcon("icons\\BilMartIcon.png");
        Image scaledImage = logo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        
        JLabel titleLabel = new JLabel("BilMart Help Center");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        panel.add(logoLabel);
        panel.add(titleLabel);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(RED_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 1));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
    
    private JScrollPane createGettingStartedPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Getting Started with BilMart");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea introText = createTextArea(
            "Welcome to BilMart, the marketplace exclusively for Bilkent University students!\n\n" +
            "BilMart allows you to buy and sell items within the Bilkent community. " +
            "Whether you're looking for textbooks, lecture materials, clothes, or other items, " +
            "BilMart makes it easy to connect with other students."
        );
        
        JLabel accountLabel = new JLabel("Creating an Account");
        accountLabel.setFont(HEADING_FONT);
        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea accountText = createTextArea(
            "1. Click on \"Don't have an account?\" on the login screen\n" +
            "2. Fill in your Bilkent University email (must end with @ug.bilkent.edu.tr)\n" +
            "3. Choose a username and password\n" +
            "4. Click \"Register\"\n" +
            "5. Check your email for a verification code\n" +
            "6. Enter the code to complete registration"
        );
        
        JLabel navigationLabel = new JLabel("Navigating the Application");
        navigationLabel.setFont(HEADING_FONT);
        navigationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea navigationText = createTextArea(
            "The top bar contains icons for:\n\n" +
            "• Home: Click on the BilMart logo to return to home screen\n" +
            "• Messages: View and send messages to other users\n" +
            "• Bookmarks: View advertisements you've bookmarked\n" +
            "• Add: Post a new advertisement\n" +
            "• Help: Open this help center\n" +
            "• Profile: View your profile and settings"
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(introText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(accountLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(accountText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(navigationLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(navigationText);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JScrollPane createAdvertisementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Managing Advertisements");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea introText = createTextArea(
            "BilMart makes it easy to post, find, and manage advertisements for items you want to sell or buy."
        );
        
        JLabel postingLabel = new JLabel("Posting an Advertisement");
        postingLabel.setFont(HEADING_FONT);
        postingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea postingText = createTextArea(
            "1. Click the '+' icon in the top bar\n" +
            "2. Enter a title for your advertisement\n" +
            "3. Select the type of item (Lecture Material, Cloth, or Other)\n" +
            "4. Enter the price in ₺\n" +
            "5. Add detailed information about your item\n" +
            "6. Upload an image of your item\n" +
            "7. Click \"Add Advert\"\n\n" +
            "Note: Each advertisement must have a unique title."
        );
        
        JLabel searchingLabel = new JLabel("Searching for Advertisements");
        searchingLabel.setFont(HEADING_FONT);
        searchingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea searchingText = createTextArea(
            "You can search for advertisements in several ways:\n\n" +
            "• Use the search bar on the home screen to search by keyword\n" +
            "• Click on category buttons (Books, Clothes) to view items in specific categories\n" +
            "• Filter advertisements by price range, item type, keyword, or seller username\n" +
            "• View all advertisements from a specific user by visiting their profile"
        );
        
        JLabel managingLabel = new JLabel("Managing Your Advertisements");
        managingLabel.setFont(HEADING_FONT);
        managingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea managingText = createTextArea(
            "To manage your advertisements:\n\n" +
            "1. Go to your profile by clicking your profile picture\n" +
            "2. Click \"Select to Edit Adverts\" to change availability status\n" +
            "3. Click \"Select to Delete Adverts\" to remove advertisements\n\n" +
            "You can set an advertisement as:\n" +
            "• Available: Visible to all users\n" +
            "• Unavailable: Hidden from search results"
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(introText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(postingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(postingText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(searchingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(searchingText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(managingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(managingText);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JScrollPane createProfilesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("User Profiles");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea introText = createTextArea(
            "Your profile helps other users learn about you and builds trust in the BilMart community."
        );
        
        JLabel yourProfileLabel = new JLabel("Your Profile");
        yourProfileLabel.setFont(HEADING_FONT);
        yourProfileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea yourProfileText = createTextArea(
            "To access your profile, click on your profile picture in the top bar. On your profile, you can:\n\n" +
            "• Set your availability status (Available/Unavailable)\n" +
            "• Edit your profile picture\n" +
            "• See your current rating and reviews\n" +
            "• View your current advertisements\n" +
            "• Manage your advertisements (edit/delete)\n" +
            "• View advertisements you've previously viewed"
        );
        
        JLabel otherProfilesLabel = new JLabel("Viewing Other Profiles");
        otherProfilesLabel.setFont(HEADING_FONT);
        otherProfilesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea otherProfilesText = createTextArea(
            "You can view other users' profiles by:\n\n" +
            "• Clicking on their username in an advertisement\n" +
            "• Searching for their username using the search bar in the top bar\n\n" +
            "On another user's profile, you can:\n" +
            "• View their availability status, email, and rating\n" +
            "• See reviews left by other users\n" +
            "• Rate the user (1-5 stars)\n" +
            "• Leave a review\n" +
            "• Add them to your contacts\n" +
            "• View their available advertisements"
        );
        
        JLabel ratingLabel = new JLabel("Rating System");
        ratingLabel.setFont(HEADING_FONT);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea ratingText = createTextArea(
            "The rating system helps maintain a trustworthy community:\n\n" +
            "• Users can rate each other on a scale of 1-5 stars\n" +
            "• You can only rate a user once\n" +
            "• You can view all ratings for a user by clicking the \"X Ratings\" button\n" +
            "• You can delete your rating for a user if needed\n" +
            "• A user's average rating is displayed on their profile"
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(introText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(yourProfileLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(yourProfileText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(otherProfilesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(otherProfilesText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(ratingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ratingText);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JScrollPane createMessagesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Messages & Reviews");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea introText = createTextArea(
            "BilMart allows you to communicate with other users through messages and reviews."
        );
        
        JLabel contactingLabel = new JLabel("Contacting Sellers");
        contactingLabel.setFont(HEADING_FONT);
        contactingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea contactingText = createTextArea(
            "To contact a seller about an advertisement:\n\n" +
            "1. Click on an advertisement to view details\n" +
            "2. Click the \"Reach Seller\" button\n" +
            "3. The seller will receive an email with your contact information\n\n" +
            "Note: You cannot contact yourself for your own advertisements."
        );
        
        JLabel reviewsLabel = new JLabel("Leaving Reviews");
        reviewsLabel.setFont(HEADING_FONT);
        reviewsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea reviewsText = createTextArea(
            "Reviews help build a trustworthy community:\n\n" +
            "1. Navigate to another user's profile\n" +
            "2. Enter your review in the text field at the bottom of the review section\n" +
            "3. Click \"Send\" to post your review\n\n" +
            "All reviews are public and visible on the user's profile."
        );
        
        JLabel contactsLabel = new JLabel("Managing Contacts");
        contactsLabel.setFont(HEADING_FONT);
        contactsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea contactsText = createTextArea(
            "To add a user to your contacts:\n\n" +
            "1. Navigate to their profile\n" +
            "2. Click \"Add to Contacts\"\n\n" +
            "To remove a user from your contacts:\n\n" +
            "1. Navigate to their profile\n" +
            "2. Click \"Remove Contact\""
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(introText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(contactingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(contactingText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(reviewsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(reviewsText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(contactsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(contactsText);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JScrollPane createBookmarksPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Bookmarks");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea introText = createTextArea(
            "Bookmarks allow you to save advertisements you're interested in for easy access later."
        );
        
        JLabel addingLabel = new JLabel("Adding Bookmarks");
        addingLabel.setFont(HEADING_FONT);
        addingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea addingText = createTextArea(
            "To bookmark an advertisement:\n\n" +
            "1. Browse or search for advertisements\n" +
            "2. Click the empty bookmark icon next to an advertisement\n\n" +
            "Note: You cannot bookmark your own advertisements."
        );
        
        JLabel viewingLabel = new JLabel("Viewing Bookmarks");
        viewingLabel.setFont(HEADING_FONT);
        viewingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea viewingText = createTextArea(
            "To view your bookmarked advertisements:\n\n" +
            "1. Click the bookmark icon in the top bar\n" +
            "2. You'll see a list of all advertisements you've bookmarked\n" +
            "3. Click on an advertisement title to view details"
        );
        
        JLabel removingLabel = new JLabel("Removing Bookmarks");
        removingLabel.setFont(HEADING_FONT);
        removingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea removingText = createTextArea(
            "To remove a bookmark:\n\n" +
            "• In the advertisement list, click the filled bookmark icon to remove it\n" +
            "• On the Bookmarks page, click the \"Delete Bookmark\" button next to an advertisement"
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(introText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(addingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addingText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(viewingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(viewingText);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(removingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(removingText);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JScrollPane createFAQPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Frequently Asked Questions");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // FAQ 1
        JLabel q1 = new JLabel("Q: I forgot my password. How can I reset it?");
        q1.setFont(HEADING_FONT);
        q1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a1 = createTextArea(
            "A: On the login screen, click the \"Forgot your password?\" button. " +
            "Enter your username and email. You'll receive a verification code via email. " +
            "Enter the code and your new password to reset it."
        );
        
        // FAQ 2
        JLabel q2 = new JLabel("Q: Can I edit an advertisement after posting it?");
        q2.setFont(HEADING_FONT);
        q2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a2 = createTextArea(
            "A: Currently, you cannot edit the title, price, or details of an advertisement after posting. " +
            "However, you can change its availability status or delete it and create a new one."
        );
        
        // FAQ 3
        JLabel q3 = new JLabel("Q: Why can't I see some advertisements?");
        q3.setFont(HEADING_FONT);
        q3.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a3 = createTextArea(
            "A: There are several possible reasons:\n" +
            "• The advertisement has been marked as unavailable by the seller\n" +
            "• The seller has set their status to unavailable\n" +
            "• Your search or filter criteria exclude those advertisements\n" +
            "• The advertisement has been deleted"
        );
        
        // FAQ 4
        JLabel q4 = new JLabel("Q: Can I delete a review or rating I've left for another user?");
        q4.setFont(HEADING_FONT);
        q4.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a4 = createTextArea(
            "A: You can delete a rating you've given to another user by visiting their profile, " +
            "clicking on the \"X Ratings\" button, and then clicking \"Delete Rating\" next to your rating. " +
            "Currently, you cannot delete reviews you've left."
        );
        
        // FAQ 5
        JLabel q5 = new JLabel("Q: How do I change my profile picture?");
        q5.setFont(HEADING_FONT);
        q5.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a5 = createTextArea(
            "A: Go to your profile by clicking your profile picture in the top bar. " +
            "Click the \"Edit Profile Picture\" button. You can either upload a new image " +
            "or reset to the default profile picture."
        );
        
        // FAQ 6
        JLabel q6 = new JLabel("Q: What does the availability status mean?");
        q6.setFont(HEADING_FONT);
        q6.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a6 = createTextArea(
            "A: Your availability status indicates whether you're actively using BilMart:\n" +
            "• Available (home icon): Your profile and advertisements are visible to all users\n" +
            "• Unavailable (away icon): Your profile is visible, but your advertisements are hidden"
        );
        
        // FAQ 7
        JLabel q7 = new JLabel("Q: Can I use BilMart if I'm not a Bilkent University student?");
        q7.setFont(HEADING_FONT);
        q7.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea a7 = createTextArea(
            "A: No, BilMart is exclusively for Bilkent University students. " +
            "You need a valid Bilkent University email address (ending with @ug.bilkent.edu.tr) to register."
        );
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(q1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a1);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q2);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a2);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q3);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a3);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q4);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a4);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q5);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a5);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q6);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a6);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(q7);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(a7);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    
    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(CONTENT_FONT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(null);
        textArea.setBorder(null);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        return textArea;
    }
    
}
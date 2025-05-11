import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ItemsBar extends JPanel implements ActionListener{

    ImageIcon bookMarkIcon = new ImageIcon("icons\\BookmarkIcon.png");
    ImageIcon addAdvertIcon = new ImageIcon("icons\\AddAdvertIcon.png");
    ImageIcon HelpIcon = new ImageIcon("icons\\HelpIcon.png");
    ImageIcon MessageIcon = new ImageIcon("icons\\MessageIcon.png");
    ImageIcon UserIcon = new ImageIcon("icons\\UserIcon.png");
    ImageIcon BilMartIcon = new ImageIcon("icons\\BilMartIcon.png");
    ImageIcon BilMartWriting = new ImageIcon("icons\\BilMart.png");
    ImageIcon SearchIcon = new ImageIcon("icons\\SearchIcon.png");
    ImageIcon SearchBarImage = new ImageIcon("icons\\SearchBar.png");

    JButton bilMartText; 
    JLabel bilMartLogo; 

    JButton messageButton;
    JButton bookmarkButton;
    JButton addButton;
    JButton helpButton;
    JButton profileButton;
    
    JButton searchButton;
    JTextField searchField;
    
    JPanel leftPart; //BilMart text and the Icon
    JPanel middlePart; //Username search bar
    JPanel rightPart; // Buttons

    private final int ICON_WIDTH = 38;
    private final int ICON_HEIGHT = 38;
    private final Color BLUE_COLOR = new Color(21,50,80);

    boolean hasSearchBar;
    boolean isOnlyAddScreen;
 
    public ItemsBar(boolean hasSearchBar /*If true has a username searchbar*/ ) {
        this.setPreferredSize(new Dimension(1024, 100));
        this.setBackground(BLUE_COLOR);
        this.setLayout(new BorderLayout());
        this.isOnlyAddScreen = true;
        this.hasSearchBar = hasSearchBar;

        leftPart = new JPanel();
        leftPart.setBackground(BLUE_COLOR);
        leftPart.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));

        middlePart = new JPanel();
        middlePart.setBackground(BLUE_COLOR);
  
        rightPart = new JPanel();
        rightPart.setLayout(new GridLayout(1,5,5,0));
        rightPart.setBackground(BLUE_COLOR);
  
        //BilMartWriting
        bilMartText = new JButton();
        bilMartText.setIcon(resizeIcon(BilMartWriting, 138, 30));
        bilMartText.setContentAreaFilled(false);
        bilMartText.setBorderPainted(false);
        bilMartText.setFocusPainted(false);
        bilMartText.addActionListener(this);
        leftPart.add(bilMartText);

        //BilMartLogo
        bilMartLogo = new JLabel();
        bilMartLogo.setIcon(resizeIcon(BilMartIcon,ICON_WIDTH,ICON_HEIGHT));
        leftPart.add(bilMartLogo);

        //SearchBar
        if (hasSearchBar) {
            addSearchBar();
        }

        //messageButton
        messageButton = new JButton();
        messageButton.setContentAreaFilled(false);
        messageButton.setBorderPainted(false);
        messageButton.setFocusPainted(false);
        messageButton.addActionListener(this);
        messageButton.setIcon(resizeIcon(MessageIcon, ICON_WIDTH, ICON_HEIGHT));
        rightPart.add(messageButton);

        //bookmarkButton
        bookmarkButton = new JButton();
        bookmarkButton.setIcon(resizeIcon(bookMarkIcon, ICON_WIDTH, ICON_HEIGHT));
        bookmarkButton.setContentAreaFilled(false);
        bookmarkButton.setBorderPainted(false);
        bookmarkButton.setFocusPainted(false);
        bookmarkButton.addActionListener(this);
        rightPart.add(bookmarkButton);
        
        //darkModeButton
        addButton = new JButton();
        addButton.setIcon(resizeIcon(addAdvertIcon, ICON_WIDTH, ICON_HEIGHT));
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.addActionListener(this);
        rightPart.add(addButton);

        //helpButton
        helpButton = new JButton();
        helpButton.setIcon(resizeIcon(HelpIcon, ICON_WIDTH, ICON_HEIGHT));
        helpButton.setContentAreaFilled(false);
        helpButton.setBorderPainted(false);
        helpButton.setIcon(resizeIcon(HelpIcon, ICON_WIDTH, ICON_HEIGHT));
        helpButton.setFocusPainted(false);
        helpButton.addActionListener(this);
        rightPart.add(helpButton);
        
        //profileButton
        profileButton = new JButton();
        profileButton.setIcon(resizeIcon(new ImageIcon(MainFile.currentUserForAll.getProfilePicture()), ICON_WIDTH, ICON_HEIGHT));
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(false);
        profileButton.setFocusPainted(false);
        profileButton.addActionListener(this);
        rightPart.add(profileButton);
        
        add(leftPart, BorderLayout.WEST);
        add(middlePart);
        add(rightPart, BorderLayout.EAST);
    }

    public void refreshProfilePicture() {
        profileButton.setIcon(resizeIcon(new ImageIcon(MainFile.currentUserForAll.getProfilePicture()), ICON_WIDTH, ICON_HEIGHT));
    }

    public void removeSearchBar() {
        middlePart.removeAll();
    }

    public void addSearchBar() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout()); // centers contents
        wrapperPanel.setPreferredSize(new Dimension(1024, 90));
        wrapperPanel.setOpaque(false); // transparent to show background if needed
    
        // Background label as container
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setPreferredSize(new Dimension(330, 42));
        backgroundLabel.setIcon(resizeIcon(SearchBarImage, 330, 42));
    
        // Transparent panel on top of background for field + button
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    
        searchField = new JTextField("Enter Username");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setOpaque(false);
        searchField.setCaretColor(Color.BLACK);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Enter Username")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Enter Username");
                }
            }
        });
    
        searchButton = new JButton(resizeIcon(SearchIcon, 35, 35));
        searchButton.setPreferredSize(new Dimension(45, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);
    
        overlayPanel.add(searchField, BorderLayout.CENTER);
        overlayPanel.add(searchButton, BorderLayout.EAST);
    
        backgroundLabel.add(overlayPanel);
        wrapperPanel.add(backgroundLabel);

        
        middlePart.add(wrapperPanel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }

    private void createHelpScreen() {
        HelpScreen helpScreen = new HelpScreen();
        helpScreen.setTitle("Help Screen");
        helpScreen.pack();
        helpScreen.setVisible(true);
    }

    public void setIsOnlyAddScene(boolean a) {
        isOnlyAddScreen = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookmarkButton) {
            HomeScreen.hm.changePanel(new BookmarksPanel(MainFile.currentUserForAll));
        }
        if (e.getSource() == searchButton) {

            ArrayList<String> allUsernames = new ArrayList<>();
            try {
                PreparedStatement allUsernamesStatement = Database.databaseConnection.prepareStatement("SELECT username FROM users");
                ResultSet allUsernamesRs = allUsernamesStatement.executeQuery();
                while (allUsernamesRs.next()){

                    allUsernames.add(allUsernamesRs.getString("username"));

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if ( allUsernames.indexOf(searchField.getText()) < 0){
                JOptionPane.showMessageDialog(null, "There is no users wih that username", "User Not Found", JOptionPane.ERROR_MESSAGE);
            }
            else{
                HomeScreen.hm.changePanel(new ProfilePanel(searchField.getText(), MainFile.currentUserForAll));
            }
            
        }
        if (e.getSource() == helpButton) {
            createHelpScreen();
        }

        if (e.getSource() == profileButton) {
            //ProfilePanel profilePage = new ProfilePanel(LoginScreen.getCurrentUser().getUsername(), LoginScreen.getCurrentUser()); //if started by loginscreen main method
            ProfilePanel profilePage = new ProfilePanel(MainFile.currentUserForAll.getUsername(), MainFile.currentUserForAll); //if started by homescreen main method
            HomeScreen.hm.items.addSearchBar();
            HomeScreen.hm.changePanel(profilePage); //Will change these later
        }
        if (e.getSource() == addButton) {
            if (isOnlyAddScreen) {
                new AddAdvertScene();
                isOnlyAddScreen = false;
            }
            
        }
        if (e.getSource() == bilMartText) {
            HomeScreen.hm.items.removeSearchBar();
            HomeScreen.hm.reloadHomeScreenPanel();
        }

        
    }

}


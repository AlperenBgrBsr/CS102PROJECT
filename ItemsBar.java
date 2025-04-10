import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To Do:
 * - 
 * Notes:
 * Currently the methods of the buttons setFocusPainted(false); is optional
 */

public class ItemsBar extends JPanel implements ActionListener{

    ImageIcon bookMarkIcon = new ImageIcon("icons\\BookmarkIcon.png");
    ImageIcon darkModeIcon = new ImageIcon("icons\\DarkModeIcon.png");
    ImageIcon HelpIcon = new ImageIcon("icons\\HelpIcon.png");
    ImageIcon MessageIcon = new ImageIcon("icons\\MessageIcon.png");
    ImageIcon UserIcon = new ImageIcon("icons\\UserIcon.png");
    ImageIcon BilMartIcon = new ImageIcon("icons\\BilMartIcon.png");
    ImageIcon BilMartWriting = new ImageIcon("icons\\BilMart.png");
    ImageIcon SearchIcon = new ImageIcon("icons\\SearchIcon.png");
    ImageIcon SearchBarImage = new ImageIcon("icons\\SearchBar.png");

    JLabel bilMartLabel; 
    JLabel bilMartLogo; 

    JButton messageButton;
    JButton bookmarkButton;
    JButton darkModeButton;
    JButton helpButton;
    JButton profileButton;
    
    JButton searchButton;
    JTextField searchField;
    
    JPanel leftPart; //BilMart text and the Icon
    JPanel middlePart; //Username search bar
    JPanel rightPart; // Buttons

    private final int ICON_WIDTH = 31;
    private final int ICON_HEIGHT = 35;
    private final Color BLUE_COLOR = new Color(21,50,80);

    public ItemsBar() {
        this.setPreferredSize(new Dimension(1024, 90));
        this.setBackground(BLUE_COLOR);
        this.setLayout(new BorderLayout());
        
        leftPart = new JPanel();
        leftPart.setBackground(BLUE_COLOR);
        leftPart.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 25));

        middlePart = new JPanel();
        middlePart.setBackground(BLUE_COLOR);
  
        rightPart = new JPanel();
        rightPart.setLayout(new GridLayout(1,5,5,0));
        rightPart.setBackground(BLUE_COLOR);
  
        //BilMartWriting
        bilMartLabel = new JLabel();
        bilMartLabel.setIcon(resizeIcon(BilMartWriting, 138, 30));
        leftPart.add(bilMartLabel);

        //BilMartLogo
        bilMartLogo = new JLabel();
        bilMartLogo.setIcon(resizeIcon(BilMartIcon,ICON_WIDTH,ICON_HEIGHT));
        leftPart.add(bilMartLogo);

        //---------------------
        //------SearchBar------
        //---------------------
        final int yOffset = 30;
        final int xOffset = 35;

        middlePart.setLayout(null); // For manual positioning

        JLabel searchBarBackground = new JLabel();
        searchBarBackground.setBounds(xOffset, yOffset - 5, 400, 40); // x, y, width, height
        searchBarBackground.setIcon(resizeIcon(SearchBarImage, 350, 40));

        // Text field setup
        searchField = new JTextField("Enter username");
        searchField.setBounds(xOffset + 15, yOffset, 250, 30); // Positioned inside the background
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setOpaque(false); // Transparent background
        searchField.setCaretColor(Color.BLACK); // Optional: Black cursor
        searchField.setBorder(BorderFactory.createEmptyBorder());

        // Search icon button
        searchButton = new JButton();
        searchButton.setBounds(xOffset + 300, yOffset, 30, 30);
        searchButton.setIcon(resizeIcon(SearchIcon, ICON_WIDTH - 7, ICON_HEIGHT - 7));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this); // Optional

        // Add in correct order
        middlePart.add(searchButton);
        middlePart.add(searchField);
        middlePart.add(searchBarBackground);

        //=============
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
        darkModeButton = new JButton();
        darkModeButton.setIcon(resizeIcon(darkModeIcon, ICON_WIDTH, ICON_HEIGHT));
        darkModeButton.setContentAreaFilled(false);
        darkModeButton.setBorderPainted(false);
        darkModeButton.setFocusPainted(false);
        darkModeButton.addActionListener(this);
        rightPart.add(darkModeButton);

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
        profileButton.setIcon(resizeIcon(UserIcon, ICON_WIDTH, ICON_HEIGHT));
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(false);
        profileButton.setFocusPainted(false);
        profileButton.addActionListener(this);
        rightPart.add(profileButton);
        
        add(leftPart, BorderLayout.WEST);
        add(middlePart);
        add(rightPart, BorderLayout.EAST);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == bookmarkButton) {
            System.out.println("aaa");
       }
       if (e.getSource() == searchButton) {
            System.out.println("b");
       }
    }
}

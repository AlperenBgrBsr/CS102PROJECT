import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class HomeScreen extends JFrame implements ActionListener{

    //Panels
    JPanel homePagePanel;      // Holds everything
    JPanel currentPanel;

    //Search Bar Items
    JButton searchButton;
    JTextField searchField;

    //Buttons
    JButton booksButton;
    JButton clothesButton;
    public static HomeScreen hm;
    ItemsBar items;

    public HomeScreen() 
    {
        this.setTitle("BilMart HomePage");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024,1080);
        this.setIconImage(new ImageIcon("icons\\BilMartIcon.png").getImage());
        //ItemsBar
        items = new ItemsBar(false);
        items.setBounds(0, 0, 1024, 90);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setSize(new Dimension(1024,100));
        topPanel.add(items, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);
        
        homePagePanel = new JPanel();
        homePagePanel.setPreferredSize(new Dimension(1024,550));
        homePagePanel.add(searchBar());
        homePagePanel.add(categoriesText());
        homePagePanel.add(categories());
        currentPanel = homePagePanel;
        this.add(currentPanel);
        this.setVisible(true);
        JFrame frame = this;    
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleComponentWidths(currentPanel, frame.getWidth() / 1024.0);
            }
        });
    }

    public static void scaleComponentWidths(Container container, double scaleFactor) {
        if (container.getLayout() == null) {
            for (Component comp : container.getComponents()) {
                if (comp instanceof JSlider || comp instanceof JScrollBar || comp instanceof JComboBox|| comp instanceof JScrollPane) {
                    continue;
                }

                if (comp instanceof JComponent jComp) {
                    if (jComp.getClientProperty("originalBounds") == null) {
                        jComp.putClientProperty("originalBounds", jComp.getBounds());
                    }

                    Rectangle original = (Rectangle) jComp.getClientProperty("originalBounds");
                    int newWidth = (int) (original.width * scaleFactor);
                    int newX = (int) (original.x * scaleFactor);
                    jComp.setBounds(newX, original.y, newWidth, original.height);
                }

                // If component is a container, recurse
                if (comp instanceof Container childContainer) {
                    scaleComponentWidths(childContainer, scaleFactor);
                }
            }
        }
        else if (!(container.getLayout() instanceof BorderLayout || container.getLayout() instanceof GridBagLayout || container.getLayout() instanceof BoxLayout)){ 
            for (Component comp : container.getComponents()) {
                if (comp instanceof JSlider || comp instanceof JScrollBar || comp instanceof JComboBox || comp instanceof JScrollPane) {
                    continue;
                }


                if (comp instanceof JComponent jComp) {
                    if (jComp.getClientProperty("originalBounds") == null) {
                        jComp.putClientProperty("originalBounds", jComp.getBounds());
                    }

                    Rectangle original = (Rectangle) jComp.getClientProperty("originalBounds");
                    int newWidth = (int) (original.width * scaleFactor);
                    int newX = (int) (original.x * scaleFactor);
                    comp.setPreferredSize(new Dimension((int)(newWidth), original.height));

                }

                if (comp instanceof Container childContainer) {
                    scaleComponentWidths(childContainer, scaleFactor);
                }
            }
        }
        else if (container.getLayout() instanceof BorderLayout) {
            for (Component comp : container.getComponents()) {
                if (comp instanceof JSlider || comp instanceof JScrollBar || comp instanceof JComboBox || comp instanceof JScrollPane) {
                    continue;
                }

                if (comp instanceof JComponent jComp) {
                    if (jComp.getClientProperty("originalPreferredSize") == null) {
                        jComp.putClientProperty("originalPreferredSize", jComp.getPreferredSize());
                    }

                    Dimension original = (Dimension) jComp.getClientProperty("originalPreferredSize");
                    int newWidth = (int) (original.width * scaleFactor);
                    jComp.setPreferredSize(new Dimension(newWidth, original.height));
                }

                if (comp instanceof Container childContainer) {
                    scaleComponentWidths(childContainer, scaleFactor);
                }
            }

            container.revalidate();
            container.repaint();
        }
    }
    
    public void changePanel(JPanel panelToChange) {
        this.remove(currentPanel);             
        currentPanel = panelToChange;           
        this.add(currentPanel);                 
        scaleComponentWidths(currentPanel, this.getWidth() / 1024.0);
        this.revalidate();                      
        this.repaint();                         
    }

    public void reloadHomeScreenPanel() {
        this.remove(currentPanel);             
        currentPanel = homePagePanel;           
        this.add(currentPanel);                 
        this.revalidate();                      
        this.repaint();  
    }
    
    private JPanel searchBar() {
        ImageIcon searchIcon = new ImageIcon("icons\\SearchIcon.png");
        ImageIcon searchBarImage = new ImageIcon("icons\\SearchBar.png");
    
        JPanel wrapperPanel = new JPanel(new GridBagLayout()); // centers contents
        wrapperPanel.setPreferredSize(new Dimension(1024, 200));
    
        // Background label as container
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setPreferredSize(new Dimension(500, 50));
        backgroundLabel.setIcon(resizeIcon(searchBarImage, 500, 50));
    
        // Transparent panel on top of background for field + button
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    
        searchField = new JTextField("Search for any item");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setOpaque(false);
        searchField.setCaretColor(Color.BLACK);
        searchField.setForeground(Color.black);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().toLowerCase().equals("search for any item")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Search for any item");
                }
            }
        });
    
        searchButton = new JButton(resizeIcon(searchIcon, 35, 35));
        searchButton.setPreferredSize(new Dimension(45, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);
    
        overlayPanel.add(searchField, BorderLayout.CENTER);
        overlayPanel.add(searchButton, BorderLayout.EAST);
    
        backgroundLabel.add(overlayPanel);
        wrapperPanel.add(backgroundLabel);
    
        return wrapperPanel;
    }
    
    private JPanel categoriesText() {
        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelWrapper.setPreferredSize(new Dimension(1980,60));

        JLabel expCategoriesText = new JLabel();
        expCategoriesText.setIcon(resizeIcon(new ImageIcon("icons\\ExploreCategoriesText.png"), 180, 20));     
        expCategoriesText.setForeground(Color.WHITE);  
        labelWrapper.add(expCategoriesText);

        return labelWrapper;
    }

    private JPanel categories() {
        ImageIcon booksButtonIcon = new ImageIcon("icons\\booksButton.png");
        ImageIcon clothesButtonIcon = new ImageIcon("icons\\clothesButton.png");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setSize(new Dimension(1980,250));
        buttonsPanel.setLocation(0,250);

        booksButton = new JButton() {
            @Override
            public boolean contains(int x, int y) {
                int radius = Math.min(getWidth(), getHeight()) / 2;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                double dx = x - centerX;
                double dy = y - centerY;

                return dx * dx + dy * dy <= radius * radius;
            }
        };
        booksButton.setIcon(resizeIcon(booksButtonIcon, 235, 235));
        booksButton.setContentAreaFilled(false);
        booksButton.setBorderPainted(false);
        booksButton.setFocusPainted(false);
        booksButton.addActionListener(this); 

        clothesButton = new JButton() {
            @Override
            public boolean contains(int x, int y) {
                int radius = Math.min(getWidth(), getHeight()) / 2;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                double dx = x - centerX;
                double dy = y - centerY;

                return dx * dx + dy * dy <= radius * radius;
            }
        };
        clothesButton.setIcon(resizeIcon(clothesButtonIcon, 235, 235));
        clothesButton.setContentAreaFilled(false);
        clothesButton.setBorderPainted(false);
        clothesButton.setFocusPainted(false);
        clothesButton.setBackground(new Color(21,50,80));
        booksButton.setBackground(new Color(21,50,80));
        clothesButton.addActionListener(this); 

        buttonsPanel.add(booksButton);
        buttonsPanel.add(clothesButton);
        return buttonsPanel;
    }
    
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            if (searchField.getText().trim().equalsIgnoreCase("Search for any item"))
                searchField.setText("");
            hm.changePanel(new AdvertViewPanel(Integer.MIN_VALUE, Integer.MAX_VALUE, "", searchField.getText(), "", MainFile.currentUserForAll));
        }
        if (e.getSource() == clothesButton) {
            hm.changePanel(new AdvertViewPanel(Integer.MIN_VALUE, Integer.MAX_VALUE, "Cloth", "", "", MainFile.currentUserForAll));
        }
        if (e.getSource() == booksButton) {
            hm.changePanel(new AdvertViewPanel(Integer.MIN_VALUE, Integer.MAX_VALUE, "Lecture Material", "", "", MainFile.currentUserForAll));
        }
    }

    public ItemsBar getItemsBar() {
        return items;
    }
}

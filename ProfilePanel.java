import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;




public class ProfilePanel extends JPanel{


    private Image emptyStar;
    private Image oneQuarterStar;
    private Image halfStar;
    private Image threeQuarterStar;
    private Image fullStar;
    private String username;
    private User currentUser;
    private int ratingForAddRatingFrame;
    private static String otherProfileUsername;
    private static Rating currentUsersRating;
    
    int scale = 1024;
  

    public ProfilePanel(String username, User currentUser){ // These parameters are to see if the user is looking at their own profile or other's profile.
        this.username = username;
        this.currentUser = currentUser;
        this.setLayout(null);
        this.setBackground(Color.white);
        otherProfileUsername = username;

        //Refresh Button
        JButton refreshButton = new JButton();
        ImageIcon refreshIcon = new ImageIcon("icons\\refreshIcon.png");
        Image refreshIconImage = refreshIcon.getImage();
        refreshIconImage = refreshIconImage.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newRefreshIcon = new ImageIcon(refreshIconImage);
        refreshButton.setIcon(newRefreshIcon);
        refreshButton.setFocusable(false);
        refreshButton.setBounds(10 ,10,50,50);//CHANGE THIS PLACE LATER!
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                HomeScreen.hm.changePanel(new ProfilePanel(username, currentUser));
                HomeScreen.hm.items.refreshProfilePicture();
            }
            
        });

        try{
            
            emptyStar = ImageIO.read(new File("icons\\emptystar.png")); 
            oneQuarterStar = ImageIO.read(new File("icons\\onequarterstar.png")); 
            halfStar = ImageIO.read(new File("icons\\halfstar.png"));
            threeQuarterStar = ImageIO.read(new File("icons\\threequarterstar.png"));  
            fullStar = ImageIO.read(new File("icons\\fullstar.png"));           
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Image is not loaded", "ERROR!",JOptionPane.ERROR_MESSAGE);
        }

        
        

        //Own Profile
        if ( username.equals(currentUser.getUsername())){

            //Email Label
            JLabel emailLabel = new JLabel(currentUser.getEmail());
            emailLabel.setBounds(280,125,400,20);
            emailLabel.setFont(new Font("Arias", Font.BOLD, 15));

            //Username Label
            JLabel usernameLabel = new JLabel(currentUser.getUsername());
            usernameLabel.setBounds(136,205,300,30);
            usernameLabel.setFont(new Font("Arias", Font.BOLD, 18));

            //Select Status
            ButtonGroup buttonGroup = new ButtonGroup();
            JToggleButton homeButton = new JToggleButton();

            ImageIcon homeIcon = new ImageIcon("icons\\homeIcon.png");
            Image homeIconImage = homeIcon.getImage();
            homeIconImage = homeIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newHomeIcon = new ImageIcon(homeIconImage);
            homeButton.setIcon(newHomeIcon);
            homeButton.setContentAreaFilled(false);
            homeButton.setBorderPainted(false);
            homeButton.setFocusPainted(false);
            homeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentUser.updateAvailability(true);
                    //Changing availability in database 
                    //--------------------------------
                    try {
                        PreparedStatement changeAvailability = Database.databaseConnection.prepareStatement("UPDATE users SET available = true WHERE username = ?");
                        changeAvailability.setString(1, currentUser.getUsername());
                        changeAvailability.executeUpdate();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    refreshButton.doClick();
                    //--------------------------------

                    JOptionPane.showMessageDialog(null, "You have set yourself as available", "Availability", JOptionPane.INFORMATION_MESSAGE,newHomeIcon);
                }
                
            });
            homeButton.setBounds(700,140, 60,40);

            JToggleButton awayButton = new JToggleButton();
            ImageIcon awayIcon = new ImageIcon("icons\\awayIcon.png");
            Image awayIconImage = awayIcon.getImage();
            awayIconImage = awayIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newAwayIcon = new ImageIcon(awayIconImage);
            awayButton.setIcon(newAwayIcon);
            awayButton.setContentAreaFilled(false);
            awayButton.setBorderPainted(false);
            awayButton.setFocusPainted(false);
            awayButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentUser.updateAvailability(false);
                    //Changing availability in database 
                    //--------------------------------
                    try {
                        PreparedStatement changeAvailability = Database.databaseConnection.prepareStatement("UPDATE users SET available = false WHERE username = ?");
                        changeAvailability.setString(1, currentUser.getUsername());
                        changeAvailability.executeUpdate();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    refreshButton.doClick();
                    //--------------------------------
                    JOptionPane.showMessageDialog(null, "You have set yourself as unavailable", "Availability", JOptionPane.INFORMATION_MESSAGE,newAwayIcon);
                }
                
            });
            awayButton.setBounds(760,140, 60,40);
            buttonGroup.add(homeButton);
            buttonGroup.add(awayButton);

            JLabel selectStatusLabel = new JLabel("Select Status");
            selectStatusLabel.setBounds(700,110,200,30);
            selectStatusLabel.setFont(new Font("Arial",Font.BOLD,20));

            JLabel statusPicture = new JLabel();
            boolean currentUserAvailability = false;
            try {
                PreparedStatement checkAvailabilityStatement = Database.databaseConnection.prepareStatement("SELECT available FROM users WHERE username = ?");
                checkAvailabilityStatement.setString(1, currentUser.getUsername());
                ResultSet checkAvailabilityRs = checkAvailabilityStatement.executeQuery();
                if (checkAvailabilityRs.next()){
                    currentUserAvailability = checkAvailabilityRs.getBoolean("available");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if ( currentUserAvailability ){ 
                statusPicture = new JLabel(newHomeIcon);
                statusPicture.setBackground(Color.white);
            }
            else{
                statusPicture = new JLabel(newAwayIcon);
                statusPicture.setBackground(Color.white);
            }
            statusPicture.setOpaque(true);
            
            
            
            statusPicture.setBounds(320,80, 60,40);
            statusPicture.setFocusable(false);
            this.add(statusPicture);

            //Rating


            JLabel ratingLabel = new JLabel();
            String formattedRating = String.format("%.2f",currentUser.getRating());
            ratingLabel.setText(formattedRating);
            ratingLabel.setFont(new Font("Arial Black",Font.BOLD, 35));
            ratingLabel.setBounds(300,212,100,100);

            JButton ratingCountButton = new JButton(currentUser.getRatingAmount() + " Ratings");
            ratingCountButton.setBorder(new LineBorder(Color.BLACK,1));
            ratingCountButton.setBounds(170,310,130,40);
            ratingCountButton.setFont(new Font("Arial", Font.BOLD, 20));
            ratingCountButton.setFocusable(false);  
            ratingCountButton.setBackground(Color.white);
            ratingCountButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    JFrame ratingAmountFrame = new JFrame("Ratings Amount");
                    ratingAmountFrame.setLayout(new BorderLayout());
                    ratingAmountFrame.setPreferredSize(new Dimension(800,600));

                    JPanel ratingsPanel = new JPanel();
                    ratingsPanel.setLayout(new GridLayout(currentUser.getRatingAmount(),1));
                    ratingsPanel.setBackground(Color.white);
                    ArrayList<Rating> ratingsList = currentUser.getRatingsList();

                    for (int i = 0; i < ratingsList.size(); i++){

                        Rating currentRating = ratingsList.get(i);
                        JPanel sampleRatingPanel = new JPanel(){

                            protected void paintComponent(Graphics g2){
                                super.paintComponent(g2);
                                for (int i = 0; i < 5; i++){
                                    g2.drawImage(emptyStar, 200 + 90*i, 60, 90, 90, this);
                                }
                                for (int i = 0; i < currentRating.getRating(); i++){
                                    g2.drawImage(fullStar, 200 + 90*i, 60, 90, 90, this);
                                }
    
                            }


                        };
                        sampleRatingPanel.setLayout(null);
                        JLabel sender = new JLabel(currentRating.getSenderUsername());
                        sender.setFont(new Font("Arial", Font.BOLD, 20));
                        sender.setBounds(20,60,200,100);
                        sampleRatingPanel.setPreferredSize(new Dimension(750,200));
                        sampleRatingPanel.add(sender);
                        sampleRatingPanel.setBackground(Color.white);
                        ratingsPanel.add(sampleRatingPanel);



                    }

                    JScrollPane ratingAmountScrollPane = new JScrollPane(ratingsPanel);
                    ratingAmountScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                   
                    JPanel returnButtonPanel = new JPanel();
                    returnButtonPanel.setPreferredSize(new Dimension(800,100));
                    returnButtonPanel.setLayout(null);

                    JButton returnButton = new JButton("RETURN");
                    returnButton.setBorder(new LineBorder(Color.BLACK,1));
                    returnButton.setFont(new Font("Arial", Font.BOLD, 20));
                    returnButton.setFocusable(false);  
                    returnButton.setBackground(new Color(151,12,16));
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ratingAmountFrame.dispose();
                        }
                        
                    });
                    returnButton.setBounds(300,25,200,50);
                    returnButtonPanel.add(returnButton);
                    
                    ratingAmountFrame.setBackground(Color.white);
                    ratingAmountFrame.add(ratingAmountScrollPane, BorderLayout.CENTER);
                    ratingAmountFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    ratingAmountFrame.setResizable(false);
                    ratingAmountFrame.pack();
                    ratingAmountFrame.setVisible(true); 


                }
                
            });

            //Reviews
           
            JTextArea reviewArea = new JTextArea();
            reviewArea = new JTextArea();
            reviewArea.setLineWrap(true);
            reviewArea.setWrapStyleWord(true);
            reviewArea.setEditable(false);
            reviewArea.setFocusable(false);
            reviewArea.setFont(new Font("Arial",Font.PLAIN,15));
            ArrayList<Review> currentReviews = new ArrayList<>();
            currentReviews = currentUser.getReviewsList();
            for (int i = 0; i < currentReviews.size(); i++){
                reviewArea.setText("\n" + currentReviews.get(i).getSenderUsername() + ": " + currentReviews.get(i).getReviewContent() + "\n" + reviewArea.getText());
            }
            reviewArea.setCaretPosition(0);

            JScrollPane scrollPaneForReviewArea = new JScrollPane(reviewArea);
            scrollPaneForReviewArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneForReviewArea.setBorder(new LineBorder(Color.BLACK,1));
            scrollPaneForReviewArea.setBounds(30,370,500,300);
            scrollPaneForReviewArea.setBackground(Color.white);
            
            JLabel reviewLabel = new JLabel("  " + currentUser.getReviewsCount() +" Reviews");
            reviewLabel.setBorder(new LineBorder(Color.BLACK,1));
            reviewLabel.setBounds(30,310,130,40);
            reviewLabel.setFont(new Font("Arial", Font.BOLD, 20));
            
            //Adverts Found

            
            JButton advertsFoundButton = new JButton( "Own Available Adverts");
            advertsFoundButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                  
                        
                   HomeScreen.hm.changePanel(new AdvertViewPanel(Integer.MIN_VALUE, Integer.MAX_VALUE, "", "", currentUser.getUsername(), currentUser)); // from my sample, may change
                    

                    
                }
                
            });

            advertsFoundButton.setFocusable(false);
            advertsFoundButton.setBorder(new LineBorder(Color.BLACK,1));
            advertsFoundButton.setBackground(Color.white);
            advertsFoundButton.setFont(new Font("Arial",Font.BOLD,17));
            advertsFoundButton.setBounds(310,310,220,40);

            //Select to Edit Advert

            JButton selectToEditAdvertButton = new JButton("Select to Edit Adverts");
            selectToEditAdvertButton.setBorder(new LineBorder(Color.BLACK,1));
            selectToEditAdvertButton.setBounds(650,250,250,40);
            selectToEditAdvertButton.setFont(new Font("Arial", Font.BOLD, 20));
            selectToEditAdvertButton.setFocusable(false);  
            selectToEditAdvertButton.setBackground(Color.white);
            selectToEditAdvertButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    JFrame selectToEditAdvertsFrame = new JFrame("Select To Edit Adverts");
                    selectToEditAdvertsFrame.setLayout(new BorderLayout());
                    selectToEditAdvertsFrame.setPreferredSize(new Dimension(800,600));
                    selectToEditAdvertsFrame.setBackground(Color.white);

                    ArrayList<Advert> currentSelectToEditAdverts = currentUser.getAdvertsList();

                    
                    JPanel advertsPanel = new JPanel();
                    advertsPanel.setLayout(new GridLayout(currentSelectToEditAdverts.size(),1));

                    for (int i = 0; i < currentSelectToEditAdverts.size() ; i++){

                        Advert currentAdvert = currentSelectToEditAdverts.get(i);
                       

                        JPanel sampleAdvertPanel = new JPanel(){
                            protected void paintComponent(Graphics g) {
                                g.drawImage(currentAdvert.getImage(), 20, 20, 80,80,null );
                            };
                        };
                   
                        sampleAdvertPanel.setLayout(null);
                        sampleAdvertPanel.setBackground(Color.white);
                        JLabel titleLabel = new JLabel(" " + currentAdvert.getTitle());
                        titleLabel.setBorder(new LineBorder(Color.black,1));
                        titleLabel.setBounds(120,20,400,80);
                        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));

                        JLabel priceLabel = new JLabel(currentAdvert.getPrice()+" ₺");
                        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
                        priceLabel.setBounds(540,20,100,80);

            
                        JButton homeButtonForAdvert = new JButton();
                        JButton awayButtonForAdvert = new JButton();
                        
                        ImageIcon homeIcon = new ImageIcon("icons\\homeIcon.png");
                        Image homeIconImage = homeIcon.getImage();
                        homeIconImage = homeIconImage.getScaledInstance(50, 40, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon newHomeIcon = new ImageIcon(homeIconImage);
                        homeButtonForAdvert.setIcon(newHomeIcon);
                        homeButtonForAdvert.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                currentUser.updateAvailability(true);
                                //Changing availability in database 
                                //--------------------------------
                                try {
                                    currentAdvert.updateAvailability(false);
                                    PreparedStatement changeAvailability = Database.databaseConnection.prepareStatement("UPDATE adverts SET availability = false WHERE advertTitle = ? AND sellerUsername = ?");
                                    changeAvailability.setString(1, currentAdvert.getTitle());
                                    changeAvailability.setString(2, currentUser.getUsername());
                                    changeAvailability.executeUpdate();
                                    homeButtonForAdvert.setVisible(false);
                                    awayButtonForAdvert.setVisible(true);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                
                                //--------------------------------

                                JOptionPane.showMessageDialog(null, "You have set this advert as unavailable", "Availability", JOptionPane.INFORMATION_MESSAGE,newAwayIcon);
                            }
                            
                        });
                        homeButtonForAdvert.setBounds(680,50,50,40);
                        

                        
                        ImageIcon awayIcon = new ImageIcon("icons\\awayIcon.png");
                        Image awayIconImage = awayIcon.getImage();
                        awayIconImage = awayIconImage.getScaledInstance(50, 40, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon newAwayIcon = new ImageIcon(awayIconImage);
                        awayButtonForAdvert.setIcon(newAwayIcon);
                        awayButtonForAdvert.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                currentUser.updateAvailability(false);
                                //Changing availability in database 
                                //--------------------------------
                                try {
                                    currentAdvert.updateAvailability(true);
                                    PreparedStatement changeAvailability = Database.databaseConnection.prepareStatement("UPDATE adverts SET availability = true WHERE advertTitle = ? AND sellerUsername = ?");
                                    changeAvailability.setString(1, currentAdvert.getTitle());
                                    changeAvailability.setString(2, currentUser.getUsername());
                                    changeAvailability.executeUpdate();
                                    homeButtonForAdvert.setVisible(true);
                                    awayButtonForAdvert.setVisible(false);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                
                                //--------------------------------
                                JOptionPane.showMessageDialog(null, "You have set this advert as available", "Availability", JOptionPane.INFORMATION_MESSAGE,newHomeIcon);
                            }
                            
                        });
                        awayButtonForAdvert.setBounds(680,50,50,40);

                        if (currentAdvert.checkAvailability()){
                            homeButtonForAdvert.setVisible(true);
                            awayButtonForAdvert.setVisible(false);
                        }
                        else{
                            homeButtonForAdvert.setVisible(false);
                            awayButtonForAdvert.setVisible(true);
                        }
                      

                        JLabel statusLabel = new JLabel("STATUS");
                        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        statusLabel.setBounds(680,10,50,40);
                        statusLabel.setBackground(Color.white);
                        
                        sampleAdvertPanel.add(homeButtonForAdvert); 
                        sampleAdvertPanel.add(awayButtonForAdvert);
                        sampleAdvertPanel.add(statusLabel);
                        sampleAdvertPanel.add(titleLabel);
                        sampleAdvertPanel.add(priceLabel);
                        sampleAdvertPanel.setBorder(new LineBorder(Color.black, 1));
                        sampleAdvertPanel.setPreferredSize(new Dimension(750,120));
                        advertsPanel.add(sampleAdvertPanel);
                        
                    }

                    advertsPanel.setBackground(Color.white);
                    JScrollPane selectToEditAdvertScrollPane = new JScrollPane(advertsPanel);
                    selectToEditAdvertScrollPane.setBackground(Color.white);
                    selectToEditAdvertScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                   
                    JPanel returnButtonPanel = new JPanel();
                    returnButtonPanel.setPreferredSize(new Dimension(800,100));
                    returnButtonPanel.setLayout(null);

                    JButton returnButton = new JButton("RETURN");
                    returnButton.setBorder(new LineBorder(Color.BLACK,1));
                    returnButton.setFont(new Font("Arial", Font.BOLD, 20));
                    returnButton.setFocusable(false);  
                    returnButton.setBackground(new Color(151,12,16));
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                           
                            selectToEditAdvertsFrame.dispose();
                            
                            
                        }
                        
                    });
                    returnButton.setBounds(300,25,200,50);
                    returnButtonPanel.add(returnButton);
                    
                   
                    selectToEditAdvertsFrame.add(selectToEditAdvertScrollPane, BorderLayout.CENTER);
                    selectToEditAdvertsFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    selectToEditAdvertsFrame.setResizable(false);
                    selectToEditAdvertsFrame.pack();
                    selectToEditAdvertsFrame.setVisible(true); 
                    


                }
                
            });


            //Select to Delete Advert

            JButton selectToDeleteAdvertButton = new JButton("Select to Delete Adverts");
            selectToDeleteAdvertButton.setBorder(new LineBorder(Color.BLACK,1));
            selectToDeleteAdvertButton.setBounds(650,310,250,40);
            selectToDeleteAdvertButton.setFont(new Font("Arial", Font.BOLD, 20));
            selectToDeleteAdvertButton.setFocusable(false);  
            selectToDeleteAdvertButton.setBackground(Color.white);
            selectToDeleteAdvertButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    JFrame selectToDeleteAdvertsFrame = new JFrame("Select To Delete Adverts");
                    selectToDeleteAdvertsFrame.setLayout(new BorderLayout());
                    selectToDeleteAdvertsFrame.setPreferredSize(new Dimension(800,600));

                    ArrayList<Advert> currentSelectToDeleteAdverts = currentUser.getAdvertsList();
                    
                    JPanel advertsPanel = new JPanel();
                    advertsPanel.setLayout(new GridLayout(currentSelectToDeleteAdverts.size(),1));
                    for (int i = 0; i < currentSelectToDeleteAdverts.size() ; i++){

                        String currentTitle = currentSelectToDeleteAdverts.get(i).getTitle();
                        BufferedImage currentAdvertImage = currentSelectToDeleteAdverts.get(i).getImage();
                        int currentPrice = currentSelectToDeleteAdverts.get(i).getPrice();

                        JPanel sampleAdvertPanel = new JPanel(){
                            protected void paintComponent(Graphics g) {
                                g.drawImage(currentAdvertImage, 20, 20, 80,80,null );
                            };
                        };
                   
                        sampleAdvertPanel.setLayout(null);
                        sampleAdvertPanel.setBackground(Color.white);
                        JLabel titleLabel = new JLabel(" " + currentTitle);
                        titleLabel.setBorder(new LineBorder(Color.black,1));
                        titleLabel.setBounds(120,20,400,80);
                        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));

                        JLabel priceLabel = new JLabel(currentPrice+" ₺");
                        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
                        priceLabel.setBounds(540,20,100,80);

                        JButton deleteAdvertButton = new JButton("Delete Advert");
                        deleteAdvertButton.setBorder(new LineBorder(Color.black,1));
                        deleteAdvertButton.setBounds(640,40,100,40);
                        deleteAdvertButton.setFocusable(false);
                        deleteAdvertButton.setBackground(new Color(151,12,16));
                        deleteAdvertButton.setForeground(Color.white);
                        deleteAdvertButton.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    PreparedStatement deleteAdvertStatement  = Database.databaseConnection.prepareStatement("DELETE FROM adverts WHERE advertTitle = ? AND sellerUsername = ?");
                                    deleteAdvertStatement.setString(1, currentTitle);
                                    deleteAdvertStatement.setString(2, currentUser.getUsername());
                                    deleteAdvertStatement.executeUpdate();
                                    refreshButton.doClick();
                                    JOptionPane.showMessageDialog(null, "Advert has been deleted from the application!", "Advert Deleted", JOptionPane.INFORMATION_MESSAGE);
                                    selectToDeleteAdvertsFrame.dispose();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                
                            }
                            
                        });
                        

                        sampleAdvertPanel.add(deleteAdvertButton);
                        sampleAdvertPanel.add(titleLabel);
                        sampleAdvertPanel.add(priceLabel);
                        sampleAdvertPanel.setBorder(new LineBorder(Color.black, 1));
                        sampleAdvertPanel.setPreferredSize(new Dimension(750,120));
                        advertsPanel.add(sampleAdvertPanel);
                        
                    }
                    advertsPanel.setBackground(Color.white);
                    JScrollPane selectToDeleteAdvertScrollPane = new JScrollPane(advertsPanel);
                    selectToDeleteAdvertScrollPane.setBackground(Color.WHITE);
                    selectToDeleteAdvertScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                   
                    JPanel returnButtonPanel = new JPanel();
                    returnButtonPanel.setPreferredSize(new Dimension(800,100));
                    returnButtonPanel.setLayout(null);

                    JButton returnButton = new JButton("RETURN");
                    returnButton.setBorder(new LineBorder(Color.BLACK,1));
                    returnButton.setFont(new Font("Arial", Font.BOLD, 20));
                    returnButton.setFocusable(false);  
                    returnButton.setBackground(new Color(151,12,16));
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectToDeleteAdvertsFrame.dispose();
                        }
                        
                    });
                    returnButton.setBounds(300,25,200,50);
                    returnButtonPanel.add(returnButton);
                    
                   
                    selectToDeleteAdvertsFrame.add(selectToDeleteAdvertScrollPane, BorderLayout.CENTER);
                    selectToDeleteAdvertsFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    selectToDeleteAdvertsFrame.setResizable(false);
                    selectToDeleteAdvertsFrame.pack();
                    selectToDeleteAdvertsFrame.setVisible(true); 
                    


                }
                
            });

            JButton editProfilePictureButton = new JButton("Edit Profile Picture");
            editProfilePictureButton.setBorder(new LineBorder(Color.BLACK,1));
            editProfilePictureButton.setBounds(650,430,250,40);
            editProfilePictureButton.setFont(new Font("Arial", Font.BOLD, 20));
            editProfilePictureButton.setFocusable(false);  
            editProfilePictureButton.setBackground(Color.white);
            editProfilePictureButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] options = {"Add a new profile picture", "Reset profile picture to default"};

                    // Show the option dialog
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "",
                            "Profile Picture Selection",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     // No custom icon
                            options,  // The two custom buttons
                            options[0] // Default selected option
                    );

                    if (choice == 0) {
                        JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Select an Image");

                    // Only allow image files
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Image Files", "jpg", "png", "jpeg", "bmp"
                    );
                    fileChooser.setFileFilter(filter);

                    int userSelection = fileChooser.showOpenDialog(null);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        
                        try {
                            Image uploadedImage = ImageIO.read(selectedFile);
                            if (uploadedImage == null) {
                                // ImageIO.read returns null if the file is not a known image type
                                JOptionPane.showMessageDialog(null, "The selected file is not a valid image.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            currentUser.setProfilePictrue(uploadedImage);
                            Database.setProfilePictureInDatabase(uploadedImage, currentUser.getUsername());
                            refreshButton.doClick();
                            JOptionPane.showMessageDialog(null, "Profile picture has been updated!", "Profile Picture Update",JOptionPane.INFORMATION_MESSAGE);

                        } 
                        catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "An error occurred while loading the image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }     
                    } 
                    else if (choice == 1) {
                        
                        try {
                            Image uploadedImage = ImageIO.read(new File("icons\\profile-picture.png"));
                            currentUser.setProfilePictrue(uploadedImage);
                            Database.setProfilePictureInDatabase(uploadedImage, currentUser.getUsername());
                            refreshButton.doClick();
                            JOptionPane.showMessageDialog(null, "Profile picture has been updated!", "Profile Picture Update",JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error Happened", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                    
                }
            });
            this.add(editProfilePictureButton); 

            //Viewed Adverts 

            JButton viewedAdvertsButton = new JButton("Viewed Adverts");
            viewedAdvertsButton.setBorder(new LineBorder(Color.BLACK,1));
            viewedAdvertsButton.setBounds(650,370,250,40);
            viewedAdvertsButton.setFont(new Font("Arial", Font.BOLD, 20));
            viewedAdvertsButton.setFocusable(false);  
            viewedAdvertsButton.setBackground(Color.white);
            viewedAdvertsButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    JFrame viewedAdvertsFrame = new JFrame("Viewed Adverts");
                    viewedAdvertsFrame.setLayout(new BorderLayout());
                    viewedAdvertsFrame.setPreferredSize(new Dimension(800,600));

                    JPanel advertsPanel = new JPanel();

                    ArrayList<Advert> currentViewedAdverts = currentUser.getViewedAdverts();

                    advertsPanel.setLayout(new GridLayout(currentViewedAdverts.size(),1));
                    for (int i = 0; i < currentViewedAdverts.size(); i++){

                        
                        String currentTitle = currentViewedAdverts.get(i).getTitle();
                        BufferedImage currentAdvertImage = currentViewedAdverts.get(i).getImage();
                        int currentPrice = currentViewedAdverts.get(i).getPrice();

                        JPanel sampleAdvertPanel = new JPanel(){
                            protected void paintComponent(Graphics g) {
                                g.drawImage(currentAdvertImage, 20, 20, 80,80,null );
                            };
                        };
                   
                        sampleAdvertPanel.setLayout(null);
                        sampleAdvertPanel.setBackground(Color.white);
                        JLabel titleLabel = new JLabel(" " + currentTitle);
                        titleLabel.setBorder(new LineBorder(Color.black,1));
                        titleLabel.setBounds(120,20,400,80);
                        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));

                        JLabel priceLabel = new JLabel(currentPrice+" ₺");
                        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
                        priceLabel.setBounds(540,20,100,80);

                        JButton deleteAdvertButton = new JButton("Delete Advert");
                        deleteAdvertButton.setBorder(new LineBorder(Color.black,1));
                        deleteAdvertButton.setBounds(640,40,100,40);
                        deleteAdvertButton.setFocusable(false);
                        deleteAdvertButton.setBackground(new Color(151,12,16));
                        deleteAdvertButton.setForeground(Color.white);
                        deleteAdvertButton.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    PreparedStatement deleteAdvertStatement  = Database.databaseConnection.prepareStatement("DELETE FROM viewedadverts WHERE advertId = (SELECT advertId FROM adverts WHERE advertTitle = ?) AND viewerUsername = ?");
                                    deleteAdvertStatement.setString(1, currentTitle);
                                    deleteAdvertStatement.setString(2, currentUser.getUsername());
                                    deleteAdvertStatement.executeUpdate();
                                    refreshButton.doClick();
                                    JOptionPane.showMessageDialog(null, "Advert has been deleted from viewed adverts!", "Advert Deleted", JOptionPane.INFORMATION_MESSAGE);
                                    viewedAdvertsFrame.dispose();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                
                            }
                            
                        });
                        

                        sampleAdvertPanel.add(deleteAdvertButton);
                        sampleAdvertPanel.add(titleLabel);
                        sampleAdvertPanel.add(priceLabel);
                        sampleAdvertPanel.setBorder(new LineBorder(Color.black, 1));
                        sampleAdvertPanel.setPreferredSize(new Dimension(750,120));
                        advertsPanel.add(sampleAdvertPanel);



                    }

                    JScrollPane viewedAdvertsScrollPane = new JScrollPane(advertsPanel);
                    viewedAdvertsScrollPane.setBackground(Color.white);
                    viewedAdvertsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                   
                    JPanel returnButtonPanel = new JPanel();
                    returnButtonPanel.setPreferredSize(new Dimension(800,100));
                    returnButtonPanel.setLayout(null);

                    JButton returnButton = new JButton("RETURN");
                    returnButton.setBorder(new LineBorder(Color.BLACK,1));
                    returnButton.setFont(new Font("Arial", Font.BOLD, 20));
                    returnButton.setFocusable(false);  
                    returnButton.setBackground(new Color(151,12,16));
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            viewedAdvertsFrame.dispose();
                        }
                        
                    });
                    returnButton.setBounds(300,25,200,50);
                    returnButtonPanel.add(returnButton);
                    
                   
                    viewedAdvertsFrame.add(viewedAdvertsScrollPane, BorderLayout.CENTER);
                    viewedAdvertsFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    viewedAdvertsFrame.setResizable(false);
                    viewedAdvertsFrame.pack();
                    viewedAdvertsFrame.setVisible(true);



                }
                
            });

            //Logout Button

            JButton logoutButton = new JButton("LOGOUT");
            logoutButton.setBorder(new LineBorder(Color.BLACK,1));
            logoutButton.setBounds(800,650,150,40);
            logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
            logoutButton.setFocusable(false);  
            logoutButton.setBackground(new Color(151,12,16));
            logoutButton.setForeground(Color.white);
            logoutButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "LOGGING OUT...", "Title", JOptionPane.INFORMATION_MESSAGE);
                }
                
            });



            this.add(selectToEditAdvertButton);
            this.add(logoutButton);
            this.add(viewedAdvertsButton);
            this.add(selectToDeleteAdvertButton);
            this.add(reviewLabel);
            this.add(advertsFoundButton);
            this.add(scrollPaneForReviewArea);
            this.add(selectStatusLabel);
            this.add(emailLabel);
            this.add(usernameLabel);
            this.add(homeButton);
            this.add(awayButton);
            this.add(ratingLabel);
            this.add(ratingCountButton);
        }
        //Other Profile
        else{
            
            //Email Label

            //GETTING EMAIL FROM DATABASE
            //---------------------------------
            String email = "";
            try {   
                PreparedStatement emailStatement = Database.databaseConnection.prepareStatement("SELECT user_email FROM users WHERE username = ?");
                emailStatement.setString(1, username);
                ResultSet rs = emailStatement.executeQuery();
                while ( rs.next() ){
                    email = rs.getString("user_email");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //---------------------------------
            
            JLabel emailLabel = new JLabel(email); 
            emailLabel.setBounds(280,125,300,20);
            emailLabel.setFont(new Font("Arias", Font.BOLD, 15));

            //Username Label
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setBounds(136,205,300,30);
            usernameLabel.setFont(new Font("Arias", Font.BOLD, 18));

            //Status Pic

            ImageIcon homeIcon = new ImageIcon("icons\\homeIcon.png");
            Image homeIconImage = homeIcon.getImage();
            homeIconImage = homeIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newHomeIcon = new ImageIcon(homeIconImage);

            ImageIcon awayIcon = new ImageIcon("icons\\awayIcon.png");
            Image awayIconImage = awayIcon.getImage();
            awayIconImage = awayIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newAwayIcon = new ImageIcon(awayIconImage);

            JLabel statusPicture = new JLabel();
            boolean availability = true;
            
            //GETTING AVAILABILITY FROM THE DATABASE
            //-----------------------------------------------------
            try {
                PreparedStatement availabilityStatement = Database.databaseConnection.prepareStatement("SELECT available FROM users WHERE username = ?");
                availabilityStatement.setString(1, username);
                ResultSet rs = availabilityStatement.executeQuery();
                while (rs.next()){
                    availability = rs.getBoolean("available");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //-----------------------------------------------------

            if ( availability ){ 
                statusPicture = new JLabel(newHomeIcon);
                statusPicture.setBackground(Color.white);
            }
            else{
                statusPicture = new JLabel(newAwayIcon);
                statusPicture.setBackground(Color.white);
            }
            statusPicture.setOpaque(true);
            
            
            
            statusPicture.setBounds(320,80, 60,40);
            statusPicture.setFocusable(false);

            //Rating

            JLabel ratingLabel = new JLabel();
            String formattedRating = String.format("%.2f",getRatingsForOtherProfile());
            ratingLabel.setText(formattedRating);
            ratingLabel.setFont(new Font("Arial Black",Font.BOLD, 35));
            ratingLabel.setBounds(300,212,100,100);

            JButton ratingCountButton = new JButton( getTotalNumberOfRatingsForOtherProfile() + " Ratings"); 
            ratingCountButton.setBorder(new LineBorder(Color.BLACK,1));
            ratingCountButton.setBounds(170,310,130,40);
            ratingCountButton.setFont(new Font("Arial", Font.BOLD, 20));
            ratingCountButton.setFocusable(false);  
            ratingCountButton.setBackground(Color.white);
            ratingCountButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    JFrame ratingAmountFrame = new JFrame("Ratings Amount");
                    ratingAmountFrame.setLayout(new BorderLayout());
                    ratingAmountFrame.setPreferredSize(new Dimension(800,600));

                    JPanel ratingsPanel = new JPanel();
                    ratingsPanel.setLayout(new GridLayout(getTotalNumberOfRatingsForOtherProfile(),1));
                    ratingsPanel.setBackground(Color.white);
                    ArrayList<Rating> ratingsList = getRatingsListForOtherProfile();

                    boolean hasRated = false;
                    currentUsersRating = null;

                    for (int i = 0; i < ratingsList.size() && !hasRated ; i++){
                        if ( ratingsList.get(i).getSenderUsername().equals(currentUser.getUsername())){
                            hasRated = true;
                            currentUsersRating = ratingsList.get(i);
                        }
                    }

                    if ( hasRated ){
                        JPanel sampleRatingPanel = new JPanel(){

                            protected void paintComponent(Graphics g2){
                                super.paintComponent(g2);
                                for (int i = 0; i < 5; i++){
                                    g2.drawImage(emptyStar, 200 + 80*i, 60, 80, 80, this);
                                }
                                for (int i = 0; i < currentUsersRating.getRating(); i++){
                                    g2.drawImage(fullStar, 200 + 80*i, 60, 80, 80, this);
                                }
    
                            }

                        };

                        sampleRatingPanel.setLayout(null);
                        JLabel senderUsernameLabel = new JLabel(currentUsersRating.getSenderUsername());
                        senderUsernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                        senderUsernameLabel.setBounds(20,60,200,100);
                        sampleRatingPanel.setPreferredSize(new Dimension(750,200));
                        sampleRatingPanel.add(senderUsernameLabel);
                        sampleRatingPanel.setBackground(Color.white);
    
                        JButton deleteRatingButton = new JButton("Delete Rating");
                        deleteRatingButton.setBorder(new LineBorder(Color.BLACK,1));
                        deleteRatingButton.setFont(new Font("Arial", Font.BOLD, 20));
                        deleteRatingButton.setFocusable(false);  
                        deleteRatingButton.setBackground(new Color(151,12,16));
                        deleteRatingButton.setForeground(Color.white);
                        deleteRatingButton.setBounds(605,80,150,40);
                        deleteRatingButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                Database.deleteFromDatabase(currentUsersRating);
                                
                                refreshButton.doClick();
                                JOptionPane.showMessageDialog(null, "You have deleted your rating of " + currentUsersRating.getRating() + " from " + currentUsersRating.getRecieverUsername(),"Deleted Rating",JOptionPane.INFORMATION_MESSAGE);
                                ratingAmountFrame.dispose();
                                    
                            }
                                
                        });
                        sampleRatingPanel.add(deleteRatingButton);
                        ratingsPanel.add(sampleRatingPanel);
                    }

                    for (int i = 0; i < ratingsList.size(); i++){
                        
                        Rating currentRating = ratingsList.get(i);
                        if ( !(currentRating.getSenderUsername().equals(currentUser.getUsername()))){
                            JPanel sampleRatingPanel = new JPanel(){

                                protected void paintComponent(Graphics g2){
                                    super.paintComponent(g2);
                                    for (int i = 0; i < 5; i++){
                                        g2.drawImage(emptyStar, 200 + 80*i, 60, 80, 80, this);
                                    }
                                    for (int i = 0; i < currentRating.getRating(); i++){
                                        g2.drawImage(fullStar, 200 + 80*i, 60, 80, 80, this);
                                    }
        
                                }
    
    
                            };
    
                            sampleRatingPanel.setLayout(null);
                            JLabel senderUsernameLabel = new JLabel(currentRating.getSenderUsername());
                            senderUsernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                            senderUsernameLabel.setBounds(20,60,200,100);
                            sampleRatingPanel.setPreferredSize(new Dimension(750,200));
                            sampleRatingPanel.add(senderUsernameLabel);
                            sampleRatingPanel.setBackground(Color.white);
                            ratingsPanel.add(sampleRatingPanel);
                        }
                        
                      
                    }
                    JScrollPane ratingAmountScrollPane = new JScrollPane(ratingsPanel);
                    ratingAmountScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                   
                    JPanel returnButtonPanel = new JPanel();
                    returnButtonPanel.setPreferredSize(new Dimension(800,100));
                    returnButtonPanel.setLayout(null);

                    JButton returnButton = new JButton("RETURN");
                    returnButton.setBorder(new LineBorder(Color.BLACK,1));
                    returnButton.setFont(new Font("Arial", Font.BOLD, 20));
                    returnButton.setFocusable(false);  
                    returnButton.setBackground(new Color(151,12,16));
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ratingAmountFrame.dispose();
                        }
                        
                    });
                    returnButton.setBounds(300,25,200,50);
                    returnButtonPanel.add(returnButton);
                    
                    ratingAmountFrame.setBackground(Color.white);
                    ratingAmountFrame.add(ratingAmountScrollPane, BorderLayout.CENTER);
                    ratingAmountFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    ratingAmountFrame.setResizable(false);
                    ratingAmountFrame.pack();
                    ratingAmountFrame.setVisible(true);

                }
                
            });

            JButton addRatingButton = new JButton("Add Rating");
            addRatingButton.setBorder(new LineBorder(Color.BLACK,1));
            addRatingButton.setBounds(310,310,120,40);
            addRatingButton.setFont(new Font("Arial", Font.BOLD, 20));
            addRatingButton.setFocusable(false);  
            addRatingButton.setBackground(Color.white);
            addRatingButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        PreparedStatement currentUserIdStatement = Database.databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
                        currentUserIdStatement.setString(1, currentUser.getUsername());
                        ResultSet currentUserIdRs = currentUserIdStatement.executeQuery();
                        int currentUserId = 0;
                        if (currentUserIdRs.next()){
                            currentUserId = currentUserIdRs.getInt("user_id");
                        }

                        PreparedStatement checkAddRatingStatement = Database.databaseConnection.prepareStatement("SELECT sender_Id FROM ratings WHERE recieverId = (SELECT user_id from users WHERE username = ?)");
                        checkAddRatingStatement.setString(1, username);
                        ResultSet checkAddRatingRs = checkAddRatingStatement.executeQuery();
                        ArrayList<Integer> allRatingSendersId = new ArrayList<>();
                        while (checkAddRatingRs.next()){
                            
                            allRatingSendersId.add(checkAddRatingRs.getInt("sender_Id"));

                        }
                        if ( allRatingSendersId.indexOf(currentUserId) < 0){

                            ratingForAddRatingFrame = 0;
                            JFrame addRatingFrame = new JFrame("Add Rating");
                            JPanel addRatingPanel = new JPanel(){
                                
                                protected void paintComponent(Graphics g2){
                                    super.paintComponent(g2);
                                    for (int i = 0; i < 5; i++){
                                        g2.drawImage(emptyStar, 10 + 90*i, 60, 90, 90, this);
                                    }
                                    for (int i = 0; i < ratingForAddRatingFrame; i++){
                                        g2.drawImage(fullStar, 10 + 90*i, 60, 90, 90, this);
                                    }

                                }

                            };
                            
                            addRatingPanel.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseClicked(MouseEvent e) {
                    
                                    if ( e.getY() <= 150 && e.getY() >= 60 ){
                                        if ( e.getX() >= 0 && e.getX() <= 100 ){
                                            ratingForAddRatingFrame = 1;

                                        }
                                        else if ( e.getX() <= 190 ){
                                            ratingForAddRatingFrame = 2;
                                        }
                                        else if ( e.getX() <= 280 ){
                                            ratingForAddRatingFrame = 3;
                                        }
                                        else if ( e.getX() <= 370 ){
                                            ratingForAddRatingFrame = 4;
                                        }
                                        else if ( e.getX() <= 460 ){
                                            ratingForAddRatingFrame = 5;
                                        }
                                        addRatingPanel.repaint();
                                    }
                                    
                                }
                                @Override
                                public void mousePressed(MouseEvent e) {}
                                @Override
                                public void mouseReleased(MouseEvent e) {}
                                @Override
                                public void mouseEntered(MouseEvent e) {}
                                @Override
                                public void mouseExited(MouseEvent e) {}
                                
                            });

                            JButton resetButton = new JButton("Reset");
                            resetButton.setFocusable(false);
                            resetButton.setBorder(new LineBorder(Color.black,1));
                            resetButton.setBackground(new Color(151,12,16));
                            resetButton.setForeground(Color.white);
                            resetButton.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ratingForAddRatingFrame = 0;
                                    addRatingPanel.repaint();
                                }
                                
                            });
                            resetButton.setBounds(60,180,150,50);
                            resetButton.setFont(new Font("Arial", Font.BOLD, 20));

                            JButton sendRating = new JButton("Send Rating");
                            sendRating.setFocusable(false);
                            sendRating.setBorder(new LineBorder(Color.black,1));
                            sendRating.setBackground(new Color(151,12,16));
                            sendRating.setForeground(Color.white);
                            sendRating.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    Rating addedRating = new Rating(currentUser.getUsername(), username, ratingForAddRatingFrame);
                                    Database.addToDatabase(addedRating);
                                    ImageIcon fullstarIcon = new ImageIcon(fullStar);
                                    
                                    refreshButton.doClick();
                                    JOptionPane.showMessageDialog(null, "You have rated " + username + " " + ratingForAddRatingFrame + " stars!", "USER RATING", JOptionPane.INFORMATION_MESSAGE, fullstarIcon);
                                    addRatingFrame.dispose();

                                }
                                
                            });
                            sendRating.setBounds(250,180,150,50);
                            sendRating.setFont(new Font("Arial", Font.BOLD, 20));

                            JLabel label = new JLabel("SELECT RATING FOR " + username.toUpperCase(Locale.ENGLISH ));
                            label.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20));
                            label.setBounds(30,10,480,40);

                            addRatingPanel.add(label);
                            addRatingPanel.add(sendRating);
                            addRatingPanel.add(resetButton);
                            addRatingPanel.setLayout(null);
                            addRatingPanel.setBackground(Color.white);
                            addRatingPanel.setPreferredSize(new Dimension(480,250));
                            addRatingFrame.add(addRatingPanel);
                            addRatingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addRatingFrame.setResizable(false);
                            addRatingFrame.pack();
                            addRatingFrame.setVisible(true);

                        }
                        else{
                            JOptionPane.showMessageDialog(null, "You cannot rate one user more than once!", "ERROR",JOptionPane.ERROR_MESSAGE);
                        }


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    
                }
                
            });

            //Reviews
           
            JTextArea reviewArea = new JTextArea();
            reviewArea.setLineWrap(true);
            reviewArea.setWrapStyleWord(true);
            reviewArea.setEditable(false);
            reviewArea.setFocusable(false);
            reviewArea.setFont(new Font("Arial",Font.PLAIN,15));
            ArrayList<Review> currentReviews = getReviewsListForOtherProfile();
            for (int i = 0; i < currentReviews.size(); i++){
                reviewArea.setText("\n" + " " + currentReviews.get(i).getSenderUsername() +": " + currentReviews.get(i).getReviewContent() + "\n" + reviewArea.getText());
            }
            reviewArea.setCaretPosition(0);

            JScrollPane scrollPaneForReviewArea = new JScrollPane(reviewArea);
            scrollPaneForReviewArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneForReviewArea.setBorder(new LineBorder(Color.BLACK,1));
            scrollPaneForReviewArea.setBounds(30,360,500,300);
            scrollPaneForReviewArea.setBackground(Color.white);
            


            JTextField reviewTextField = new JTextField(" Add Review");
            reviewTextField.setFont(new Font("Arial",Font.ITALIC,15));
            reviewTextField.setBorder(new LineBorder(Color.BLACK,1));
            reviewTextField.setBounds(30,660,440,30);
            reviewTextField.setPreferredSize(new Dimension(440,30));
            reviewTextField.setBackground(Color.white);
            reviewTextField.addMouseListener(new MouseListener() { //To Empty The Text Field
                @Override
                public void mouseClicked(MouseEvent e) {
                    if ( reviewTextField.getText().equals(" Add Review") ){
                        reviewTextField.setText("");
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
                
            });

            JButton reviewSendButton = new JButton("Send");
            reviewSendButton.setFont(new Font("Arial",Font.ITALIC + Font.BOLD,15));
            reviewSendButton.setBorder(new LineBorder(Color.BLACK,1));
            reviewSendButton.setBounds(470,660,60,30);
            reviewSendButton.setPreferredSize(new Dimension(60,30));
            reviewSendButton.setBackground(Color.white);
            reviewSendButton.setFocusable(false);
            reviewSendButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String review = reviewTextField.getText();
                    Review newReview = new Review(currentUser.getUsername(), username, review);
                    Database.addToDatabase(newReview);
                    reviewTextField.setText(" Add Review");
                    refreshButton.doClick();
                    
                }
                
            });

            JLabel reviewLabel = new JLabel("  " + currentReviews.size()  +" Reviews");
            reviewLabel.setBorder(new LineBorder(Color.BLACK,1));
            reviewLabel.setBounds(30,310,130,40);
            reviewLabel.setFont(new Font("Arial", Font.BOLD, 20));

            //Adverts Found

    

            JButton advertsFoundButton = new JButton(getAdvertsCountForOtherProfile() + " Adverts Found");
            advertsFoundButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if ( getAdvertsCountForOtherProfile() == 0){
                        JOptionPane.showMessageDialog(null, "No Adverts Found", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        
                        HomeScreen.hm.changePanel(new AdvertViewPanel(Integer.MIN_VALUE, Integer.MAX_VALUE, "", "", username, currentUser));
                        
                    }
                }
                
            });
            advertsFoundButton.setFocusable(false);
            advertsFoundButton.setBorder(new LineBorder(Color.BLACK,1));
            advertsFoundButton.setBackground(Color.white);
            advertsFoundButton.setFont(new Font("Arial",Font.BOLD,20));
            advertsFoundButton.setBounds(700,190,200,40);

            //Add to Contact Button

            JButton addToContactButton = new JButton("Add to Contacts");
            addToContactButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isAlreadyContacts = false;
                    try {
                        PreparedStatement alreadyContactsCheckStatement = Database.databaseConnection.prepareStatement("SELECT * FROM contacts");
                        ResultSet checkContactsRs = alreadyContactsCheckStatement.executeQuery();
                        while (checkContactsRs.next()){

                            String username1 = checkContactsRs.getString("username1");
                            String username2 = checkContactsRs.getString("username2");
                            if ( (username1.equals(username) && username2.equals(currentUser.getUsername())) || (username1.equals(currentUser.getUsername()) && username2.equals(username))){
                                isAlreadyContacts = true;
                            }

                        }

                        if (isAlreadyContacts){
                            JOptionPane.showMessageDialog(null, username + " is already in your contacts list!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            PreparedStatement addToContactsStatement = Database.databaseConnection.prepareStatement("INSERT INTO contacts VALUES (?,?)");
                            addToContactsStatement.setString(1, currentUser.getUsername());
                            addToContactsStatement.setString(2, username);
                            addToContactsStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, username + " has been added to your contacts list!", "Added To Contacts", JOptionPane.INFORMATION_MESSAGE);
                            
                        }   


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    
                }
                
            });
            addToContactButton.setFocusable(false);
            addToContactButton.setBorder(new LineBorder(Color.BLACK,1));
            addToContactButton.setBackground(Color.white);
            addToContactButton.setFont(new Font("Arial",Font.BOLD,20));
            addToContactButton.setBounds(700,460,200,40);


            //Remove Contact Button

            JButton removeContactButton = new JButton("Remove Contact");
            removeContactButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                   
                    boolean isAlreadyContacts = false;
                    try {
                        PreparedStatement alreadyContactsCheckStatement = Database.databaseConnection.prepareStatement("SELECT * FROM contacts");
                        ResultSet checkContactsRs = alreadyContactsCheckStatement.executeQuery();
                        while (checkContactsRs.next()){

                            String username1 = checkContactsRs.getString("username1");
                            String username2 = checkContactsRs.getString("username2");
                            if ( (username1.equals(username) && username2.equals(currentUser.getUsername())) || (username1.equals(currentUser.getUsername()) && username2.equals(username))){
                                isAlreadyContacts = true;
                            }

                        }

                        if (isAlreadyContacts){
                            PreparedStatement removeFromContactsStatement = Database.databaseConnection.prepareStatement("DELETE FROM contacts WHERE (username1 = ? and username2 = ?) OR (username1 = ? and username2 = ?)");
                            removeFromContactsStatement.setString(1, currentUser.getUsername());
                            removeFromContactsStatement.setString(2, username);
                            removeFromContactsStatement.setString(3, username);
                            removeFromContactsStatement.setString(4, currentUser.getUsername());
                            removeFromContactsStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, username + " has been removed from contacts list!", "Successfully Removed",JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, username + " is not in your contacts!","ERROR",JOptionPane.ERROR_MESSAGE );
                        }   


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }



                }
                
            });
            removeContactButton.setFocusable(false);
            removeContactButton.setBorder(new LineBorder(Color.BLACK,1));
            removeContactButton.setBackground(Color.white);
            removeContactButton.setFont(new Font("Arial",Font.BOLD,20));
            removeContactButton.setBounds(700,520,200,40);
            


            this.add(reviewSendButton);
            this.add(reviewTextField);
            this.add(statusPicture);
            this.add(removeContactButton);
            this.add(addToContactButton);
            this.add(advertsFoundButton);
            this.add(scrollPaneForReviewArea);
            this.add(reviewLabel);
            this.add(usernameLabel);
            this.add(emailLabel);
            this.add(ratingLabel);
            this.add(ratingCountButton);
            this.add(addRatingButton);
        }


        
        
        this.add(refreshButton);
        this.setPreferredSize(new Dimension(1024,720));      
        this.setVisible(true); 
        
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(getCurrentProfilePicture(otherProfileUsername , currentUser), 120 * HomeScreen.hm.getWidth() / 1024, 50, 150 ,150, this);
        for (int i = 0; i < 5; i++){
            g.drawImage(emptyStar, 60 + 45*i, 240, 45, 45, this);
        }

        double rating = 0;
        int drawnStarAmount = 0;
        if ( currentUser.getUsername().equals(username) ){
            
           rating = currentUser.getRating();   
        }   
        else{
            rating = getRatingsForOtherProfile();
        }

        while ( rating >= 1 ){

            g.drawImage(fullStar, 60 + 45*drawnStarAmount, 240, 45, 45, this);
            rating--;
            drawnStarAmount++;
        }
        if ( rating >= 0.75 && rating < 1){
            g.drawImage(threeQuarterStar, 60 + 45*drawnStarAmount, 240, 45, 45, this);
        }
        else if (rating >= 0.5){
            g.drawImage(halfStar, 60 + 45*drawnStarAmount, 240, 45, 45, this);
        }
        else if ( rating >= 0.25 ){
            g.drawImage(oneQuarterStar, 60 + 45*drawnStarAmount, 240, 45, 45, this);
        }
    }   

    public static double getRatingsForOtherProfile(){
        double ratingForOthersProfile = 0;
        try {
            double rating = 0;
            int totalNumberOfRatings = 0;
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, otherProfileUsername);
            ResultSet rs = ratingStatement.executeQuery();
            while (rs.next()){
                rating += rs.getInt("rating");
                totalNumberOfRatings++;
            }
            ratingForOthersProfile = rating / totalNumberOfRatings;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingForOthersProfile;
    }

    public static int getTotalNumberOfRatingsForOtherProfile(){
        int totalNumberOfRatings = 0;
        try {
            
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, otherProfileUsername);
            ResultSet rs = ratingStatement.executeQuery();
            while (rs.next()){
                totalNumberOfRatings++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalNumberOfRatings;
    }
    public static ArrayList<Review> getReviewsListForOtherProfile(){
        ArrayList<Review> reviewsList = new ArrayList<>();
        try {
            
            PreparedStatement getReviewsStatement = Database.databaseConnection.prepareStatement("SELECT * FROM reviews WHERE recieverId = (SELECT user_Id FROM users WHERE username = ?)");
            getReviewsStatement.setString(1, otherProfileUsername);
            ResultSet reviewsRs = getReviewsStatement.executeQuery();
            while (reviewsRs.next()){

                int senderId = reviewsRs.getInt("sender_Id");
                int recieverId = reviewsRs.getInt("recieverId");
                String reviewContent = reviewsRs.getString("review");
                
                String senderUsername = "";
                PreparedStatement senderUsernameStatement = Database.databaseConnection.prepareStatement("SELECT username FROM users WHERE user_id = ?");
                senderUsernameStatement.setInt(1, senderId);
                ResultSet senderRs = senderUsernameStatement.executeQuery();
                while (senderRs.next()){
                    senderUsername = senderRs.getString("username");
                }
 
                String recieverUsername = "";
                PreparedStatement recieverUsernameStatement = Database.databaseConnection.prepareStatement("SELECT username FROM users WHERE user_id = ?");
                recieverUsernameStatement.setInt(1, recieverId);
                ResultSet recieverRs = recieverUsernameStatement.executeQuery();
                while (recieverRs.next()){
                    recieverUsername = recieverRs.getString("username");
                }

                reviewsList.add(new Review(senderUsername, recieverUsername, reviewContent));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewsList;


    }
    public static int getAdvertsCountForOtherProfile(){
        int advertsCount = 0;
        try {
            PreparedStatement getAdvertsListStatement = Database.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE sellerUsername = ?");
            getAdvertsListStatement.setString(1, otherProfileUsername);
            ResultSet advertsListRs = getAdvertsListStatement.executeQuery();
            while ( advertsListRs.next() ){
            

                if ( advertsListRs.getBoolean("availability")){
                    advertsCount++;
                }
                

            }
            PreparedStatement getUserAvailability = Database.databaseConnection.prepareStatement("SELECT available FROM users WHERE username = ?");
            getUserAvailability.setString(1, otherProfileUsername);
            ResultSet getUserAvailabilityRs = getUserAvailability.executeQuery();
            boolean userAvailability = false;
            if ( getUserAvailabilityRs.next() ){
                userAvailability = getUserAvailabilityRs.getBoolean("available");
            }
            if ( !userAvailability){
                advertsCount = 0;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return advertsCount;
    }

    public static ArrayList<Rating> getRatingsListForOtherProfile (){
        ArrayList<Rating> ratingsList = new ArrayList<>();

        try {
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT * FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, otherProfileUsername);
            ResultSet rs = ratingStatement.executeQuery();
            while (rs.next()){
               
               
                int currentRating = rs.getInt("rating");   
                int sender_Id = rs.getInt("sender_Id");
                String senderUsername = "";

                PreparedStatement ratingSenderStatement = Database.databaseConnection.prepareStatement("SELECT username FROM users WHERE user_Id = ?");
                ratingSenderStatement.setInt(1, sender_Id);
                ResultSet rs_SenderUsername = ratingSenderStatement.executeQuery();
                while (rs_SenderUsername.next() ){
                    senderUsername = rs_SenderUsername.getString("username");
                }

                ratingsList.add(new Rating(senderUsername, otherProfileUsername, currentRating));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingsList;
    }
    public static BufferedImage getCurrentProfilePicture(String username, User currentUser){
        if ( currentUser.getUsername().equals(username)){
            return currentUser.getProfilePicture();
        }
        else{
            BufferedImage profilePicture = null;
            try {
                PreparedStatement getProfilePictureStatement = Database.databaseConnection.prepareStatement("SELECT profilePicture FROM users WHERE username = ?");
                getProfilePictureStatement.setString(1, username);
                ResultSet getProfilePictureRs = getProfilePictureStatement.executeQuery();
                
                if ( getProfilePictureRs.next()){
                    
                    byte[] imageBytes = getProfilePictureRs.getBytes("profilePicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        try {
                            profilePicture = ImageIO.read(byteInputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } 
                    }
                }

               
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return profilePicture;
            
        }
    }
    

}

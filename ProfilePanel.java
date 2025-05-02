import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;



public class ProfilePanel extends JPanel{

    private Image profilePicture;
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
        refreshButton.setBounds(10,50,50,50);//CHANGE THIS PLACE LATER!
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MainFile.hm.changePanel(new ProfilePanel(username, currentUser));
            }
            
        });

        try{
            profilePicture = ImageIO.read(new File("icons\\profile-picture.png")); 
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
            emailLabel.setBounds(280,165,200+currentUser.getEmail().length()*5,20);
            emailLabel.setFont(new Font("Arias", Font.BOLD, 15));

            //Username Label
            JLabel usernameLabel = new JLabel(currentUser.getUsername());
            usernameLabel.setBounds(160 - currentUser.getUsername().length()*2,250,100+currentUser.getUsername().length()*5,30);
            usernameLabel.setFont(new Font("Arias", Font.BOLD, 18));

            //Select Status
            ButtonGroup buttonGroup = new ButtonGroup();

            JToggleButton homeButton = new JToggleButton();

            ImageIcon homeIcon = new ImageIcon("icons\\homeIcon.png");
            Image homeIconImage = homeIcon.getImage();
            homeIconImage = homeIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newHomeIcon = new ImageIcon(homeIconImage);
            homeButton.setIcon(newHomeIcon);
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
                    //--------------------------------

                    JOptionPane.showMessageDialog(null, "You have set yourself as available", "Availability", JOptionPane.INFORMATION_MESSAGE,newHomeIcon);
                }
                
            });
            homeButton.setBounds(700,180, 60,40);

            JToggleButton awayButton = new JToggleButton();
            ImageIcon awayIcon = new ImageIcon("icons\\awayIcon.png");
            Image awayIconImage = awayIcon.getImage();
            awayIconImage = awayIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newAwayIcon = new ImageIcon(awayIconImage);
            awayButton.setIcon(newAwayIcon);
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
                    //--------------------------------
                    JOptionPane.showMessageDialog(null, "You have set yourself as unavailable", "Availability", JOptionPane.INFORMATION_MESSAGE,newAwayIcon);
                }
                
            });
            awayButton.setBounds(760,180, 60,40);
            buttonGroup.add(homeButton);
            buttonGroup.add(awayButton);

            JLabel selectStatusLabel = new JLabel("Select Status");
            selectStatusLabel.setBounds(700,150,200,30);
            selectStatusLabel.setFont(new Font("Arial",Font.BOLD,20));

            //Rating


            JLabel ratingLabel = new JLabel();
            String formattedRating = String.format("%.2f",currentUser.getRating());
            ratingLabel.setText(formattedRating);
            ratingLabel.setFont(new Font("Arial Black",Font.BOLD, 35));
            ratingLabel.setBounds(300,272,100,100);

            JButton ratingCountButton = new JButton(currentUser.getRatingAmount() + " Ratings");
            ratingCountButton.setBorder(new LineBorder(Color.BLACK,1));
            ratingCountButton.setBounds(160,370,120,40);
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
                        JLabel senderUsernameLabel = new JLabel(currentRating.getSenderUsername());
                        senderUsernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                        senderUsernameLabel.setBounds(20,60,200,100);
                        sampleRatingPanel.setPreferredSize(new Dimension(750,200));
                        sampleRatingPanel.add(senderUsernameLabel);
                        sampleRatingPanel.setBackground(Color.white);
                        ratingsPanel.add(sampleRatingPanel);



                    }

                    JScrollPane ratingAmountScrollPane = new JScrollPane(ratingsPanel);
                    ratingAmountScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                   
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
            

            JScrollPane scrollPaneForReviewArea = new JScrollPane(reviewArea);
            scrollPaneForReviewArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPaneForReviewArea.setBorder(new LineBorder(Color.BLACK,1));
            scrollPaneForReviewArea.setBounds(30,430,500,300);
            scrollPaneForReviewArea.setBackground(Color.white);
            
            JLabel reviewLabel = new JLabel("  " + currentUser.getReviewsCount() +" Reviews");
            reviewLabel.setBorder(new LineBorder(Color.BLACK,1));
            reviewLabel.setBounds(30,370,120,40);
            reviewLabel.setFont(new Font("Arial", Font.BOLD, 20));
            
            //Adverts Found

            JButton advertsFoundButton = new JButton(currentUser.getAdvertsCount() + " Adverts Found");
            advertsFoundButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if ( currentUser.getAdvertsCount() == 0){
                        JOptionPane.showMessageDialog(null, "No Adverts Found", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
            });
            advertsFoundButton.setFocusable(false);
            advertsFoundButton.setBorder(new LineBorder(Color.BLACK,1));
            advertsFoundButton.setBackground(Color.white);
            advertsFoundButton.setFont(new Font("Arial",Font.BOLD,20));
            advertsFoundButton.setBounds(290,370,200,40);

            //Select to Delete Advert

            JButton selectToDeleteAdvertButton = new JButton("Select to Delete Adverts");
            selectToDeleteAdvertButton.setBorder(new LineBorder(Color.BLACK,1));
            selectToDeleteAdvertButton.setBounds(650,290,250,40);
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
                    for (int i = 0; i < currentSelectToDeleteAdverts.size(); i++){

                        //CREATING AN ADVERT PANEL WITH DELETE BUTTON 
                        
                    }

                    JScrollPane selectToDeleteAdvertScrollPane = new JScrollPane(advertsPanel);
                    selectToDeleteAdvertScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                   
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


            //Viewed Adverts 

            JButton viewedAdvertsButton = new JButton("Viewed Adverts");
            viewedAdvertsButton.setBorder(new LineBorder(Color.BLACK,1));
            viewedAdvertsButton.setBounds(650,350,250,40);
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

                        //CREATING AN ADVERT PANEL WITH DELETE BUTTON 

                    }

                    JScrollPane viewedAdvertsScrollPane = new JScrollPane(advertsPanel);
                    viewedAdvertsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                   
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
            logoutButton.setBounds(800,690,150,40);
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
            emailLabel.setBounds(280,165,200+email.length()*5,20);
            emailLabel.setFont(new Font("Arias", Font.BOLD, 15));

            //Username Label
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setBounds(160 - username.length()*2,250,100+username.length()*5,30);
            usernameLabel.setFont(new Font("Arias", Font.BOLD, 18));

            //Status Pic

            ImageIcon homeIcon = new ImageIcon("homeIcon.png");
            Image homeIconImage = homeIcon.getImage();
            homeIconImage = homeIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newHomeIcon = new ImageIcon(homeIconImage);

            ImageIcon awayIcon = new ImageIcon("awayIcon.png");
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
            }
            else{
                statusPicture = new JLabel(newAwayIcon);
            }
            statusPicture.setOpaque(true);
            
            
            
            statusPicture.setBounds(320,120, 60,40);
            statusPicture.setFocusable(false);

            //Rating

            JLabel ratingLabel = new JLabel();
            String formattedRating = String.format("%.2f",getRatingsForOtherProfile());
            ratingLabel.setText(formattedRating);
            ratingLabel.setFont(new Font("Arial Black",Font.BOLD, 35));
            ratingLabel.setBounds(300,272,100,100);

            JButton ratingCountButton = new JButton( getTotalNumberOfRatingsForOtherProfile() + " Ratings"); 
            ratingCountButton.setBorder(new LineBorder(Color.BLACK,1));
            ratingCountButton.setBounds(160,370,120,40);
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
                                JOptionPane.showMessageDialog(null, "You have deleted your rating of " + currentUsersRating.getRating() + " from " + currentUsersRating.getRecieverUsername(),"Deleted Rating",JOptionPane.INFORMATION_MESSAGE);
                                ratingAmountFrame.dispose();
                                refreshButton.doClick();
                                    
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
                    ratingAmountScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                   
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
            addRatingButton.setBounds(290,370,120,40);
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
                                    addRatingFrame.dispose();
                                    JOptionPane.showMessageDialog(null, "You have rated " + username + " " + ratingForAddRatingFrame + " stars!", "USER RATING", JOptionPane.INFORMATION_MESSAGE, fullstarIcon);
                                    refreshButton.doClick();

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
            scrollPaneForReviewArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPaneForReviewArea.setBorder(new LineBorder(Color.BLACK,1));
            scrollPaneForReviewArea.setBounds(30,420,500,300);
            scrollPaneForReviewArea.setBackground(Color.white);
            


            JTextField reviewTextField = new JTextField(" Add Review");
            reviewTextField.setFont(new Font("Arial",Font.ITALIC,15));
            reviewTextField.setBorder(new LineBorder(Color.BLACK,1));
            reviewTextField.setBounds(30,720,440,30);
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
            reviewSendButton.setBounds(470,720,60,30);
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
            reviewLabel.setBounds(30,370,120,40);
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
                        JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
            });
            advertsFoundButton.setFocusable(false);
            advertsFoundButton.setBorder(new LineBorder(Color.BLACK,1));
            advertsFoundButton.setBackground(Color.white);
            advertsFoundButton.setFont(new Font("Arial",Font.BOLD,20));
            advertsFoundButton.setBounds(700,230,200,40);

            //Add to Contact Button

            JButton addToContactButton = new JButton("Add to Contacts");
            addToContactButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
                }
                
            });
            addToContactButton.setFocusable(false);
            addToContactButton.setBorder(new LineBorder(Color.BLACK,1));
            addToContactButton.setBackground(Color.white);
            addToContactButton.setFont(new Font("Arial",Font.BOLD,20));
            addToContactButton.setBounds(700,500,200,40);


            //Remove Contact Button

            JButton removeContactButton = new JButton("Remove Contact");
            removeContactButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
                }
                
            });
            removeContactButton.setFocusable(false);
            removeContactButton.setBorder(new LineBorder(Color.BLACK,1));
            removeContactButton.setBackground(Color.white);
            removeContactButton.setFont(new Font("Arial",Font.BOLD,20));
            removeContactButton.setBounds(700,560,200,40);
            


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
        this.setPreferredSize(new Dimension(1024,768));      
        this.setVisible(true); 
        
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(profilePicture, 120, 90, 150,150, this);
        for (int i = 0; i < 5; i++){
            g.drawImage(emptyStar, 60 + 45*i, 300, 45, 45, this);
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

            g.drawImage(fullStar, 60 + 45*drawnStarAmount, 300, 45, 45, this);
            rating--;
            drawnStarAmount++;
        }
        if ( rating >= 0.75 && rating < 1){
            g.drawImage(threeQuarterStar, 60 + 45*drawnStarAmount, 300, 45, 45, this);
        }
        else if (rating >= 0.5){
            g.drawImage(halfStar, 60 + 45*drawnStarAmount, 300, 45, 45, this);
        }
        else if ( rating >= 0.25 ){
            g.drawImage(oneQuarterStar, 60 + 45*drawnStarAmount, 300, 45, 45, this);
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

                advertsCount++;

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
    

}

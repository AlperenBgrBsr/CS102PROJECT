import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class AdvertViewPanel extends JPanel{


    private Image emptyBookmarkImage;
    private Image filledBookmarkImage;
    private ImageIcon emptyBookmarkIcon;
    private ImageIcon filledBookmarkIcon;
    private ArrayList<Advert> allAdvertsList;
    private JTextField minTextField;
    private JTextField maxTextField;
    private JRadioButton lectureMaterialButton;
    private JRadioButton clothButton;
    private JRadioButton otherButton;
    private JTextField wordFilterTextField; 
    private JTextField usernameFilterTextField;
    private JRadioButton allTypesButton;
    private BufferedImage advertImageForUserDetails;
    private String userDetailsEmail;
    

    public AdvertViewPanel(int minPrice,int maxPrice,String type,String wordFilter,String usernameFilter, User currentUser){
        

        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
       
        try {

            emptyBookmarkImage = ImageIO.read(new File("icons\\emptyBookmark.png"));
            filledBookmarkImage = ImageIO.read(new File("icons\\filledBookmark.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        emptyBookmarkImage = emptyBookmarkImage.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        emptyBookmarkIcon = new ImageIcon(emptyBookmarkImage);

        filledBookmarkImage = filledBookmarkImage.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        filledBookmarkIcon = new ImageIcon(filledBookmarkImage);


        JButton refreshButton = new JButton();
        ImageIcon refreshIcon = new ImageIcon("refreshIcon.png");
        Image refreshIconImage = refreshIcon.getImage();
        refreshIconImage = refreshIconImage.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newRefreshIcon = new ImageIcon(refreshIconImage);
        refreshButton.setIcon(newRefreshIcon);
        refreshButton.setFocusable(false);
        refreshButton.setBounds(10,10,50,50);//CHANGE THIS PLACE LATER!
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                HomeScreen.hm.changePanel(new AdvertViewPanel(minPrice, maxPrice, type, wordFilter, usernameFilter, currentUser));
              
            }
            
        });


        allAdvertsList = getAllAdverts();
        allAdvertsList = filterAllAdverts(minPrice, maxPrice, type, wordFilter, usernameFilter);
        ArrayList<Advert> currentUserBookmarkedAdverts = currentUser.getBookmarkedAdverts();
        
        JPanel advertsPanel = new JPanel();

        if ( allAdvertsList.size() == 0){
            JLabel noAdvertsLabel = new JLabel("                 NO ADVERTS FOUND");
            noAdvertsLabel.setFont(new Font("Arial",Font.BOLD,40));
            noAdvertsLabel.setBackground(Color.white);
            advertsPanel.setLayout(new GridLayout(1,1));
            advertsPanel.add(noAdvertsLabel);
        }  
        else{
            advertsPanel.setLayout(new GridLayout(allAdvertsList.size(),1));
        }

        for ( int i = 0 ; i < allAdvertsList.size(); i++){

            Advert currentAdvert = allAdvertsList.get(i);
            
            JPanel sampleAdvertPanel = new JPanel(){
                protected void paintComponent(Graphics g) {
                    g.drawImage(currentAdvert.getImage(), 20 * HomeScreen.hm.getWidth() / 1024, 20, 80,80,null );
                };
            };
                
            sampleAdvertPanel.setLayout(null);
            sampleAdvertPanel.setBackground(Color.white);

            //Title

            JButton titleButton = new JButton(" " + currentAdvert.getTitle());
            titleButton.setBorder(new LineBorder(Color.black,1));
            titleButton.setBounds(120,20,400,80);
            titleButton.setFont(new Font("Arial", Font.PLAIN, 15));
            titleButton.setFocusable(false);
            titleButton.setBackground(Color.white);
            titleButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    JFrame advertDetailsFrame = new JFrame("Advert Details Frame");
                    advertDetailsFrame.setLayout(new BorderLayout());

                    //User Details Panel

                    advertImageForUserDetails = null;
                    
                    try {
                        PreparedStatement getProfilePictureStatement = Database.databaseConnection.prepareStatement("SELECT profilePicture FROM users WHERE username = ?");
                        getProfilePictureStatement.setString(1, currentAdvert.getSellerUsername());
                        ResultSet getProfilePictureRs = getProfilePictureStatement.executeQuery();

                        while ( getProfilePictureRs.next() ){
                            
                            advertImageForUserDetails = null;
                            byte[] imageBytes = getProfilePictureRs.getBytes("profilePicture");
                            if (imageBytes != null) {
                                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                                try {
                                    advertImageForUserDetails = ImageIO.read(byteInputStream);
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                } 
                            }
                            
                        }
                    } catch (SQLException e1) {
                        
                        e1.printStackTrace();
                    }
                    


                    JPanel userDetailsPanel = new JPanel(){
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g;
                            g2d.drawImage(advertImageForUserDetails, 45, 150, 100,100,null);
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawLine(190, 0,190, 500);
                        }
                    };

                    userDetailsPanel.setLayout(null);
                    userDetailsPanel.setBackground(Color.white);
                    userDetailsPanel.setPreferredSize(new Dimension(200,500));
                    userDetailsEmail = "";


                    JButton userDetailUsernameButton = new JButton(currentAdvert.getSellerUsername());
                    userDetailUsernameButton.setFont(new Font("Arial",Font.BOLD,18));
                    userDetailUsernameButton.setBounds(Math.max(18 - currentAdvert.getSellerUsername().length(), 0),270,160,30);
                    userDetailUsernameButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
                            HomeScreen.hm.items.addSearchBar();
                            HomeScreen.hm.changePanel(new ProfilePanel(currentAdvert.getSellerUsername(), MainFile.currentUserForAll));
						}
                    });

                    userDetailUsernameButton.setContentAreaFilled(false);
                    userDetailUsernameButton.setBorderPainted(false);
                    userDetailUsernameButton.setFocusPainted(false);
                    userDetailUsernameButton.setOpaque(true);
                    try {

                        PreparedStatement userDetailsStatement = Database.databaseConnection.prepareStatement("SELECT user_email FROM users WHERE username = ?");
                        userDetailsStatement.setString(1, currentAdvert.getSellerUsername());
                        ResultSet userDetailsRs = userDetailsStatement.executeQuery();
                        if ( userDetailsRs.next() ){
                            userDetailsEmail = userDetailsRs.getString("user_email");
                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    JLabel userDetailsEmailLabel = new JLabel(userDetailsEmail);
                    userDetailsEmailLabel.setFont(new Font("Arial",Font.BOLD, 13));
                    userDetailsEmailLabel.setBounds(15,310,180,50);

                    userDetailsPanel.add(userDetailUsernameButton);
                    userDetailsPanel.add(userDetailsEmailLabel);
                   
                    //Advert Details Panel

                    JPanel advertDetailsPanel = new JPanel(){
                        @Override
                        protected void paintComponent(Graphics g) {
                            g.drawImage(currentAdvert.getImage(), 150, 50,  100,100,null);
                        }
                    };
                    advertDetailsPanel.setLayout(null);
                    advertDetailsPanel.setPreferredSize(new Dimension(400,500));
                    advertDetailsPanel.setBackground(Color.white);

                    JLabel advertTitleLabel = new JLabel("Title: " + currentAdvert.getTitle());
                    advertTitleLabel.setFont(new Font("Arial",Font.BOLD, 20));
                    advertTitleLabel.setBounds(10,170,400,40);

                    JLabel priceLabel = new JLabel("Price: " + currentAdvert.getPrice());
                    priceLabel.setFont(new Font("Arial",Font.BOLD,20));
                    priceLabel.setBounds(10,200,400,40);

                    JLabel typeLabel = new JLabel("Type: " + currentAdvert.getType());
                    typeLabel.setFont(new Font("Arial",Font.BOLD,20));
                    typeLabel.setBounds(10,230,400,40);
                    
                    JLabel detailedInformationLabel = new JLabel("Detailed Information:");
                    detailedInformationLabel.setFont(new Font("Arial",Font.BOLD,20));
                    detailedInformationLabel.setBounds(10,260,400,40);

                    JTextArea detailedInformationArea = new JTextArea();
                    detailedInformationArea.setFont(new Font("Arial",Font.BOLD,15));
                    detailedInformationArea.setLineWrap(true);
                    detailedInformationArea.setWrapStyleWord(true);
                    detailedInformationArea.setEditable(false);
                    detailedInformationArea.setFocusable(false);
                    detailedInformationArea.setCaretPosition(0);
                    detailedInformationArea.append(currentAdvert.getDetailedInformation());
                    

                    JScrollPane detailedInformationScrollPane = new JScrollPane(detailedInformationArea);
                    detailedInformationScrollPane.setBackground(Color.white);
                    detailedInformationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    detailedInformationScrollPane.setBounds(10,300,360,150);


                    advertDetailsPanel.add(detailedInformationScrollPane);
                    advertDetailsPanel.add(detailedInformationLabel);
                    advertDetailsPanel.add(priceLabel);
                    advertDetailsPanel.add(typeLabel);
                    advertDetailsPanel.add(advertTitleLabel);


                    //Return Panel
                    
                    JPanel returnButtonPanel = new JPanel(){
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g;
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawLine(0,1,600,1);
                        }
                    };
                    returnButtonPanel.setPreferredSize(new Dimension(600,100));
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
                            
                            advertDetailsFrame.dispose();
                            
                        }
                        
                    });
                    returnButton.setBounds(200,25,200,50);
                    returnButtonPanel.add(returnButton);
                    

                    advertDetailsFrame.add(returnButtonPanel,BorderLayout.SOUTH);
                    advertDetailsFrame.add(userDetailsPanel,BorderLayout.WEST);
                    advertDetailsFrame.add(advertDetailsPanel,BorderLayout.CENTER);
                    advertDetailsFrame.setBackground(Color.white);
                    advertDetailsFrame.setPreferredSize(new Dimension(600,600));
                    advertDetailsFrame.pack();
                    advertDetailsFrame.setResizable(false);
                    advertDetailsFrame.setVisible(true);

                }
                
            });

            //Price 

            JLabel priceLabel = new JLabel(currentAdvert.getPrice()+" ₺");
            priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
            priceLabel.setBounds(540,20,100,80);

            //Bookmark Button
            JButton bookmarkedButton = new JButton();
            JButton notBookmarkedButton = new JButton();
            boolean isBookmarked = false;
            
            for (int j = 0; j < currentUserBookmarkedAdverts.size() && !isBookmarked; j++){

                if ( currentUserBookmarkedAdverts.get(j).getSellerUsername().equals(currentAdvert.getSellerUsername()) && currentUserBookmarkedAdverts.get(j).getTitle().equals(currentAdvert.getTitle()) ){
                    isBookmarked = true;
                }

            }

            
            bookmarkedButton.setIcon(filledBookmarkIcon);
            bookmarkedButton.setFocusable(false);
            bookmarkedButton.setBackground(Color.white);
            bookmarkedButton.setBorderPainted(false);
            bookmarkedButton.setBounds(605,40,40,40);
            bookmarkedButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    try {
                        int advertId = 0;
                        PreparedStatement advertIdFindStatement = Database.databaseConnection.prepareStatement("SELECT advertId FROM adverts WHERE sellerUsername = ? AND advertTitle = ?");
                        advertIdFindStatement.setString(1, currentAdvert.getSellerUsername());
                        advertIdFindStatement.setString(2, currentAdvert.getTitle());
                        ResultSet advertIdRs = advertIdFindStatement.executeQuery();
                        if (advertIdRs.next()){
                            advertId = advertIdRs.getInt("advertId");
                        }

                        PreparedStatement deleteBookmarkStatement = Database.databaseConnection.prepareStatement("DELETE FROM bookmarks WHERE advertId = ? AND bookmarkUsername = ?");
                        deleteBookmarkStatement.setInt(1, advertId);
                        deleteBookmarkStatement.setString(2, currentUser.getUsername());
                        deleteBookmarkStatement.executeUpdate();
                        

                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }
                        
                    bookmarkedButton.setVisible(false);
                    notBookmarkedButton.setVisible(true);

                }
                
            });

            
            notBookmarkedButton.setIcon(emptyBookmarkIcon);
            notBookmarkedButton.setFocusable(false);
            notBookmarkedButton.setBackground(Color.white);
            notBookmarkedButton.setBorderPainted(false); 
            notBookmarkedButton.setBounds(605,40,40,40);
            notBookmarkedButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    if ( currentAdvert.getSellerUsername().equals(currentUser.getUsername())){
                        JOptionPane.showMessageDialog(null, "You cannot bookmark your own advert!", "Cannot Bookmark",JOptionPane.ERROR_MESSAGE);
                    }
                    else{

                        try {

                            int advertId = 0;
                            PreparedStatement advertIdFindStatement = Database.databaseConnection.prepareStatement("SELECT advertId FROM adverts WHERE sellerUsername = ? AND advertTitle = ?");
                            advertIdFindStatement.setString(1, currentAdvert.getSellerUsername());
                            advertIdFindStatement.setString(2, currentAdvert.getTitle());
                            ResultSet advertIdRs = advertIdFindStatement.executeQuery();
                            if (advertIdRs.next()){
                                advertId = advertIdRs.getInt("advertId");
                            }
                            PreparedStatement addBookmarkStatement = Database.databaseConnection.prepareStatement("INSERT INTO bookmarks VALUES (?,?)");
                            addBookmarkStatement.setInt(1, advertId);
                            addBookmarkStatement.setString(2, currentUser.getUsername());
                            addBookmarkStatement.executeUpdate();
                            
    
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
    
                        bookmarkedButton.setVisible(true);
                        notBookmarkedButton.setVisible(false);

                    }   
                    
                    

                }
                
            });

            if ( isBookmarked ){
                bookmarkedButton.setVisible(true);
                notBookmarkedButton.setVisible(false);
            }
            else{
                bookmarkedButton.setVisible(false);
                notBookmarkedButton.setVisible(true);
            }


            //Reach Seller Button 

            JButton reachSellerButton = new JButton("Reach Seller");
            reachSellerButton.setBorder(new LineBorder(Color.black,1));
            reachSellerButton.setBounds(670,40,100,40);
            reachSellerButton.setFocusable(false);
            reachSellerButton.setBackground(new Color(151,12,16));
            reachSellerButton.setForeground(Color.white);
            reachSellerButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if ( currentAdvert.getSellerUsername().equals(currentUser.getUsername())){
                        JOptionPane.showMessageDialog(null, "This is your own advert!","Cannot Reach Seller",JOptionPane.ERROR_MESSAGE);
                    }
                    else{

                        try {
                            ArrayList<Integer> allAdvertIds = new ArrayList<>();
                            int advertId = 0;
                            PreparedStatement advertIdFindStatement = Database.databaseConnection.prepareStatement("SELECT advertId FROM adverts WHERE sellerUsername = ? AND advertTitle = ?");
                            advertIdFindStatement.setString(1, currentAdvert.getSellerUsername());
                            advertIdFindStatement.setString(2, currentAdvert.getTitle());
                            ResultSet advertIdRs = advertIdFindStatement.executeQuery();
                            if (advertIdRs.next()){
                                advertId = advertIdRs.getInt("advertId");
                            }
                            PreparedStatement checkReachSellerStatement  = Database.databaseConnection.prepareStatement("SELECT advertId FROM viewedadverts WHERE viewerUsername = ?");
                            checkReachSellerStatement.setString(1, currentUser.getUsername());
                            ResultSet checkReachSellerRs = checkReachSellerStatement.executeQuery();
                            while ( checkReachSellerRs.next() ){
                                allAdvertIds.add(checkReachSellerRs.getInt("advertId"));
                            }
                            if ( allAdvertIds.indexOf(advertId) < 0){
                                PreparedStatement reachSellerStatement = Database.databaseConnection.prepareStatement("INSERT INTO viewedadverts VALUES (?,?)");
                                reachSellerStatement.setInt(1, advertId);
                                reachSellerStatement.setString(2, currentUser.getUsername());
                                reachSellerStatement.executeUpdate();
                                EmailSender.sendInformationEmailForAdvert(currentUser, currentAdvert.getSellerUsername(), userDetailsEmail, currentAdvert);
                                JOptionPane.showMessageDialog(null, "The seller have been sent an email with your information!","Reached Seller",JOptionPane.INFORMATION_MESSAGE);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "You have already reached this seller before!","Cannot Reach Seller",JOptionPane.ERROR_MESSAGE);
                            }
                            
    
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                    }
                    
                                
                }
                            
            });
                        
            sampleAdvertPanel.add(notBookmarkedButton);
            sampleAdvertPanel.add(bookmarkedButton);
            sampleAdvertPanel.add(reachSellerButton);
            sampleAdvertPanel.add(titleButton);
            sampleAdvertPanel.add(priceLabel);
            sampleAdvertPanel.setBorder(new LineBorder(Color.black, 1));
            sampleAdvertPanel.setPreferredSize(new Dimension(750,120));
            advertsPanel.add(sampleAdvertPanel); 


        }
        advertsPanel.setBackground(Color.white);
        JScrollPane advertsPanelScrollPane = new JScrollPane(advertsPanel);
        advertsPanelScrollPane.setBackground(Color.white);
        advertsPanelScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //-------------------------------------------------


        JPanel filtersPanel = new JPanel();
        filtersPanel.setPreferredSize(new Dimension(200,600));
        filtersPanel.setBorder(new LineBorder(Color.black,1));
        filtersPanel.setLayout(null);
        filtersPanel.add(refreshButton);


        //Price filter 
        JLabel priceFilterLabel = new JLabel("Price Interval");
        priceFilterLabel.setBorder(new LineBorder(Color.black,1));
        
        priceFilterLabel.setFont(new Font("Arial",Font.BOLD+ Font.ITALIC,20));
        priceFilterLabel.setBounds(35,70,130,30);
        priceFilterLabel.setBackground(Color.white);

        JLabel minLabel = new JLabel("Min");
        minLabel.setFont(new Font("Arial",Font.BOLD, 15));
        minLabel.setBounds(35,100,60,30);

        minTextField = new JTextField();
        minTextField.setBounds(25,125,50,30);
        
        JLabel maxLabel = new JLabel("Max");
        maxLabel.setFont(new Font("Arial",Font.BOLD, 15));
        maxLabel.setBounds(130,100,60,30);

        maxTextField = new JTextField();
        maxTextField.setBounds(120,125,50,30);

        //Advert Type Filter
        ButtonGroup advertTypeButtonGroup = new ButtonGroup();
        allTypesButton = new JRadioButton("All Adverts");
        allTypesButton.setFont(new Font("Arial",Font.BOLD,15));
        allTypesButton.setBounds(10,165,150,20);
        allTypesButton.setFocusable(false);

        lectureMaterialButton = new JRadioButton("Lecture Material");
        lectureMaterialButton.setFont(new Font("Arial",Font.BOLD,15));
        lectureMaterialButton.setBounds(10,185,150,20);
        lectureMaterialButton.setFocusable(false);
        
        clothButton = new JRadioButton("Cloth");
        clothButton.setFont(new Font("Arial",Font.BOLD,15));
        clothButton.setBounds(10,205,150,20);
        clothButton.setFocusable(false);

        otherButton = new JRadioButton("Other");
        otherButton.setFont(new Font("Arial",Font.BOLD,15));
        otherButton.setBounds(10,225,150,20);
        otherButton.setFocusable(false);

        lectureMaterialButton.setActionCommand("Lecture Material");
        clothButton.setActionCommand("Cloth");
        otherButton.setActionCommand("Other");
        allTypesButton.setActionCommand("");

        advertTypeButtonGroup.add(allTypesButton);
        advertTypeButtonGroup.add(lectureMaterialButton);
        advertTypeButtonGroup.add(clothButton);
        advertTypeButtonGroup.add(otherButton);
        

        //Word Filter

        JLabel wordFilterLabel = new JLabel("  Word Filter");
        wordFilterLabel.setBorder(new LineBorder(Color.black,1));    
        wordFilterLabel.setFont(new Font("Arial",Font.BOLD + Font.ITALIC,20));
        wordFilterLabel.setBounds(35,260,130,30);
        wordFilterLabel.setBackground(Color.white);
        wordFilterTextField = new JTextField();
        wordFilterTextField.setFont(new Font("Arial",Font.ITALIC,15));
        wordFilterTextField.setBounds(25,300,150,30);


        //Username Filter

        JLabel usernameLabel = new JLabel(" Username Filter");
        usernameLabel.setBorder(new LineBorder(Color.black,1));    
        usernameLabel.setFont(new Font("Arial",Font.BOLD + Font.ITALIC,15));
        usernameLabel.setBounds(35,350,130,30);
        usernameLabel.setBackground(Color.white);
        usernameFilterTextField = new JTextField();
        usernameFilterTextField.setFont(new Font("Arial",Font.ITALIC,15));
        usernameFilterTextField.setBounds(25,390,150,30);
    
        
        //Search Button

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial",Font.BOLD,20));
        searchButton.setBackground(new Color(151,12,16));
        searchButton.setForeground(Color.white);
        searchButton.setFocusable(false);
        searchButton.setBounds(25,440,150,40);
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int searchButtonMin = 0;
                boolean isFullDigitsMinText = true;

               
                String minTextFieldString = minTextField.getText();  
                for (int j = 0; j < minTextFieldString.length() && isFullDigitsMinText ; j++){

                    if ( !Character.isDigit(minTextFieldString.charAt(j))){
                    isFullDigitsMinText = false;
                    }
                }
                    
                
                
                int searchButtonMax = 0;
                boolean isFullDigitsMaxText = true;


               

                String maxTextFieldString = maxTextField.getText();
                
                for (int j = 0; j < maxTextFieldString.length() && isFullDigitsMaxText ; j++){
                    if ( !Character.isDigit(maxTextFieldString.charAt(j))){
                        isFullDigitsMaxText = false;
                    }
                }
                
               
                if (!isFullDigitsMaxText && !isFullDigitsMinText){
                    JOptionPane.showMessageDialog(null, "Please enter a nonnegative integer value in the minimum and the maximum price field!","Incorrect Type",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if (!isFullDigitsMaxText){
                    JOptionPane.showMessageDialog(null, "Please enter a nonnegative integer value in the maximum price field!","Incorrect Type",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if (!isFullDigitsMinText){
                    JOptionPane.showMessageDialog(null, "Please enter a nonnegative integer value in the minimum price field!","Incorrect Type",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    if ( minTextField.getText().equals("")){
                        searchButtonMin = Integer.MIN_VALUE;             
                    }
                    else{
                        searchButtonMin = Integer.parseInt(minTextField.getText());
                    }
                    if ( maxTextField.getText().equals("")){
                        searchButtonMax = Integer.MAX_VALUE;             
                    }
                    else{
                        searchButtonMax = Integer.parseInt(maxTextField.getText());
                    }
                }

                


                ButtonModel selectedModel = advertTypeButtonGroup.getSelection();
                String searchButtonType = selectedModel.getActionCommand();

                String searchButtonWordFilter = wordFilterTextField.getText();
                if ( searchButtonWordFilter.equalsIgnoreCase("Enter a word")){
                    searchButtonWordFilter = "";
                }

                String searchButtonUsernameFilter = usernameFilterTextField.getText();
                if ( searchButtonUsernameFilter.equalsIgnoreCase("Enter a username")){
                    searchButtonUsernameFilter = "";
                }



                HomeScreen.hm.changePanel(new AdvertViewPanel(searchButtonMin, searchButtonMax, searchButtonType, searchButtonWordFilter, searchButtonUsernameFilter, currentUser));
                
                
            }
            
        });

        //Setting the filters

        if ( type.equalsIgnoreCase("Lecture Material")){
            lectureMaterialButton.setSelected(true);
        }
        else if ( type.equalsIgnoreCase("Cloth")){
            clothButton.setSelected(true);
        }
        else if ( type.equalsIgnoreCase("Other")){
            otherButton.setSelected(true);
        }
        else{
            allTypesButton.setSelected(true);
        }
        wordFilterTextField.setText(wordFilter);
        usernameFilterTextField.setText(usernameFilter);
        if( minPrice!= Integer.MIN_VALUE){
            minTextField.setText(minPrice+"");
        }
        else{
            minTextField.setText("");
        }
        if( maxPrice!= Integer.MAX_VALUE){
            maxTextField.setText(maxPrice+"");
        }
        else{
            maxTextField.setText("");
        }
  



        filtersPanel.add(allTypesButton);
        filtersPanel.add(searchButton);
        filtersPanel.add(usernameLabel);
        filtersPanel.add(usernameFilterTextField);
        filtersPanel.add(wordFilterLabel);
        filtersPanel.add(wordFilterTextField);
        filtersPanel.add(lectureMaterialButton);
        filtersPanel.add(clothButton);
        filtersPanel.add(otherButton);
        filtersPanel.add(maxTextField);
        filtersPanel.add(minTextField);
        filtersPanel.add(maxLabel);
        filtersPanel.add(priceFilterLabel);
        filtersPanel.add(minLabel);
        //------------------------------------------------

        
        this.add(filtersPanel,BorderLayout.WEST);
        this.add(advertsPanelScrollPane,BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(1000,600));      
        this.setVisible(true);

    }


    private ArrayList<Advert> getAllAdverts(){

        ArrayList<Advert> advertsList = new ArrayList<>();;
        try {
            
            PreparedStatement getAdvertsListStatement = Database.databaseConnection.prepareStatement("SELECT * FROM adverts ORDER BY advertId DESC");
            ResultSet advertsListRs = getAdvertsListStatement.executeQuery();
            while ( advertsListRs.next() ){

                BufferedImage advertImage = null;
                byte[] imageBytes = advertsListRs.getBytes("advertPicture");
                if (imageBytes != null) {
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                    try {
                        advertImage = ImageIO.read(byteInputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                }

                PreparedStatement getUserAvailabilityStatement = Database.databaseConnection.prepareStatement("SELECT available FROM users WHERE username = ?");
                getUserAvailabilityStatement.setString(1, advertsListRs.getString("sellerUsername"));
                ResultSet getUserAvailabilityRs = getUserAvailabilityStatement.executeQuery();
                boolean userAvailability = true;
                if ( getUserAvailabilityRs.next() ){
                    userAvailability = getUserAvailabilityRs.getBoolean("available");
                }

                if (advertsListRs.getBoolean("availability") && userAvailability){
                    advertsList.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getInt("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));
                }
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return advertsList;
    }

    private ArrayList<Advert> filterAllAdverts(int minPrice,int maxPrice,String type,String wordFilter,String usernameFilter){
        ArrayList<Advert> filteredAdvertsList = new ArrayList<>();

        for (int i = 0; i <allAdvertsList.size(); i++){

            Advert currentAdvert = allAdvertsList.get(i);
            if ( type.equals("") ){
                if ( currentAdvert.getPrice() >= minPrice && currentAdvert.getPrice() <= maxPrice && currentAdvert.getSellerUsername().toLowerCase().indexOf(usernameFilter.toLowerCase()) >= 0 && currentAdvert.getTitle().toLowerCase().indexOf(wordFilter.toLowerCase()) >= 0 ){
                    filteredAdvertsList.add(currentAdvert);
                }
            }
            else{
                if ( currentAdvert.getPrice() >= minPrice && currentAdvert.getPrice() <= maxPrice && currentAdvert.getType().equalsIgnoreCase(type) && currentAdvert.getSellerUsername().toLowerCase().indexOf(usernameFilter.toLowerCase()) >= 0 && currentAdvert.getTitle().toLowerCase().indexOf(wordFilter.toLowerCase()) >= 0){
                    filteredAdvertsList.add(currentAdvert);
                }
            }
        }

        return filteredAdvertsList;
    }

}

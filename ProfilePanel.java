import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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

  

    public ProfilePanel(String username, User currentUser){ // These parameters are to see if the user is looking at their own profile or other's profile.
        this.username = username;
        this.currentUser = currentUser;
        this.setLayout(null);
        this.setBackground(Color.white);

        try{
            profilePicture = ImageIO.read(new File("profile-picture.png")); 
            emptyStar = ImageIO.read(new File("emptystar.png")); 
            oneQuarterStar = ImageIO.read(new File("onequarterstar.png")); 
            halfStar = ImageIO.read(new File("halfstar.png"));
            threeQuarterStar = ImageIO.read(new File("threequarterstar.png"));  
            fullStar = ImageIO.read(new File("fullstar.png"));           
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

            ImageIcon homeIcon = new ImageIcon("homeIcon.png");
            Image homeIconImage = homeIcon.getImage();
            homeIconImage = homeIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newHomeIcon = new ImageIcon(homeIconImage);
            homeButton.setIcon(newHomeIcon);
            homeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentUser.setIsAvailable(true);
                    JOptionPane.showMessageDialog(null, "You have set yourself as available", "Availability", JOptionPane.INFORMATION_MESSAGE,newHomeIcon);
                }
                
            });
            homeButton.setBounds(700,180, 60,40);

            JToggleButton awayButton = new JToggleButton();
            ImageIcon awayIcon = new ImageIcon("awayIcon.png");
            Image awayIconImage = awayIcon.getImage();
            awayIconImage = awayIconImage.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH);
            ImageIcon newAwayIcon = new ImageIcon(awayIconImage);
            awayButton.setIcon(newAwayIcon);
            awayButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentUser.setIsAvailable(false);
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

            JButton ratingCountButton = new JButton("X Ratings");
            ratingCountButton.setBorder(new LineBorder(Color.BLACK,1));
            ratingCountButton.setBounds(160,370,120,40);
            ratingCountButton.setFont(new Font("Arial", Font.BOLD, 20));
            ratingCountButton.setFocusable(false);  
            ratingCountButton.setBackground(Color.white);
            ratingCountButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
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
            for (int i = 1; i <= 100; i++){
                reviewArea.append("\n");
                reviewArea.append("User" + i + ": BLAH BLAH BLAH BLAAH \n");
            }
            

            JScrollPane scrollPaneForReviewArea = new JScrollPane(reviewArea);
            scrollPaneForReviewArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPaneForReviewArea.setBorder(new LineBorder(Color.BLACK,1));
            scrollPaneForReviewArea.setBounds(30,430,500,300);
            scrollPaneForReviewArea.setBackground(Color.white);
            
            JLabel reviewLabel = new JLabel("  X Reviews");
            reviewLabel.setBorder(new LineBorder(Color.BLACK,1));
            reviewLabel.setBounds(30,370,120,40);
            reviewLabel.setFont(new Font("Arial", Font.BOLD, 20));
            
            //Adverts Found

            JButton advertsFoundButton = new JButton("X Adverts Found");
            advertsFoundButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Directing...", "Title", JOptionPane.INFORMATION_MESSAGE);
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

                    JPanel advertsPanel = new JPanel();
                    advertsPanel.setLayout(new GridLayout(30,1));
                    for (int i = 0; i < 10; i++){

                        JPanel greenPanel = new JPanel();
                        greenPanel.setBackground(Color.green);
                        greenPanel.setPreferredSize(new Dimension(100,100));
                        JPanel redPanel = new JPanel();
                        redPanel.setBackground(Color.red);
                        redPanel.setPreferredSize(new Dimension(100,100));
                        JPanel bluePanel = new JPanel();
                        bluePanel.setBackground(Color.blue);
                        bluePanel.setPreferredSize(new Dimension(100,100));
                        advertsPanel.add(greenPanel);
                        advertsPanel.add(redPanel);
                        advertsPanel.add(bluePanel);

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
                    returnButton.setBackground(Color.red);
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
                    advertsPanel.setLayout(new GridLayout(30,1));
                    for (int i = 0; i < 10; i++){

                        JPanel greenPanel = new JPanel();
                        greenPanel.setBackground(Color.green);
                        greenPanel.setPreferredSize(new Dimension(100,100));
                        JPanel redPanel = new JPanel();
                        redPanel.setBackground(Color.red);
                        redPanel.setPreferredSize(new Dimension(100,100));
                        JPanel bluePanel = new JPanel();
                        bluePanel.setBackground(Color.blue);
                        bluePanel.setPreferredSize(new Dimension(100,100));
                        advertsPanel.add(greenPanel);
                        advertsPanel.add(redPanel);
                        advertsPanel.add(bluePanel);

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
                    returnButton.setBackground(Color.red);
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
            logoutButton.setBackground(Color.red);
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
            
        }


        this.setPreferredSize(new Dimension(1024,768));      
        this.setVisible(true); 

    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(profilePicture, 120, 90, 150,150, this);
        for (int i = 0; i < 5; i++){
            g.drawImage(emptyStar, 60 + 45*i, 300, 45, 45, this);
        }
        if ( currentUser.getUsername().equals(username) ){
            double rating = currentUser.getRating();
            int drawnStarAmount = 0;
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
        else{

        }


    }   


}
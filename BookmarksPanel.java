import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookmarksPanel extends JPanel {

    private BufferedImage advertImageForUserDetails;


    public BookmarksPanel(User currentUser){


        //Refresh Button 
        
        JButton refreshButton = new JButton();
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Main.frame and Main.currentPanel will change (the names)!!
                Main.frame.getContentPane().remove(Main.currentPanel);
                Main.currentPanel = new BookmarksPanel(currentUser);
                Main.frame.getContentPane().add(Main.currentPanel);
                Main.frame.revalidate();
                Main.frame.repaint();
            }
            
        });




        ArrayList<Advert> bookmarkedAdvertsList = currentUser.getBookmarkedAdverts();
        

        JPanel allAdvertsPanel = new JPanel(); // To store every sample advert panel
        allAdvertsPanel.setBackground(Color.white);
        allAdvertsPanel.setLayout(new GridLayout(bookmarkedAdvertsList.size(),1));
        

        for (int i = 0; i < bookmarkedAdvertsList.size(); i++){

            Advert currentAdvert = bookmarkedAdvertsList.get(i);
                       

            JPanel sampleAdvertPanel = new JPanel(){
                protected void paintComponent(Graphics g) {
                    g.drawImage(currentAdvert.getImage(), 20, 20, 120,120,null );
                };
            };
                   
            sampleAdvertPanel.setLayout(null);
            sampleAdvertPanel.setBackground(Color.white);


            //Title
            //---------------------------------------------------------------------------------
            JButton titleButton = new JButton(" " + currentAdvert.getTitle());
            titleButton.setBorder(new LineBorder(Color.black,1));
            titleButton.setBounds(180,40,450,80);
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
                    String userDetailsEmail = "";


                    JLabel userDetailUsernameLabel = new JLabel(currentAdvert.getSellerUsername());
                    userDetailUsernameLabel.setFont(new Font("Arial",Font.BOLD,20));
                    userDetailUsernameLabel.setBounds(35,270,150,50);

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

                    userDetailsPanel.add(userDetailUsernameLabel);
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
                    detailedInformationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
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
                    returnButton.setBackground(Color.red);
                    returnButton.setForeground(Color.white);
                    returnButton.addActionListener(new ActionListener() {
        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            refreshButton.doClick();
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
                    advertDetailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    advertDetailsFrame.setResizable(false);
                    advertDetailsFrame.setVisible(true);

                }
                
            });
            //----------------------------------------------------------

            //Price
            JLabel priceLabel = new JLabel(currentAdvert.getPrice()+" ₺");
            priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
            priceLabel.setBounds(680,40,100,80);


            //Delete Bookmark Button

            JButton deleteBookmarkButton = new JButton("Delete Bookmark");
            deleteBookmarkButton.setFocusable(false);
            deleteBookmarkButton.setBackground(Color.red);
            deleteBookmarkButton.setForeground(Color.white);
            deleteBookmarkButton.setFont(new Font("Arial",Font.BOLD,15));
            deleteBookmarkButton.setBounds(780,50,180,60);
            deleteBookmarkButton.addActionListener(new ActionListener() {

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
                        JOptionPane.showMessageDialog(null, "You have deleted the bookmark of the advert", "Bookmark Deleted", JOptionPane.INFORMATION_MESSAGE);

                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }
                    refreshButton.doClick();

                }
                
            });

            sampleAdvertPanel.add(deleteBookmarkButton);
            sampleAdvertPanel.setPreferredSize(new Dimension(1000,200));
            sampleAdvertPanel.add(priceLabel);
            sampleAdvertPanel.add(titleButton);

            allAdvertsPanel.add(sampleAdvertPanel);

        
        }

        JScrollPane bookmarkedAdvertsScrollPane = new JScrollPane(allAdvertsPanel);//make it a scroll pane
        bookmarkedAdvertsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        bookmarkedAdvertsScrollPane.setBackground(Color.white);

        this.setLayout(new BorderLayout());
        this.add(bookmarkedAdvertsScrollPane,BorderLayout.CENTER);
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(1024,720));
        this.setVisible(true);
        

    }



}

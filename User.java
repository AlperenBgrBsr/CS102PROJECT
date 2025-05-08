import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;



public class User {
    private String username;
    private String email;
    private String password;
    private double rating;
    private int ratingAmount;
    private ArrayList<Rating> ratingsList;
    private boolean isAvailable;
    private ArrayList<Review> reviewsList;
    private int reviewsCount;
    private int advertsCount;
    private ArrayList<Advert> advertsList;
    private ArrayList<Advert> bookmarkedAdverts;
    private ArrayList<Advert> viewedAdverts;
    private BufferedImage profilePicture;
   
    


    public User(String username, String email, String password, boolean isAvailable) {
        this(username,password);
        this.email = email;
        this.rating = 0;
        this.isAvailable = isAvailable;
        this.ratingAmount = 0;
        reviewsList = new ArrayList<>();
        reviewsCount = 0;
        advertsCount = 0;
        advertsCount = 0;
        advertsList = new ArrayList<>();
        viewedAdverts = new ArrayList<>();
        bookmarkedAdverts = new ArrayList<>();
        ratingsList = new ArrayList<>();

        try { //When first instantited automically default profile picture
            if (profilePicture == null)
            profilePicture = toBufferedImage(ImageIO.read(new File("icons\\profile-picture.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    public User(String username, String email, String password) {

        this(username,password);
        this.email = email;
        this.rating = 0;
        this.isAvailable = true;
        this.ratingAmount = 0;
        reviewsList = new ArrayList<>();
        reviewsCount = 0;
        advertsCount = 0;
        advertsCount = 0;
        advertsList = new ArrayList<>();
        viewedAdverts = new ArrayList<>();
        bookmarkedAdverts = new ArrayList<>();
        ratingsList = new ArrayList<>();

        try { //When first instantited automically default profile picture
            if (profilePicture == null)
            profilePicture = toBufferedImage(ImageIO.read(new File("icons\\profile-picture.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
  
        
    }

    public ArrayList<Advert> getViewedAdverts() {
        viewedAdverts = new ArrayList<>();
        try {
            PreparedStatement getViewedAdvertsStatement = Database.databaseConnection.prepareStatement("SELECT advertId FROM viewedadverts WHERE viewerUsername = ?");
            getViewedAdvertsStatement.setString(1, username);
            ResultSet viewedResultSet = getViewedAdvertsStatement.executeQuery();
            while (viewedResultSet.next()){

                int advertId = viewedResultSet.getInt("advertId");
                PreparedStatement getAdvertsWithId = Database.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE advertId = ?");
                getAdvertsWithId.setInt(1, advertId);
                ResultSet advertsListRs = getAdvertsWithId.executeQuery();
                while (advertsListRs.next()){
                    
                    BufferedImage advertImage = null;
                    byte[] imageBytes = advertsListRs.getBytes("advertPicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        advertImage = ImageIO.read(byteInputStream); 
                    }

                    viewedAdverts.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getInt("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));

                    
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewedAdverts;
    }
    public ArrayList<Advert> getBookmarkedAdverts() {
        bookmarkedAdverts = new ArrayList<>();
        try {
            PreparedStatement getBookmarkedAdvertsStatement = Database.databaseConnection.prepareStatement("SELECT advertId FROM bookmarks WHERE bookmarkUsername = ?");
            getBookmarkedAdvertsStatement.setString(1, username);
            ResultSet bookmarkResultSet = getBookmarkedAdvertsStatement.executeQuery();
            while (bookmarkResultSet.next()){

                int advertId = bookmarkResultSet.getInt("advertId");
                PreparedStatement getAdvertsWithId = Database.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE advertId = ?");
                getAdvertsWithId.setInt(1, advertId);
                ResultSet advertsListRs = getAdvertsWithId.executeQuery();
                while (advertsListRs.next()){

                    BufferedImage advertImage = null;
                    byte[] imageBytes = advertsListRs.getBytes("advertPicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        advertImage = ImageIO.read(byteInputStream); 
                    }

                    bookmarkedAdverts.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getInt("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));


                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.reverse(bookmarkedAdverts);
        return bookmarkedAdverts;
    }
    public int getAdvertsCount() {
        advertsCount = 0;
        try {
            PreparedStatement getAdvertsListStatement = Database.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE sellerUsername = ?");
            getAdvertsListStatement.setString(1, username);
            ResultSet advertsListRs = getAdvertsListStatement.executeQuery();
            while ( advertsListRs.next() ){

                advertsCount++;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return advertsCount;
    }

    public ArrayList<Advert> getAdvertsList() {
    
        try {
            
            advertsList = new ArrayList<>();
            PreparedStatement getAdvertsListStatement = Database.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE sellerUsername = ?");
            getAdvertsListStatement.setString(1, username);
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
                advertsList.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getInt("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return advertsList;
    }
    
    public int getReviewsCount() {
        reviewsCount = 0;
        PreparedStatement getReviewsStatement;
        try {
            getReviewsStatement = Database.databaseConnection.prepareStatement("SELECT * FROM reviews WHERE recieverId = (SELECT user_Id FROM users WHERE username = ?)");
            getReviewsStatement.setString(1, username);
            ResultSet reviewsRs = getReviewsStatement.executeQuery();
            while (reviewsRs.next()){
                reviewsCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewsCount;
    }
    public ArrayList<Review> getReviewsList() {
        try {
            reviewsList = new ArrayList<>();
            PreparedStatement getReviewsStatement = Database.databaseConnection.prepareStatement("SELECT * FROM reviews WHERE recieverId = (SELECT user_Id FROM users WHERE username = ?)");
            getReviewsStatement.setString(1, username);
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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String anemail) {
        this.email = anemail;
    }

    public void setUsername(String ausername) {
        this.username = ausername;
    }

    public void setPassword(String apassword) {
        this.password = apassword;
    }
    public ArrayList<Rating> getRatingsList(){
        ratingsList = new ArrayList<>();

        try {
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT * FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, username);
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

                ratingsList.add(new Rating(senderUsername, username, currentRating));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingsList;
    }
    public double getRating() {
        try {
            rating = 0;
            ratingAmount = 0;
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, username);
            ResultSet rs = ratingStatement.executeQuery();
            while (rs.next()){
                rating += rs.getInt("rating");
                ratingAmount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rating/ratingAmount;

    }
    public int getRatingAmount() {
        try {
            ratingAmount = 0;
            PreparedStatement ratingStatement = Database.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
            ratingStatement.setString(1, username);
            ResultSet rs = ratingStatement.executeQuery();
            while (rs.next()){
                ratingAmount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingAmount;
    }
    public boolean checkAvailability(){
        return isAvailable;
    }
    public BufferedImage getProfilePicture() {
        BufferedImage currentProfilePicture = null;
        
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

        return currentProfilePicture;
    }
    public void setProfilePictrue(Image image) {
        profilePicture = toBufferedImage(image);
    }

    public void updateAvailability(boolean isAvailable){
        this.isAvailable = isAvailable;
    }
     private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage bufferedImage) {
            return bufferedImage;
        }

        // Make sure the image is fully loaded
        img = new ImageIcon(img).getImage();

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

}

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;



public class User {
    private String username;
    private String email;
    private String password;
    private double rating;
    private int ratingAmount;
    private boolean isAvailable;
    private ArrayList<Review> reviewsList;
    private int reviewsCount;
    private int advertsCount;
    private ArrayList<Advert> advertsList;
    private ArrayList<Advert> bookmarkedAdverts;
    private ArrayList<Advert> viewedAdverts;

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
        
    }

    public ArrayList<Advert> getViewedAdverts() {
        viewedAdverts = new ArrayList<>();
        try {
            PreparedStatement getViewedAdvertsStatement = Main.databaseConnection.prepareStatement("SELECT advertId FROM viewedadverts WHERE viewerUsername = ?");
            getViewedAdvertsStatement.setString(1, username);
            ResultSet viewedResultSet = getViewedAdvertsStatement.executeQuery();
            while (viewedResultSet.next()){

                int advertId = viewedResultSet.getInt("advertId");
                PreparedStatement getAdvertsWithId = Main.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE advertId = ?");
                getAdvertsWithId.setInt(1, advertId);
                ResultSet advertsListRs = getAdvertsWithId.executeQuery();
                while (advertsListRs.next()){
                    
                    BufferedImage advertImage = null;
                    byte[] imageBytes = advertsListRs.getBytes("advertPicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        advertImage = ImageIO.read(byteInputStream); 
                    }

                    viewedAdverts.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getString("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));

                    
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
            PreparedStatement getBookmarkedAdvertsStatement = Main.databaseConnection.prepareStatement("SELECT advertId FROM bookmarks WHERE bookmarkUsername = ?");
            getBookmarkedAdvertsStatement.setString(1, username);
            ResultSet bookmarkResultSet = getBookmarkedAdvertsStatement.executeQuery();
            while (bookmarkResultSet.next()){

                int advertId = bookmarkResultSet.getInt("advertId");
                PreparedStatement getAdvertsWithId = Main.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE advertId = ?");
                getAdvertsWithId.setInt(1, advertId);
                ResultSet advertsListRs = getAdvertsWithId.executeQuery();
                while (advertsListRs.next()){

                    BufferedImage advertImage = null;
                    byte[] imageBytes = advertsListRs.getBytes("advertPicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        advertImage = ImageIO.read(byteInputStream); 
                    }

                    bookmarkedAdverts.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getString("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));


                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookmarkedAdverts;
    }
    public int getAdvertsCount() {
        advertsCount = 0;
        try {
            PreparedStatement getAdvertsListStatement = Main.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE sellerUsername = ?");
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
            PreparedStatement getAdvertsListStatement = Main.databaseConnection.prepareStatement("SELECT * FROM adverts WHERE sellerUsername = ?");
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
                advertsList.add(new Advert(advertImage, advertsListRs.getString("advertTitle"), advertsListRs.getString("advertPrice"), advertsListRs.getString("advertDetails"), advertsListRs.getString("sellerUsername"), advertsListRs.getBoolean("availability"), advertsListRs.getString("type")));

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
            getReviewsStatement = Main.databaseConnection.prepareStatement("SELECT * FROM reviews WHERE recieverId = (SELECT user_Id FROM users WHERE username = ?)");
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
            PreparedStatement getReviewsStatement = Main.databaseConnection.prepareStatement("SELECT * FROM reviews WHERE recieverId = (SELECT user_Id FROM users WHERE username = ?)");
            getReviewsStatement.setString(1, username);
            ResultSet reviewsRs = getReviewsStatement.executeQuery();
            while (reviewsRs.next()){

                int senderId = reviewsRs.getInt("sender_Id");
                int recieverId = reviewsRs.getInt("recieverId");
                String reviewContent = reviewsRs.getString("review");
                
                String senderUsername = "";
                PreparedStatement senderUsernameStatement = Main.databaseConnection.prepareStatement("SELECT username FROM users WHERE user_id = ?");
                senderUsernameStatement.setInt(1, senderId);
                ResultSet senderRs = senderUsernameStatement.executeQuery();
                while (senderRs.next()){
                    senderUsername = senderRs.getString("username");
                }
 
                String recieverUsername = "";
                PreparedStatement recieverUsernameStatement = Main.databaseConnection.prepareStatement("SELECT username FROM users WHERE user_id = ?");
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
    public double getRating() {
        try {
            rating = 0;
            ratingAmount = 0;
            PreparedStatement ratingStatement = Main.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
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
            PreparedStatement ratingStatement = Main.databaseConnection.prepareStatement("SELECT rating FROM ratings WHERE recieverId = (SELECT user_id FROM users WHERE username = ?)");
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
    public boolean getIsAvailable(){
        return isAvailable;
    }
    public void setIsAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }
    public void addToDatabase(){
        PreparedStatement addToDatabaseStatement;
        try {
            addToDatabaseStatement = Main.databaseConnection.prepareStatement("INSERT INTO USERS (user_email,user_password,available,username) VALUES (?,?,?,?)");
            addToDatabaseStatement.setString(1, email);
            addToDatabaseStatement.setString(2, password);
            addToDatabaseStatement.setBoolean(3, isAvailable);
            addToDatabaseStatement.setString(4, username);
            addToDatabaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}

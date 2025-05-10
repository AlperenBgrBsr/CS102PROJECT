import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Database {

    public static Connection databaseConnection;

    public static void createConnection()  {
        
        try {
            databaseConnection = DriverManager.getConnection("jdbc:mysql://bilmartserver.mysql.database.azure.com/bilmartdb", "bilmartdbuser", "uhcGEFT!*oad194");
            System.out.println("Connection complete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public static void deleteFromDatabase(Rating rating){
        try {
            PreparedStatement deleteRatingStatement = databaseConnection.prepareStatement("DELETE FROM ratings WHERE sender_Id = (SELECT user_id FROM users WHERE username = ?) AND recieverId = (SELECT user_id FROM users WHERE username = ?)");
            deleteRatingStatement.setString(1, rating.getSenderUsername());
            deleteRatingStatement.setString(2, rating.getRecieverUsername());
            deleteRatingStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToDatabase(Rating rating){
        try {
            int senderId = 0; 
            int recieverId = 0;
            PreparedStatement senderIdStatement = Database.databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            senderIdStatement.setString(1, rating.getSenderUsername());
            ResultSet senderRs = senderIdStatement.executeQuery();
            while (senderRs.next()){
                senderId = senderRs.getInt("user_Id");
            }
            PreparedStatement recieverIdStatement = Database.databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            recieverIdStatement.setString(1, rating.getRecieverUsername());
            ResultSet recieverRs = recieverIdStatement.executeQuery();
            while (recieverRs.next()){
                recieverId = recieverRs.getInt("user_Id");
            }
            PreparedStatement addRatingStatement = Database.databaseConnection.prepareStatement("INSERT INTO ratings VALUES(?,?,?)");
            addRatingStatement.setInt(1, senderId);
            addRatingStatement.setInt(2, recieverId);
            addRatingStatement.setInt(3, rating.getRating());
            addRatingStatement.executeUpdate();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void addToDatabase(User user){
        PreparedStatement addToDatabaseStatement;
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ImageIO.write(user.getProfilePicture(), "png", byteOutputStream);
            byte[] imageBytes = byteOutputStream.toByteArray();
            addToDatabaseStatement = databaseConnection.prepareStatement("INSERT INTO USERS (user_email,user_password,available,username,profilePicture) VALUES (?,?,?,?,?)");
            addToDatabaseStatement.setString(1, user.getEmail());
            addToDatabaseStatement.setString(2, user.getPassword());
            addToDatabaseStatement.setBoolean(3, user.checkAvailability());
            addToDatabaseStatement.setString(4, user.getUsername());
            addToDatabaseStatement.setBytes(5, imageBytes);
            addToDatabaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    public static void addToDatabase(Review review){
        try {
            
            int senderId = 0; 
            int recieverId = 0;
            PreparedStatement senderIdStatement = databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            senderIdStatement.setString(1, review.getSenderUsername());
            ResultSet senderRs = senderIdStatement.executeQuery();
            while (senderRs.next()){
                senderId = senderRs.getInt("user_Id");
            }
            PreparedStatement recieverIdStatement = databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            recieverIdStatement.setString(1, review.getRecieverUsername());
            ResultSet recieverRs = recieverIdStatement.executeQuery();
            while (recieverRs.next()){
                recieverId = recieverRs.getInt("user_Id");
            }
            PreparedStatement reviewToDatabaseStatement = databaseConnection.prepareStatement("INSERT INTO reviews VALUES (?,?,?)");
            reviewToDatabaseStatement.setInt(1, senderId);
            reviewToDatabaseStatement.setInt(2, recieverId);
            reviewToDatabaseStatement.setString(3, review.getReviewContent());
            reviewToDatabaseStatement.executeUpdate();
            
        } catch (SQLException e) {  
            e.printStackTrace();
        }
    }

    public static void addToDatabase(Message message){
        try {
            PreparedStatement addToDatabaseStatement = databaseConnection.prepareStatement("INSERT INTO messages VALUES (?,?,?)");
            addToDatabaseStatement.setString(1, message.getSenderUsername());
            addToDatabaseStatement.setString(2, message.getRecieverUsername());
            addToDatabaseStatement.setString(3, message.getMessageContent());
            addToDatabaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToDatabase(Advert advert){
        try {

            PreparedStatement addToDatabaseStatement = databaseConnection.prepareStatement("INSERT INTO adverts(advertPicture,advertTitle,advertDetails,availability,sellerUsername,type,advertPrice) VALUES (?,?,?,?,?,?,?)" );
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ImageIO.write(advert.getImage(), "png", byteOutputStream);
            byte[] imageBytes = byteOutputStream.toByteArray();
            addToDatabaseStatement.setBytes(1, imageBytes);
            addToDatabaseStatement.setString(2, advert.getTitle());
            addToDatabaseStatement.setString(3, advert.getDetailedInformation());
            addToDatabaseStatement.setBoolean(4, advert.checkAvailability());
            addToDatabaseStatement.setString(5, advert.getSellerUsername());
            addToDatabaseStatement.setString(6, advert.getType());
            addToDatabaseStatement.setInt(7, advert.getPrice());
            addToDatabaseStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void setProfilePictureInDatabase(Image img, String username){

        BufferedImage bimage = null;

        if (!(img instanceof BufferedImage)) {
            img = new ImageIcon(img).getImage();

        
            bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();
        }
        else {
            bimage = (BufferedImage) img;
        }

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bimage, "png", byteOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = byteOutputStream.toByteArray();
        

        try {
            PreparedStatement setProfilePictureInDatabaseStatement = databaseConnection.prepareStatement("UPDATE users SET profilePicture = ? WHERE username = ?");
            setProfilePictureInDatabaseStatement.setBytes(1, imageBytes);
            setProfilePictureInDatabaseStatement.setString(2, username);
            setProfilePictureInDatabaseStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        

    }
    public static ArrayList<User> getAllUsersForRegisterAndLogin(){

        //this will only return the emails, passwords and the usernames of the all of the users, other parameters will be set to default but they wont be needed
        ArrayList<User> allUsers = new ArrayList<>();

        try {
            PreparedStatement getAllUsersStatement = databaseConnection.prepareStatement("SELECT * FROM users");
            ResultSet getAllUsersRs = getAllUsersStatement.executeQuery();
            while ( getAllUsersRs.next() ){
                allUsers.add(new User(getAllUsersRs.getString("username"), getAllUsersRs.getString("user_email"), getAllUsersRs.getString("user_password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;

    }

    public static User getUserWithUsername(String username){
        User user = null;
        try {
            
            PreparedStatement getUserStatement = databaseConnection.prepareStatement("SELECT * FROM users WHERE username = ?");
            getUserStatement.setString(1, username);
            ResultSet getUserRs = getUserStatement.executeQuery();
            if ( getUserRs.next()){
                user = new User(getUserRs.getString("username"), getUserRs.getString("user_email"), getUserRs.getString("user_password"), getUserRs.getBoolean("available"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }


}

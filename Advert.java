
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;

public class Advert {

    String title;
    String price;
    BufferedImage image;
    String detailedInformation;
    String buyerUsername;
    String sellerUsername;
    boolean isAvailable;
    String type;

    public Advert(BufferedImage image, String title, String price, String detailedInformation, String buyerUsername, String sellerUsername, boolean isAvailable, String type){
        this.image = image;
        this.title = title;
        this.price = price;
        this.detailedInformation = detailedInformation;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.isAvailable = isAvailable; 
        this.type = type;
    } 


    

    public String getTitle() {
        return title;
    }




    public String getPrice() {
        return price;
    }




    public BufferedImage getImage() {
        return image;
    }




    public String getDetailedInformation() {
        return detailedInformation;
    }




    public String getBuyerUsername() {
        return buyerUsername;
    }




    public String getSellerUsername() {
        return sellerUsername;
    }




    public boolean isAvailable() {
        return isAvailable;
    }




    public String getType() {
        return type;
    }




    public void addToDatabase(){
        try {

            PreparedStatement addToDatabaseStatement = Main.databaseConnection.prepareStatement("INSERT INTO adverts(advertPicture,advertTitle,advertDetails,availability,sellerUsername,buyerUsername,type,advertPrice) VALUES (?,?,?,?,?,?,?,?)" );
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteOutputStream);
            byte[] imageBytes = byteOutputStream.toByteArray();
            addToDatabaseStatement.setBytes(1, imageBytes);
            addToDatabaseStatement.setString(2, title);
            addToDatabaseStatement.setString(3, detailedInformation);
            addToDatabaseStatement.setBoolean(4, isAvailable);
            addToDatabaseStatement.setString(5, sellerUsername);
            addToDatabaseStatement.setString(6, buyerUsername);
            addToDatabaseStatement.setString(7, type);
            addToDatabaseStatement.setString(8, price);
            addToDatabaseStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

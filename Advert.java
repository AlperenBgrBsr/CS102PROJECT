
import java.awt.image.BufferedImage;

public class Advert {

    private String title;
    private String price;
    private BufferedImage image;
    private String detailedInformation;
    private String sellerUsername;
    private boolean isAvailable;
    private String type;

    public Advert(BufferedImage image, String title, String price, String detailedInformation, String sellerUsername, boolean isAvailable, String type){
        this.image = image;
        this.title = title;
        this.price = price;
        this.detailedInformation = detailedInformation;
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


    public String getSellerUsername() {
        return sellerUsername;
    }




    public boolean checkAvailability() {
        return isAvailable;
    }




    public String getType() {
        return type;
    }
    public void updateAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}

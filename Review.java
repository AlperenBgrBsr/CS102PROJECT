import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Review {
    private String senderUsername;
    private String recieverUsername;
    private String reviewContent;

    public Review(String senderUsername, String recieverUsername, String review){

        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
        this.reviewContent = review;
    }
    public void addToDatabase(){
        try {
            
            int senderId = 0; 
            int recieverId = 0;
            PreparedStatement senderIdStatement = Main.databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            senderIdStatement.setString(1, senderUsername);
            ResultSet senderRs = senderIdStatement.executeQuery();
            while (senderRs.next()){
                senderId = senderRs.getInt("user_Id");
            }
            PreparedStatement recieverIdStatement = Main.databaseConnection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            recieverIdStatement.setString(1, recieverUsername);
            ResultSet recieverRs = recieverIdStatement.executeQuery();
            while (recieverRs.next()){
                recieverId = recieverRs.getInt("user_Id");
            }
            PreparedStatement reviewToDatabaseStatement = Main.databaseConnection.prepareStatement("INSERT INTO reviews VALUES (?,?,?)");
            reviewToDatabaseStatement.setInt(1, senderId);
            reviewToDatabaseStatement.setInt(2, recieverId);
            reviewToDatabaseStatement.setString(3, reviewContent);
            reviewToDatabaseStatement.executeUpdate();
            
        } catch (SQLException e) {  
            e.printStackTrace();
        }
    }
    public String getRecieverUsername() {
        return recieverUsername;
    }
    public String getReview() {
        return reviewContent;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
}

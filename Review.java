
public class Review {
    private String senderUsername;
    private String recieverUsername;
    private String reviewContent;

    public Review(String senderUsername, String recieverUsername, String review){

        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
        this.reviewContent = review;
    }
    
    public String getRecieverUsername() {
        return recieverUsername;
    }
    public String getReviewContent() {
        return reviewContent;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
}

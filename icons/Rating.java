public class Rating {

    private String senderUsername;
    private String recieverUsername;
    private int rating;
    public Rating(String senderUsername, String recieverUsername, int rating){

        this.rating = rating;
        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;

    }
    public int getRating() {
        return rating;
    }
    public String getRecieverUsername() {
        return recieverUsername;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
}

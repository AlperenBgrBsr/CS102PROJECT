import java.util.Date;

public class Message {

    private String senderUsername;
    private String recieverUsername;
    private String messageContent;
    private Date messageTime;

    public Message ( String senderUsername, String recieverUsername, String messageContent, Date messageTime){

        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
        this.messageContent = messageContent;
        this.messageTime = messageTime;

    }
    public String getMessageContent() {
        return messageContent;
    }
    public String getRecieverUsername() {
        return recieverUsername;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public Date getTimestamp() {
        return messageTime;
    }
    public boolean isSentByUser(User user) {
        return user.getUsername().equalsIgnoreCase(senderUsername);
    }

    
    

}
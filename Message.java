public class Message {

    private String senderUsername;
    private String recieverUsername;
    private String messageContent;

    public Message ( String senderUsername, String recieverUsername, String messageContent){

        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
        this.messageContent = messageContent;
        

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
}
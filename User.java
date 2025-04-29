public class User {
    private String username;
    private String email;
    private String password;
    private boolean isAvailable = true;
    private double rating = 2; //For now

    public User(String username, String email, String password) {
        this(username,password);
        this.email = email;
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

    public double getRating() {
        return rating;
    }

    public boolean getIsAvailable() {
        return isAvailable;
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
    public void setIsAvailable(boolean b) {
        this.isAvailable = b;
    }
}

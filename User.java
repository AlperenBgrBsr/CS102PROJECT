public class User {
    private String username;
    private String email;
    private String password;

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

    public void setEmail(String anemail) {
        this.email = anemail;
    }

    public void setUsername(String ausername) {
        this.username = ausername;
    }

    public void setPassword(String apassword) {
        this.password = apassword;
    }
}

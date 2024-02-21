package model;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public LoginRequest() {
        this.username = null;
        this.password = null;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

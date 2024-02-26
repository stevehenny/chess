package model.requests;

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

}

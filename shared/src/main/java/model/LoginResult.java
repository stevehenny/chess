package model;

public class LoginResult {
    private String authToken;
    private String username;

    public LoginResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

}

package model;

public class RegisterResult {
    private String authToken;
    private String username;
    private String message;

    public RegisterResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return username;
    }

}

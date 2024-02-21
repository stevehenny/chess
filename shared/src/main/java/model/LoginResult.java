package model;

public class LoginResult {
    private String authToken;
    private String userName;
    private String message;

    public LoginResult(String authToken, String userName,  String message) {
        this.authToken = authToken;
        this.userName = userName;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}

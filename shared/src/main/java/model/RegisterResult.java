package model;

public class RegisterResult {
    private String authToken;
    private String userName;
    private String message;

    public RegisterResult(String authToken, String userName, String message) {
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

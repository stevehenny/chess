package model;

public class LoginResult {
    private String authToken;
    private String username;
    private String message;

    public LoginResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public LoginResult(String message) {
        this.message = message;
    }
    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return username;
    }
    
    public String getMessage(){
        return "Username" + username + " " + "AuthToken: " + authToken;
    }
}

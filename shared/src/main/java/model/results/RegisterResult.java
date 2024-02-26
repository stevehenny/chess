package model.results;

public class RegisterResult {
    private String authToken;
    private String username;

    public RegisterResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }


}

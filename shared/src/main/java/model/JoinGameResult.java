package model;

public class JoinGameResult {
    private int gameID;
    private String authToken;

    private String color;

    private String errorMessage;

    public JoinGameResult(int gameID, String authToken, String color, String errorMessage) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.color = color;
        this.errorMessage = errorMessage;
    }
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}

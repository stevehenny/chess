package model;

public class CreateGameResult {
    private int gameID;
    private String authToken;


    public CreateGameResult(int gameID) {
        this.gameID = gameID;

    }

    public int getGameID() {
        return gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}

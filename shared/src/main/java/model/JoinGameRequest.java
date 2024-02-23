package model;

public class JoinGameRequest {
    private int gameID;
    private String authToken;

    private String playerColor;

    public JoinGameRequest(int gameID, String authToken, String color) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.playerColor = color;
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

    public String getColor() {
        return playerColor;
    }

}

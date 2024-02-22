package model;

public class JoinGameRequest {
    private int gameID;
    private String authToken;

    private String color;

    public JoinGameRequest(int gameID, String authToken) {
        this.gameID = gameID;
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMessage(){
        return "GameID: " + gameID + " " + "AuthToken: " + authToken;
    }
}

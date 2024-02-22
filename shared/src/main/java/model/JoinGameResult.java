package model;

public class JoinGameResult {
    private int gameID;
    private String authToken;

    private String color;

    public JoinGameResult(int gameID, String authToken, String color) {
        this.gameID = gameID;
        this.authToken = authToken;
        this.color = color;
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

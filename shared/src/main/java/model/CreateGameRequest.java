package model;

public class CreateGameRequest {
    private String gameName;
    private String authToken;

    public CreateGameRequest(String gameName, String authToken) {
        this.gameName = gameName;
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage(){
        return "GameName: " + gameName + " " + "AuthToken: " + authToken;
    }

}

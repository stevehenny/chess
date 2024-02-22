package model;
import chess.ChessGame;

public class GameData {
    private int gameID;
    private String gameName;
    private ChessGame game;
    private String whiteUsername;
    private String blackUsername;

    public GameData(int gameId, String gameName, ChessGame game, String whitePlayer, String blackPlayer) {
        this.gameID = gameId;
        this.gameName = gameName;
        this.game = game;
        this.whiteUsername = whitePlayer;
        this.blackUsername = blackPlayer;
    }

    public GameData(String gameName){
        this.gameName = gameName;
        this.gameID = 0;
        this.game = null;
        this.whiteUsername = null;
        this.blackUsername = null;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getWhitePlayer() {
        return whiteUsername;
    }

    public String getBlackPlayer() {
        return blackUsername;
    }

    public void setGameID(int gameId) {
        this.gameID = gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whiteUsername = whitePlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackUsername = blackPlayer;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "gameID=" + gameID +
                ", gameName='" + gameName + '\'' +
                ", game=" + game +
                ", whitePlayer='" + whiteUsername + '\'' +
                ", blackPlayer='" + blackUsername + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameData gameData = (GameData) o;

        if (gameID != gameData.gameID) return false;
        if (gameName != null ? !gameName.equals(gameData.gameName) : gameData.gameName != null) return false;
        if (game != null ? !game.equals(gameData.game) : gameData.game != null) return false;
        if (whiteUsername != null ? !whiteUsername.equals(gameData.whiteUsername) : gameData.whiteUsername != null) return false;
        return blackUsername != null ? blackUsername.equals(gameData.blackUsername) : gameData.blackUsername == null;
    }

    @Override
    public int hashCode() {
        int result = gameID;
        result = 91 * result + (gameName != null ? gameName.hashCode() : 0);
        result = 91 * result + (game != null ? game.hashCode() : 0);
        result = 91 * result + (whiteUsername != null ? whiteUsername.hashCode() : 0);
        result = 91 * result + (blackUsername != null ? blackUsername.hashCode() : 0);
        return result;
    }

    public void addPlayer(String username) {
        if (whiteUsername == null) {
            whiteUsername = username;
        }
        else if (blackUsername == null) {
            blackUsername = username;
        }
    }
}

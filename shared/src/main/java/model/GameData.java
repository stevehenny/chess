package model;
import chess.ChessGame;

public class GameData {
    private int gameId;
    private String gameName;
    private ChessGame game;
    private String whitePlayer;
    private String blackPlayer;

    public GameData(int gameId, String gameName, ChessGame game, String whitePlayer, String blackPlayer) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.game = game;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public GameData(String gameName){
        this.gameName = gameName;
        this.gameId = 0;
        this.game = null;
        this.whitePlayer = null;
        this.blackPlayer = null;
    }

    public int getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                ", game=" + game +
                ", whitePlayer='" + whitePlayer + '\'' +
                ", blackPlayer='" + blackPlayer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameData gameData = (GameData) o;

        if (gameId != gameData.gameId) return false;
        if (gameName != null ? !gameName.equals(gameData.gameName) : gameData.gameName != null) return false;
        if (game != null ? !game.equals(gameData.game) : gameData.game != null) return false;
        if (whitePlayer != null ? !whitePlayer.equals(gameData.whitePlayer) : gameData.whitePlayer != null) return false;
        return blackPlayer != null ? blackPlayer.equals(gameData.blackPlayer) : gameData.blackPlayer == null;
    }

    @Override
    public int hashCode() {
        int result = gameId;
        result = 91 * result + (gameName != null ? gameName.hashCode() : 0);
        result = 91 * result + (game != null ? game.hashCode() : 0);
        result = 91 * result + (whitePlayer != null ? whitePlayer.hashCode() : 0);
        result = 91 * result + (blackPlayer != null ? blackPlayer.hashCode() : 0);
        return result;
    }

    public void addPlayer(String username) {
        if (whitePlayer == null) {
            whitePlayer = username;
        }
        else if (blackPlayer == null) {
            blackPlayer = username;
        }
    }
}

package model;
import chess.ChessGame;

public class GameData {
    final int gameId;
    final String gameName;
    final ChessGame game;
    final String whitePlayer;
    final String blackPlayer;

    public GameData(int gameId, String gameName, ChessGame game, String whitePlayer, String blackPlayer) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.game = game;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
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

}

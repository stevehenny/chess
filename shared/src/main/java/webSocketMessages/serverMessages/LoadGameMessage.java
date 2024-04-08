package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private int gameId;

    private ChessGame.TeamColor playerColor;

    public LoadGameMessage(int gameId, ChessGame.TeamColor playerColor) {
        super(ServerMessageType.LOAD_GAME);
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameId;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}

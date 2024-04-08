package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private ChessGame.TeamColor playerColor;
    private int gameID;
    public JoinPlayerCommand(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }


    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}

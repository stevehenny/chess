package webSocketMessages.userCommands;

import chess.ChessMove;
public class MakeMoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.move = move;
        this.gameID = gameID;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {
        return move;
    }

    public int getGameId() {
        return gameID;
    }
}

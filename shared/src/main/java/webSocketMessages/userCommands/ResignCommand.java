package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{

    private int gameID;
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}

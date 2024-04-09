package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    private final String errorMessage;

    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage + "Error";
    }

    public String getMessage() {
        return errorMessage;
    }
}

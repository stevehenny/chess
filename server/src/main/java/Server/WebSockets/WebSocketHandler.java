package server.WebSockets;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler {
    public final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    public void WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;

    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayerCommand cmd = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(cmd, session);
                break;
            case JOIN_OBSERVER:
                joinObserver(command, session);
                break;
            case MAKE_MOVE:
                makeMove(command, session);
                break;
            case LEAVE:
                leave(command, session);
                break;
            case RESIGN:
                resign(command, session);
                break;
            default:
                throw new ResponseException("Invalid command type");
        }
    }

    public void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try {
            if (authDAO.findAuth(command.getAuthString())) {
                GameData game = gameDAO.getGame(command.getGameID());
                LoadGameMessage message = new LoadGameMessage(game.getGameID(), command.getPlayerColor());
                session.getRemote().sendString(new Gson().toJson(message));
                Notification noti = new Notification("Player joined game");
                connections.broadcast(command.getAuthString(), noti);
            }
        } catch (DataErrorException | DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }
}

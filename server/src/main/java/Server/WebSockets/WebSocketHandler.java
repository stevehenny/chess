package server.WebSockets;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
@WebSocket
public class WebSocketHandler {
    public final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayerCommand cmd = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(cmd, session);
                break;
            case JOIN_OBSERVER:
                JoinObserverCommand cmd2 = new Gson().fromJson(message, JoinObserverCommand.class);
                joinObserver(cmd2, session);
                break;
            case MAKE_MOVE:
//                makeMove(command, session);
                break;
            case LEAVE:
//                leave(command, session);
                break;
            case RESIGN:
//                resign(command, session);
                break;
            default:
//                throw new ResponseException("Invalid command type");
        }
    }

    public void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try {
            if (authDAO.findAuth(command.getAuthString()) && (gameDAO.getGame(command.getGameID()) != null)) {
                GameData game = gameDAO.getGame(command.getGameID());
                if (command.getPlayerColor() == null) {
                    Error error = new Error("Player color is null");
                    session.getRemote().sendString(new Gson().toJson(error));
                } else if ("WHITE".equals(command.getPlayerColor().toString())) {
                    if ((game.getWhitePlayer() == null) || !(authDAO.readAuth(command.getAuthString()).getUsername().equals(game.getWhitePlayer()))) {
                        Error error = new Error("Player color not specified");
                        session.getRemote().sendString(new Gson().toJson(error));
                    } else {
                        game.setWhitePlayer(command.getAuthString());
                        LoadGameMessage message = new LoadGameMessage(game.getGame());
                        session.getRemote().sendString(new Gson().toJson(message));
                        Notification notification = new Notification("Player joined game");
                        connections.broadcast(command.getAuthString(), notification);
                    }

                } else {
                    if ((game.getBlackPlayer() == null) || (authDAO.readAuth(command.getAuthString()).getUsername().equals(game.getBlackPlayer()))) {
                        Error error = new Error("Player color not specified");
                        session.getRemote().sendString(new Gson().toJson(error));
                    } else {
                        game.setBlackPlayer(command.getAuthString());
                        LoadGameMessage message = new LoadGameMessage(game.getGame());
                        session.getRemote().sendString(new Gson().toJson(message));
                        Notification notification = new Notification("Player joined game");
                        connections.broadcast(command.getAuthString(), notification);
                    }
                }

            } else {
                Error error = new Error("Player not authorized to join game");
                session.getRemote().sendString(new Gson().toJson(error));
            }


        } catch (DataErrorException | DataAccessException | IOException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }

    public void joinObserver(JoinObserverCommand command, Session session) throws IOException {

        connections.add(command.getAuthString(), session);

        try {
            if (authDAO.findAuth(command.getAuthString()) && (gameDAO.getGame(command.getGameID()) != null)) {
                GameData game = gameDAO.getGame(command.getGameID());
                LoadGameMessage message = new LoadGameMessage(game.getGame());
                session.getRemote().sendString(new Gson().toJson(message));
                Notification notification = new Notification("Observer joined game");
                connections.broadcast(command.getAuthString(), notification);
            } else {
                Error error = new Error("Observer not authorized to join game");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataAccessException | DataErrorException | IOException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }
}

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
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

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
                makeMove(command, session);
                break;
            case LEAVE:
                LeaveGameCommand cmd3 = new Gson().fromJson(message, LeaveGameCommand.class);
                leave(cmd3, session);
                break;
            case RESIGN:
                ResignCommand cmd4 = new Gson().fromJson(message, ResignCommand.class);
                resign(cmd4, session);
                break;
            default:
                throw new IOException("Invalid command type");
        }
    }



    private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try {
            if (authDAO.findAuth(command.getAuthString()) && (gameDAO.getGame(command.getGameID()) != null)) {
                GameData game = gameDAO.getGame(command.getGameID());
                if (command.getPlayerColor() == null) {
                    Error error = new Error("Player color is null");
                    session.getRemote().sendString(new Gson().toJson(error));
                } else if ("WHITE".equals(command.getPlayerColor().toString())) {
                    if ((game.getWhitePlayer() == null) || !(Objects.equals(authDAO.readAuth(command.getAuthString()).getUsername(), game.getWhitePlayer()))) {
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
                    if ((game.getBlackPlayer() == null) || !(Objects.equals(authDAO.readAuth(command.getAuthString()).getUsername(), game.getBlackPlayer()))) {
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

    private void joinObserver(JoinObserverCommand command, Session session) throws IOException {

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
    private void leave(LeaveGameCommand command, Session session) throws IOException {
        connections.remove(command.getAuthString());
        try {
            if (authDAO.findAuth(command.getAuthString())) {
                GameData game = gameDAO.getGame(command.getGameID());
                if (Objects.equals(game.getWhitePlayer(), authDAO.readAuth(command.getAuthString()).getUsername())) {
                    game.setWhitePlayer(null);
                    gameDAO.overrideGame(game);
                    Notification notification = new Notification("White Player left game");
                    session.getRemote().sendString(new Gson().toJson(notification));
                    connections.broadcast(command.getAuthString(), notification);
                    connections.remove(command.getAuthString());
                } else if (Objects.equals(game.getBlackPlayer(), authDAO.readAuth(command.getAuthString()).getUsername())) {
                    game.setBlackPlayer(null);
                    gameDAO.overrideGame(game);
                    Notification notification = new Notification("Black Player left game");
                    session.getRemote().sendString(new Gson().toJson(notification));
                    connections.broadcast(command.getAuthString(), notification);
                    connections.remove(command.getAuthString());
                }
                else{
                    Notification notification = new Notification("Player left game");
                    session.getRemote().sendString(new Gson().toJson(notification));
                    connections.broadcast(command.getAuthString(), notification);
                    connections.remove(command.getAuthString());
                }
            } else {
                Error error = new Error("Player not authorized to leave game");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException | DataAccessException | IOException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }

    private void resign(ResignCommand command, Session session) throws IOException{
        connections.add(command.getAuthString(), session);

        try{
            if(authDAO.findAuth(command.getAuthString())){
                GameData data = gameDAO.getGame(command.getGameID());
                if(Objects.equals(data.getWhitePlayer(), authDAO.readAuth(command.getAuthString()).getUsername())){
                    data.setWhitePlayer(null);
                    data.setBlackPlayer(null);
                    gameDAO.overrideGame(data);
                    Notification notification = new Notification("Player resigned");
                    session.getRemote().sendString(new Gson().toJson(notification));
                    connections.broadcast(command.getAuthString(), notification);
                } else if(Objects.equals(data.getBlackPlayer(), authDAO.readAuth(command.getAuthString()).getUsername())){
                    data.setBlackPlayer(null);
                    data.setWhitePlayer(null);
                    gameDAO.overrideGame(data);
                    Notification notification = new Notification("Player resigned");
                    session.getRemote().sendString(new Gson().toJson(notification));
                    connections.broadcast(command.getAuthString(), notification);
                } else{
                    Error error = new Error("Player not in game");
                    session.getRemote().sendString(new Gson().toJson(error));
                }
            }else{
                Error error = new Error("Player not authorized to resign");
                session.getRemote().sendString(new Gson().toJson(error));

            }
        } catch (DataAccessException | DataErrorException  | IOException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }

    private void makeMove(UserGameCommand command, Session session) {

    }

}

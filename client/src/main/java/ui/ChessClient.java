package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.*;
import server.Server;

public class ChessClient {

    private final ServerFacade server;
    private ClientState state;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        state = ClientState.SIGNED_OUT;
    }

    public String help() {
        if (state == ClientState.SIGNED_OUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        return """
                - listGames 
                - createGame <gameName>
                - joinGame <gameId> <color>
                - joinObserver <gameId>
                - logout
                - quit
                """;
    }


    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == ClientState.SIGNED_OUT) {
                return switch (cmd) {
                    case "help" -> help();
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "help" -> help();
                    case "createGame" -> createGame(params);
                    case "joinGame" -> joinGame(params);
                    case "joinObserver" -> joinObserver(params);
                    case "quit" -> "quit";
                    case "logout" -> logout();
                    case "listGames" -> listGames();
                    default -> help();
                };
            }
        } catch (Exception | ResponseException e) {
            return e.getMessage();
        }
    }


    public String register(String[] params) throws ResponseException {
        try {
            if (params.length != 3) {
                return "Usage: register <username> <password>";
            }
            var username = params[0];
            var password = params[1];
            var email = params[2];
            server.register(username, password, email);
            state = ClientState.SIGNED_IN;
            return "Registered";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String login(String[] params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Usage: login <username> <password>";
            }
            var username = params[0];
            var password = params[1];
            server.login(username, password);
            state = ClientState.SIGNED_IN;
            return "Logged in";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String logout() throws ResponseException{
        try {
            server.logout();
            state = ClientState.SIGNED_OUT;
            return "Logged out";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String createGame(String[] params) throws ResponseException{
        try {
            if (params.length != 1) {
                return "Usage: createGame <gameName>";
            }
            var gameName = params[0];
            server.createGame(gameName);
            return "Game created";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String joinGame(String[] params) {
        try {
            if (params.length != 2) {
                return "Usage: joinGame <gameId> <color>";
            }
            var gameId = params[0];
            var color = params[1];
            server.joinGame(gameId, color);
            return "Game joined";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String joinObserver(String[] params) {
        try {
            if (params.length != 1) {
                return "Usage: joinObserver <gameId>";
            }
            var gameId = params[0];
            server.joinGame(gameId, null);
            return "Observer joined";
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String listGames() {
        try {
            var games = server.listGames();
            return games.toString();
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }
}


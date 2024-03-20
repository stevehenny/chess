package ui;

import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
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
                    case "creategame" -> createGame(params);
                    case "joingame" -> joinGame(params);
                    case "joinobserver" -> joinObserver(params);
                    case "quit" -> quit();
                    case "logout" -> logout();
                    case "listgames" -> listGames();
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

    public String quit() {
        System.exit(0);
        return "Quitting\n";
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
            return "Error" + e.getMessage();
        }
    }

    public String joinObserver(String[] params) throws ResponseException {
        try {
            if (params.length != 1) {
                return "Error: bad request";
            }
            var gameIDString = params[0];
            var gameID = Integer.parseInt(gameIDString);
            server.joinObserver(gameID);
            return "Success: joined observer";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String joinGame(String[] params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Error: bad request";
            }
            var gameIDString = params[0];
            var gameID = Integer.parseInt(gameIDString);
            var playerColor = params[1];
            playerColor = playerColor.toUpperCase();
            server.joinGame(gameID, playerColor);
            return "Success: joined game";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listGames() {
        try {
            var games = server.listGames();
            var result = new StringBuilder();
            var gson = new Gson();
            for(var game : games) {
                result.append(gson.toJson(game)).append("\n");
            }
            return result.toString();
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }
}


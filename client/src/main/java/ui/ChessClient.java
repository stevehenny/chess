package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.GameDAOsql;
import exception.ResponseException;
import model.*;
import server.Server;

public class ChessClient {

    private final ServerFacade server;
    private ClientState state;

    private GameData myGame;
    private PrintBoard printBoard;

    private String playerColorGlobal;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        state = ClientState.SIGNED_OUT;
        printBoard = new PrintBoard();
    }

    public String help() {
        if (state == ClientState.SIGNED_OUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        else if (state == ClientState.SIGNED_IN) {
            return """
                    - listGames 
                    - createGame <gameName>
                    - joinGame <gameId> <color>
                    - joinObserver <gameId>
                    - logout
                    - quit
                    """;
        }
        else if (state == ClientState.GAMEPLAY) {
            return """
                    - leave
                    - redraw
                    - makeMove <from> <to>
                    - resign
                    - highlightMoves <from>
                    - move <from> <to>
                    - quit
                    """;
        }
        return "Error: bad request";
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
                    case "quit" -> quit();
                    default -> help();
                };
            } else if (state == ClientState.SIGNED_IN){
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
            else if (state == ClientState.GAMEPLAY) {
                return switch (cmd) {
                    case "help" -> help();
                    case "leave" -> leaveGame();
                    case "redraw" -> redraw();
                    case "makemove" -> makeMove(params);
                    case "resign" -> resign();
                    case "highlightmoves" -> highlightMoves(params);
                    case "move" -> move(params);
                    case "quit" -> quit();
                    default -> help();
                };
            }
        } catch (Exception | ResponseException e) {
            return e.getMessage();
        }
        return line;
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

    public String quit() throws ResponseException {
//        server.clear();
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
            var gameInd = Integer.parseInt(gameIDString);
            int index = 0;


            Collection<GameData> games = server.listGames();
            if(gameInd >= games.size() || gameInd < 0){
                return "Error: game does not exist";
            }
            for (GameData game : games) {
                if (index == gameInd) {
                    myGame = game;
                    break;
                }
                index++;
            }

            server.joinObserver(myGame.getGameID());
            printBoard.printBoards("All", myGame.getGame().getBoard());
            state = ClientState.GAMEPLAY;
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
            var gameInd = Integer.parseInt(gameIDString);
            var playerColor = params[1];
            Collection<GameData> games = server.listGames();
            if(gameInd >= games.size() || gameInd < 0){
                return "Error: game does not exist";
            }
            int index = 0;
            for (GameData game : games) {
                if (index == gameInd) {
                    myGame = game;
                    break;
                }
                index++;
            }
            playerColor = playerColor.toUpperCase();
            server.joinGame(myGame.getGameID(), playerColor);
            printBoard.printBoards("All", myGame.getGame().getBoard());
            playerColorGlobal = playerColor;
            state = ClientState.GAMEPLAY;
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
            int index = 0;
            for(var game : games) {
                result.append(index).append(gson.toJson(game)).append("\n");
                index++;
            }
            return result.toString();
        }
        catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String redraw() {
        if (playerColorGlobal.equals("WHITE")) {
            printBoard.printBoards("WHITE", myGame.getGame().getBoard());
            return "board redrawn";
    }
        else if (playerColorGlobal.equals("BLACK")) {
            printBoard.printBoards("BLACK", myGame.getGame().getBoard());
            return "board redrawn";
        }
        else{
            return "Player not in game";
        }
    }

    private String move(String[] params) {
        return "";
    }

    private String highlightMoves(String[] params) {
        return "";
    }

    private String resign() {
        return "";
    }

    private String makeMove(String[] params) {
        return "";
    }

    private String leaveGame() {
        return "";
    }
}


package services;

import dataAccess.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.*;
import dataAccess.DataErrorException;

import java.util.Collection;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class GameService {
    private UserDAO userDAO = new UserDAO();
    private AuthDAO authDAO = new AuthDAO();
    private GameDAO gameDAO = new GameDAO();

    public GameService() {
    }
    public void clear() throws DataAccessException {
        userDAO.deleteUser();
        authDAO.deleteAuth();
        gameDAO.deleteGame();
    }

    public RegisterResult register(RegisterRequest request) throws DataErrorException, DataAccessException {
        //check if username, password, or email is null
        if (request.getUserName() == null || request.getPassword() == null || request.getEmail() == null) {
            throw new DataErrorException(400, "Error: Username, password, or email is null");
        }
        //check if username, password, or email is empty
        if (request.getUserName().equals("") || request.getPassword().equals("") || request.getEmail().equals("")) {
            throw new DataErrorException(400, "Error: Username, password, or email is empty");
        }
        //check if username is already taken
        if(userDAO.findUser(request.getUserName())){
            throw new DataErrorException(403,"Error: Username already taken");
        }
        //create user
        UserData user = new UserData(request.getUserName(), request.getPassword(), request.getEmail());
        userDAO.createUser(user);

        //create auth
        AuthData auth = new AuthData();
        auth.setUsername(request.getUserName());
        auth.setAuthToken(UUID.randomUUID().toString());
        authDAO.createAuth(auth);

    //create register result
    RegisterResult registerResult = new RegisterResult(auth.getAuthToken(), request.getUserName());
    return registerResult;
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, DataErrorException {
        //check if username null or empty
        if(request.getUserName() == null || Objects.equals(request.getUserName(), "")){
            throw new DataErrorException(401,"Error: Unauthorized");
        }
        //check if password is null or empty
        if(request.getPassword() == null || Objects.equals(request.getPassword(), "")){
            throw new DataErrorException(401, "Error: Unauthorized");
        }
        //check if username exists
        if(!userDAO.findUser(request.getUserName())){
            throw new DataErrorException(401,"Error: Unknown username");
        }
        //check if password is correct
        if(!userDAO.readUser(request.getUserName()).getPassword().equals(request.getPassword())){
            throw new DataErrorException(401,"Error: Incorrect password");
        }
        //create auth
        String username = request.getUserName();
        AuthData auth = new AuthData();
        auth.setUsername(username);
        auth.setAuthToken(UUID.randomUUID().toString());
        authDAO.createAuth(auth);
        LoginResult loginResult = new LoginResult(auth.getAuthToken(), username);
        return loginResult;
    }

    public void logout(String request) throws DataErrorException, DataAccessException {
//        authDAO.findAndDeleteAuth(request.getUserName());
        if (authDAO.findAndDeleteAuth(request) == null) {
            throw new DataErrorException(401, "Error: Internal server error");
        }
    }

    public Object listGames() throws DataAccessException, DataErrorException{
        Collection<GameData> games = gameDAO.getGames();
        try {
            if (games == null) {
                throw new DataErrorException(500, "Error: Internal server error");
            }
            return games;
        }
        catch (DataErrorException e) {
            throw new DataErrorException(500, "Error: Internal server error");
        }
    }

    public CreateGameResult createGame(CreateGameRequest gameRequest) throws DataAccessException, DataErrorException {
        if (gameRequest.getGameName() == null || gameRequest.getGameName().equals("")) {
            throw new DataErrorException(400, "Error: Bad game request");
        }
        if (gameDAO.findGame(gameRequest.getGameName())) {
            throw new DataErrorException(401, "Error: Game name already taken");
        }
        if(!AuthDAO.findAuth(gameRequest.getAuthToken())){
            throw new DataErrorException(401, "Error: Unauthorized");
        }

        GameData game = new GameData(gameRequest.getGameName());
        gameDAO.createGame(game);

        int gameId = ThreadLocalRandom.current().nextInt();
        while(gameId <= 0){
            gameId = ThreadLocalRandom.current().nextInt();
        }
        game.setGameId(gameId);

        CreateGameResult createGameResult = new CreateGameResult(game.getGameId());
        return createGameResult;
    }

    public JoinGameResult joinGame(JoinGameRequest gameRequest) throws DataErrorException, DataAccessException {
        if (gameRequest.getGameID() <= 0) {
            throw new DataErrorException(400, "Error: Bad game request");
        }
        if(!AuthDAO.findAuth(gameRequest.getAuthToken())){
            throw new DataErrorException(401, "Error: Unauthorized");
        }
        GameData game = gameDAO.getGame(gameRequest.getGameID());
        if (game == null) {
            throw new DataErrorException(403, "Error: Game not found");
        }
        if (gameRequest.getColor() == null || gameRequest.getColor().equals("")){
            JoinGameResult joinGameResult = new JoinGameResult(game.getGameId(), game.getGameName(), null);
            return joinGameResult;
        }
        if (game.getWhitePlayer() != null && game.getBlackPlayer() != null) {
            throw new DataErrorException(403, "Error: Game is full");
        }

        if (gameRequest.getColor().equals("WHITE") && game.getWhitePlayer() == null){
            game.setWhitePlayer(authDAO.readAuth(gameRequest.getAuthToken()).getUsername());
            gameDAO.joinGame(game.getGameId(), game.getWhitePlayer());
            JoinGameResult joinGameResult = new JoinGameResult(game.getGameId(), game.getGameName(), "WHITE");
            return joinGameResult;
        }
        else if (gameRequest.getColor().equals("BLACK") && game.getBlackPlayer() == null){
            game.setBlackPlayer(authDAO.readAuth(gameRequest.getAuthToken()).getUsername());
            gameDAO.joinGame(game.getGameId(), game.getBlackPlayer());
            JoinGameResult joinGameResult = new JoinGameResult(game.getGameId(), game.getGameName(), "BLACK");
            return joinGameResult;
        }
        else {
            throw new DataErrorException(403, "Error: Game is full");
        }


    }

}

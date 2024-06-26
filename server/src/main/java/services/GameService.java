package services;
import dataAccess.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.*;
import model.*;
import model.results.*;
import model.requests.*;
import dataAccess.DataErrorException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {

            this.userDAO = userDAO;
            this.authDAO = authDAO;
            this.gameDAO = gameDAO;

    }
    /**
     * Clears all data from the database
     * @throws DataAccessException
     */
    public void clear() throws DataErrorException, DataAccessException {
        userDAO.deleteUser();
        authDAO.deleteAuth();
        gameDAO.deleteGame();
    }

    /**
     * Registers a new user with the given username, password, and email
     * @param request
     * @return
     * @throws DataErrorException
     * @throws DataAccessException
     */
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

    /**
     * Logs in the user with the given username and password
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public LoginResult login(LoginRequest request) throws DataAccessException, DataErrorException {
        //check if username null or empty
        if(request.getUserName() == null || request.getUserName().equals("")){
            throw new DataErrorException(401,"Error: Unauthorized login");
        }
        //check if password is null or empty
        if(request.getPassword() == null || request.getPassword().equals("")){
            throw new DataErrorException(401, "Error: Unauthorized login");
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
        String newAuth = UUID.randomUUID().toString();
        while (authDAO.findAuth(newAuth)){
            newAuth = UUID.randomUUID().toString();
        }
        auth.setAuthToken(newAuth);
        authDAO.createAuth(auth);
        LoginResult loginResult = new LoginResult(auth.getAuthToken(), username);
        return loginResult;
    }


    /**
     * Logs out the user with the given authToken
     * @param request
     * @throws DataErrorException
     * @throws DataAccessException
     */
    public void logout(String request) throws DataErrorException, DataAccessException {
        //check if user is logged in
        authDAO.findAndDeleteAuth(request);
        }


    /**
     * Lists all games
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public Collection<GameData> listGames(ListGameRequest request) throws DataAccessException, DataErrorException{
        //check if user is logged in
        if(request.getAuthToken() == null || !authDAO.findAuth(request.getAuthToken())){
            throw new DataErrorException(401, "Error: Unauthorized list game request");
        }

        Collection<GameData> games = gameDAO.listGames();
        //check if games is null
        if(games == null){
            throw new DataErrorException(401, "Error: No games found");
        }
        return games;
    }

    /**
     * Creates a new game with the given gameName
     * @param gameRequest
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public CreateGameResult createGame(CreateGameRequest gameRequest) throws DataAccessException, DataErrorException {
        //check if gameName is null or empty
        if (gameRequest.getGameName() == null || gameRequest.getGameName().equals("")) {
            throw new DataErrorException(400, "Error: Bad game request");
        }
        //check if gameName is already taken
        if (gameDAO.findGame(gameRequest.getGameName())) {
            throw new DataErrorException(401, "Error: Game name already taken");
        }
        //check if user is logged in
        if(!authDAO.findAuth(gameRequest.getAuthToken())){
            throw new DataErrorException(401, "Error: Unauthorized creation of game");
        }


        GameData game = new GameData(gameRequest.getGameName());
        int gameID = ThreadLocalRandom.current().nextInt();
        while(gameID <= 0) {
            gameID = ThreadLocalRandom.current().nextInt();
        }
        game.setGameID(gameID);
        gameDAO.createGame(game);

        CreateGameResult createGameResult = new CreateGameResult(game.getGameID());
        return createGameResult;
    }

    /**
     * Joins a game with the given gameID and color
     * @param gameRequest
     * @return
     * @throws DataErrorException
     * @throws DataAccessException
     */
    public JoinGameResult joinGame(JoinGameRequest gameRequest) throws DataErrorException, DataAccessException {
        //check if gameID is less than or equal to 0
        if (gameRequest.getGameID() <= 0) {
            throw new DataErrorException(400, "Error: Bad request to join game");
        }
        //check if user is logged in
        if(!authDAO.findAuth(gameRequest.getAuthToken())){
            throw new DataErrorException(401, "Error: Unauthorized join game");
        }
        //check if game exists
        if(gameDAO.getGame(gameRequest.getGameID())== null){
            throw new DataErrorException(401, "Error: Game does not exist");
        }
        //check if color is null or empty
        if (gameRequest.getColor() == null || gameRequest.getColor().equals("")){
            JoinGameResult joinGameResult = new JoinGameResult(gameRequest.getGameID(), gameRequest.getAuthToken(), null, null);
            return joinGameResult;
        }
        if(gameDAO.getGame(gameRequest.getGameID()).getBlackPlayer() != null && gameDAO.getGame(gameRequest.getGameID()).getWhitePlayer() != null){
            throw new DataErrorException(403, "Error: already taken");
        }

        //check if color is white and white player is null
        if (gameRequest.getColor().equals("WHITE") && gameDAO.getGame(gameRequest.getGameID()).getWhitePlayer() == null){
            GameData gameData = gameDAO.getGame(gameRequest.getGameID());
            gameData.setWhitePlayer(authDAO.readAuth(gameRequest.getAuthToken()).getUsername());
            gameDAO.joinGame(gameData);
            JoinGameResult joinGameResult = new JoinGameResult(gameRequest.getGameID(), gameRequest.getAuthToken()  , "white", null);
            return joinGameResult;
        }
        //check if color is black and black player is null
        else if (gameRequest.getColor().equals("BLACK") && gameDAO.getGame(gameRequest.getGameID()).getBlackPlayer() == null){
            GameData gameData = gameDAO.getGame(gameRequest.getGameID());
            gameData.setBlackPlayer(authDAO.readAuth(gameRequest.getAuthToken()).getUsername());
            gameDAO.joinGame(gameData);
            JoinGameResult joinGameResult = new JoinGameResult(gameRequest.getGameID(), gameRequest.getAuthToken()  , "black", null);
            return joinGameResult;
        }
        //if color is not white or black
        else {
            throw new DataErrorException(403, "Error: Game is full");
        }


    }

}

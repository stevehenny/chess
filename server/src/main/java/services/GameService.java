package services;

import dataAccess.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;
import model.LoginResult;
import model.LoginRequest;
import dataAccess.DataErrorException;
import model.GameData;
import java.util.Collection;

import java.util.Objects;
import java.util.UUID;

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
}

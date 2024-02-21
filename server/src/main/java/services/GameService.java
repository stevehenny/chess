package services;

import dataAccess.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

import java.util.Objects;

public class GameService {
    private RegisterRequest registerRequest = new RegisterRequest();
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

    public RegisterResult register(RegisterRequest request) throws DataAccessException{
        if(request.getUserName() == null || Objects.equals(request.getUserName(), "")){
            throw new IllegalArgumentException("User must have a username");
        }
        if(request.getPassword() == null || Objects.equals(request.getPassword(), "")){
            throw new IllegalArgumentException("User must have a password");
        }
        if(request.getEmail() == null || Objects.equals(request.getEmail(), "")){
            throw new IllegalArgumentException("User must have an email");
        }
        if(userDAO.findUser(request.getUserName())){
            throw new IllegalArgumentException("Username already taken");
        }
        UserData user = new UserData(request.getUserName(), request.getPassword(), request.getEmail());
        userDAO.createUser(user);

        AuthData auth = new AuthData();
        auth.setUsername(request.getUserName());
        authDAO.createAuth(auth);


    RegisterResult registerResult = new RegisterResult(request.getUserName(), auth.getAuthToken(), "DEFAULT");
    return registerResult;
    }
}

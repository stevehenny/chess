package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;

public class UserDAOsql implements UserDAO{

    public UserDAOsql() throws DataAccessException{
        configureDatabase();
    }
    public void createUser(UserData user) throws DataAccessException {
    }
    public UserData readUser(String username) throws DataAccessException{
        return null;
    }

    public void deleteUser() throws DataAccessException {
    }

    public boolean findUser(String username) throws DataAccessException {
        return false;
    }

}

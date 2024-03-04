package dataAccess;
import java.util.*;
import model.UserData;
public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;
    public UserData readUser(String username) throws DataAccessException;

    public void deleteUser() throws DataAccessException;

    public boolean findUser(String username) throws DataAccessException;

}

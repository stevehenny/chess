package dataAccess;
import java.util.*;
import model.UserData;
public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException, DataErrorException;
    public UserData readUser(String username) throws DataAccessException, DataErrorException;

    public void deleteUser() throws DataAccessException, DataErrorException;

    public boolean findUser(String username) throws DataAccessException, DataErrorException;

}

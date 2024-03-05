package dataAccess;
import java.util.*;
import model.AuthData;
public interface AuthDAO {

    public boolean findAuth(String authToken) throws DataAccessException, DataErrorException;



    public void createAuth(AuthData auth) throws DataAccessException, DataErrorException;

    public AuthData readAuth(String authToken) throws DataAccessException, DataErrorException;

    public AuthData findAndDeleteAuth(String authToken) throws DataAccessException, DataErrorException;

    public void deleteAuth() throws DataAccessException, DataErrorException;
}

package dataAccess;
import java.util.*;
import model.AuthData;
public interface AuthDAO {

    public boolean findAuth(String authToken) throws DataAccessException;



    public void createAuth(AuthData auth) throws DataAccessException ;

    public AuthData readAuth(String authToken) throws DataAccessException;

    public AuthData findAndDeleteAuth(String authToken) throws DataAccessException;

    public void deleteAuth() throws DataAccessException;
}

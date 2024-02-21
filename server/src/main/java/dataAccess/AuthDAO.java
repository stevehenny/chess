package dataAccess;
import java.util.*;
import model.AuthData;
public class AuthDAO {
    private Collection<AuthData> authData;
    public AuthDAO() {
        authData = new ArrayList<AuthData>();
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        authData.add(auth);
    }

    public AuthData readAuth(String username) throws DataAccessException{
        return null;
    }

    public void updateAuth(AuthData auth) throws DataAccessException{
    }

    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }
}

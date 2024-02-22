package dataAccess;
import java.util.*;
import model.AuthData;
public class AuthDAO {
    private static Collection<AuthData> authData;
    public AuthDAO() {
        authData = new ArrayList<AuthData>();
    }

    public boolean findAuth(String authToken) {
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        authData.add(auth);
    }

    public AuthData readAuth(String authToken) throws DataAccessException{
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void updateAuth(AuthData auth) throws DataAccessException{
    }
    public AuthData findAndDeleteAuth(String authToken) throws DataAccessException{
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                authData.remove(auth);
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }
}

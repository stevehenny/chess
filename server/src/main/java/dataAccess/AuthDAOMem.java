package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class AuthDAOMem implements AuthDAO{

    private static Collection<AuthData> authData;
    public AuthDAOMem() {
        authData = new ArrayList<AuthData>();
    }

    @Override
    public boolean findAuth(String authToken) {
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        authData.add(auth);
    }

    @Override
    public AuthData readAuth(String authToken) throws DataAccessException{
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AuthData findAndDeleteAuth(String authToken) throws DataAccessException{
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                authData.remove(auth);
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }
}



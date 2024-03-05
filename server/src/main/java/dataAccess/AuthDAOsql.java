package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;

public class AuthDAOsql implements  AuthDAO{

    private static Collection<AuthData> authData;
    public AuthDAOsql() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public boolean findAuth(String authToken) throws DataAccessException{
        var statement = "SELECT * FROM Auth WHERE authToken = ?";
        try {
            var conn = DatabaseManager.getConnection();
            var stmt = conn.prepareStatement(statement);
            stmt.setString(1, authToken);
            var rs = stmt.executeQuery();
            return rs.next();
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while finding AuthData: " + statement);
        }
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO Auth (authToken, username) VALUES (?, ?)";
        var authToken = auth.getAuthToken();
        var username = auth.getUsername();
        int result = executeStatement(statement, authToken, username);
        if (result != 1) {
            throw new DataAccessException("Failed to insert AuthData");
        }
    }

    private int executeStatement(String statement, String authToken, String username) throws DataAccessException {
        try{
            var conn = DatabaseManager.getConnection();
            var stmt = conn.prepareStatement(statement);
            if(authToken != null) {
                stmt.setString(1, authToken);
            }
            if(username != null) {
                stmt.setString(2, username);
            }
            return stmt.executeUpdate();
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while executing SQL statement: " + e.getMessage());
        }
    }

    public AuthData readAuth(String authToken) throws DataAccessException{
        var statement = "SELECT * FROM Auth WHERE authToken = ?";
        try{
            var conn = DatabaseManager.getConnection();
            var stmt = conn.prepareStatement(statement);
            stmt.setString(1, authToken);
            var rs = stmt.executeQuery();
            if(rs.next()){
                var auth = rs.getString("authToken");
                var username = rs.getString("username");
                AuthData authData = new AuthData();
                authData.setAuthToken(auth);
                authData.setUsername(username);
                return authData;
            }
            return null;
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while reading AuthData: " + statement);
        }
    }

    public AuthData findAndDeleteAuth(String authToken) throws DataAccessException{
        var statement = "SELECT FROM auth WHERE authToken = ?";
        var result = executeStatement(statement, authToken, null);
        if (result != 1) {
            throw new DataAccessException("Failed to find AuthData");
        }
        return null;
    }

    public void deleteAuth() throws DataAccessException {
        var statement = "DELETE FROM Auth";
        int result = executeStatement(statement, null, null);
        if (result != 1) {
            throw new DataAccessException("Failed to delete AuthData");
        }
    }
}
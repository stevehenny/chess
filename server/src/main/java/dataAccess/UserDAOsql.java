package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;

public class UserDAOsql implements UserDAO{

    public UserDAOsql() throws DataErrorException{
        configureDatabase();
    }
    public void createUser(UserData user) throws DataErrorException {
        var statement = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        var username = user.getUsername();
        var password = user.getPassword();
        var email = user.getEmail();
        int result = executeStatement(statement, username, password, email);
        if (result != 1) {
            throw new DataErrorException(500, "Failed to insert UserData" + statement);
        }
    }

    private int executeStatement(String statement, String username, String password, Object email) throws DataErrorException {
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(username != null) {
                stmt.setString(1, username);
            }
            if(password != null) {
                stmt.setString(2, password);
            }
            if(email != null) {
                stmt.setString(3, (String) email);
            }
            return stmt.executeUpdate();
        }
        catch(Exception e){
        throw new DataErrorException(500, "Error encountered while executing SQL statement: " + statement);
        }
    }

    public UserData readUser(String username) throws DataErrorException{
        var statement = "SELECT * FROM Users WHERE username = ?";
        try{
            var conn = DatabaseManager.getConnection();
            var stmt = conn.prepareStatement(statement);
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if(rs.next()){
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
            return null;
        }
        catch(Exception e){
            throw new DataErrorException(500,"Error encountered while reading UserData: " + statement);
        }
    }

    public void deleteUser() throws DataErrorException {
        var statement = "TRUNCATE TABLE Users";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while deleting UserData: " + statement);
        }

    }

    public boolean findUser(String username) throws DataErrorException {
        var statement = "SELECT * FROM Users WHERE username = ?";
        try {
            var conn = DatabaseManager.getConnection();
            var stmt = conn.prepareStatement(statement);
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            return rs.next();
        }
        catch(Exception e){
            throw new DataErrorException(500,"Error encountered while finding UserData: " + statement);
        }
    }

}

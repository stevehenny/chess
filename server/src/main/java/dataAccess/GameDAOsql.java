package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;

public class GameDAOsql implements GameDAO{
    public GameDAOsql() throws DataAccessException{
        configureDatabase();
    }

    public void createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, gameName, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?, ?)";
        var gameID = game.getGameID();
        var gameName = game.getGameName();
        var whiteUsername = game.getWhitePlayer();
        var blackUsername = game.getBlackPlayer();
        var gameData = game.getGame();
        int result = executeStatement(statement, gameID, gameName, whiteUsername, blackUsername, gameData);
        if (result != 1) {
            throw new DataAccessException("Failed to insert GameData");
        }
    }

    private int executeStatement(String statement, int gameID, String gameName, String whiteUsername, String blackUsername, Object gameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(gameID != 0) {
                stmt.setInt(1, gameID);
            }
            if(gameName != null) {
                stmt.setString(2, gameName);
            }
            if(whiteUsername != null) {
                stmt.setString(3, whiteUsername);
            }
            if(blackUsername != null) {
                stmt.setString(4, blackUsername);
            }
            if(gameData != null) {
                stmt.setObject(5, gameData);
            }
            return stmt.executeUpdate();
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while executing SQL statement: " + statement);
        }
    }

    public void deleteGame() throws DataAccessException {
        var statement = "DELETE FROM games";
        int result = executeStatement(statement, 0, null, null, null, null);
        if (result < 0) {
            throw new DataAccessException("Failed to delete GameData");
        }
    }

    public boolean findGame(String gameName) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameName = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, gameName);
            var rs = stmt.executeQuery();
            return rs.next();
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while finding GameData: " + statement);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setInt(1, gameID);
            var rs = stmt.executeQuery();
            if(rs.next()){
                var game = rs.getString("gameName");
                var whitePlayer = rs.getString("whiteUsername");
                var blackPlayer = rs.getString("blackUsername");
                GameData gameData = new GameData(game);
                gameData.setGameID(gameID);
                gameData.setWhitePlayer(whitePlayer);
                gameData.setBlackPlayer(blackPlayer);
                return gameData;
            }
            return null;
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while finding GameData: " + statement);
        }
    }
    public Collection<GameData> listGames() throws DataAccessException {
        var statement = "SELECT * FROM games";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var rs = stmt.executeQuery();
            Collection<GameData> games = new ArrayList<GameData>();
            while(rs.next()){
                var gameID = rs.getInt("gameID");
                var game = rs.getString("gameName");
                var whitePlayer = rs.getString("whiteUsername");
                var blackPlayer = rs.getString("blackUsername");
                GameData gameData = new GameData(game);
                gameData.setGameID(gameID);
                gameData.setWhitePlayer(whitePlayer);
                gameData.setBlackPlayer(blackPlayer);
                games.add(gameData);
            }
            return games;
        }
        catch(Exception e){
            throw new DataAccessException("Error encountered while listing GameData: " + statement);
        }
    }
}

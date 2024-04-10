package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;
import static java.sql.Types.NULL;

public class GameDAOsql implements GameDAO{
    public GameDAOsql() throws DataErrorException{
        configureDatabase();
    }

    @Override
    public void createGame(GameData game) throws DataErrorException {
        var statement = "INSERT INTO Games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        var gameID = game.getGameID();
        var gameName = game.getGameName();
        var whiteUsername = game.getWhitePlayer();
        var blackUsername = game.getBlackPlayer();
        ChessGame chessGame = game.getGame();
        Gson chess = new Gson();
        String gameData = chess.toJson(chessGame);
        executeStatement(statement, gameID, gameName, whiteUsername, blackUsername, gameData);

    }

    private void executeStatement(String statement, int gameID, String gameName, String whiteUsername, String blackUsername, String gameData) throws DataErrorException {
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(gameID >= 0) {
                stmt.setInt(1, gameID);
            }
            if(gameName != null) {
                stmt.setString(4, gameName);
            }
            if(whiteUsername != null) {
                stmt.setString(2, whiteUsername);
            }
            else stmt.setNull(2, NULL);
            if(blackUsername != null) {
                stmt.setString(3, blackUsername);
            }
            else stmt.setNull(3, NULL);
            if(gameData != null) {
                stmt.setString(5, gameData);
            }
            else stmt.setNull(5, NULL);

            stmt.executeUpdate();
        }
        catch(Exception e){
            throw new DataErrorException(500,"Error encountered while executing SQL statement: " + statement);
        }
    }

    @Override
    public void deleteGame() throws DataAccessException, DataErrorException {
        var statement = "TRUNCATE TABLE Games";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while deleting GameData: " + statement);
        }


    }

    @Override
    public boolean findGame(String gameName) throws DataErrorException {
        var statement = "SELECT * FROM Games WHERE gameName = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, gameName);
            var rs = stmt.executeQuery();
            return rs.next();
        }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while finding GameData: " + statement);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataErrorException {
        var statement = "SELECT * FROM Games WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setInt(1, gameID);
            var rs = stmt.executeQuery();
            if(rs.next()){
                var whitePlayer = rs.getString("whiteUsername");
                var blackPlayer = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var game = rs.getString("game");
                Gson gson = new Gson();
                ChessGame chessGame = gson.fromJson(game, ChessGame.class);
                return new GameData(gameID,gameName, chessGame, whitePlayer, blackPlayer);
            }

        }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while finding GameData: " + statement);
        }
        return null;
    }
    @Override
    public Collection<GameData> listGames() throws DataErrorException {
        var statement = "SELECT * FROM Games";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var rs = stmt.executeQuery();
            Collection<GameData> games = new ArrayList<GameData>();
            while(rs.next()){
                var gameID = rs.getInt("gameID");
                var game = rs.getString("game");
                var whitePlayer = rs.getString("whiteUsername");
                var blackPlayer = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                Gson gson = new Gson();
                ChessGame chessGame = gson.fromJson(game, ChessGame.class);
                games.add(new GameData(gameID,gameName, chessGame, whitePlayer, blackPlayer));
            }
            return games;
        }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while listing GameData: " + statement);
        }
    }
    @Override
    public void overrideGame(GameData game) throws DataErrorException {
        var stmt = "UPDATE Games SET gameName = ?, whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        var gameID = game.getGameID();
        var gameName = game.getGameName();
        var whiteUsername = game.getWhitePlayer();
        var blackUsername = game.getBlackPlayer();
        ChessGame chessGame = game.getGame();
        Gson chess = new Gson();
        String gameData = chess.toJson(chessGame);
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(stmt)){
            if(gameID >= 0) {
                statement.setInt(5, gameID);
            }
            if(gameName != null) {
                statement.setString(1, gameName);
            }
            if(whiteUsername != null) {
                statement.setString(2, whiteUsername);
            }
            else statement.setNull(2, NULL);
            if(blackUsername != null) {
                statement.setString(3, blackUsername);
            }
            else statement.setNull(3, NULL);
            if(gameData != null) {
                statement.setString(4, gameData);
            }
            else statement.setNull(4, NULL);

            statement.executeUpdate();
    }
        catch(Exception e){
            throw new DataErrorException(500, "Error encountered while overriding GameData: " + e.getMessage());
        }
    }
    @Override
    public void joinGame(GameData game) throws DataAccessException, DataErrorException {
        var statement = "UPDATE Games SET blackUsername = ?, whiteUsername = ? WHERE gameID = ?";
        var gameID = game.getGameID();

        if (getGame(gameID) == null) {
            throw new DataErrorException(401, "Error: Game does not exist");
        }

        var whiteUsername = game.getWhitePlayer();
        var blackUsername = game.getBlackPlayer();

        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)) {
            stmt.setString(1, blackUsername);
            stmt.setString(2, whiteUsername);
            stmt.setInt(3, gameID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error encountered while joining GameData: " + statement);
        }
    }

}

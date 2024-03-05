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
    }

    public void deleteGame() throws DataAccessException {
    }

    public boolean findGame(String gameName) throws DataAccessException {
        return false;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }
}

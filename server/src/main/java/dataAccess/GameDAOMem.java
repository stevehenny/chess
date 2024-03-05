package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameDAOMem implements GameDAO{

    private static Collection<GameData> gameData;
    public GameDAOMem() {
        gameData = new ArrayList<GameData>();
    }
    @Override
    public void createGame(GameData game) throws DataAccessException {
        gameData.add(game);
    }

    @Override
    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }

    @Override
    public boolean findGame(String gameName) throws DataAccessException {
        for (GameData game : gameData) {
            if (game.getGameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : gameData) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameData;
    }
}

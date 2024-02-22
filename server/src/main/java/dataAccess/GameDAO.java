package dataAccess;
import java.util.*;
import model.GameData;
public class GameDAO {
    private static Collection<GameData> gameData;
    public GameDAO() {
        gameData = new ArrayList<GameData>();
    }
    public void createGame(GameData game) throws DataAccessException {
        gameData.add(game);
    }

    public static void joinGame(int gameId, String username) throws DataAccessException{
        for (GameData game : gameData) {
            if (game.getGameID() == gameId) {
                game.addPlayer(username);
            }
        }
    }

    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }

    public Collection<GameData> getGames() throws DataAccessException {
        return gameData;
    }

    public boolean findGame(String gameName) throws DataAccessException {
        for (GameData game : gameData) {
            if (game.getGameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : gameData) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return gameData;
    }
}

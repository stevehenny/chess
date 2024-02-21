package dataAccess;
import java.util.*;
import model.GameData;
public class GameDAO {
    private Collection<GameData> gameData;
    public GameDAO() {
        gameData = new ArrayList<GameData>();
    }
    public void createGame(GameData game) throws DataAccessException {
        gameData.add(game);
    }
    public GameData readGame(int gameId) throws DataAccessException{
        return null;
    }
    public void joinGame(int gameId, String username) throws DataAccessException{
    }

    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }

    public Collection<GameData> getGames() throws DataAccessException {
        return gameData;
    }
}

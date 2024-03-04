package dataAccess;
import java.util.*;
import model.GameData;
public interface GameDAO {

    public void createGame(GameData game) throws DataAccessException;

    public void deleteGame() throws DataAccessException;

    public boolean findGame(String gameName) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
}

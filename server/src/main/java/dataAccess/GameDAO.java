package dataAccess;
import java.util.*;
import model.GameData;
public interface GameDAO {

    public void createGame(GameData game) throws DataAccessException, DataErrorException;

    public void deleteGame() throws DataAccessException, DataErrorException;

    public boolean findGame(String gameName) throws DataAccessException, DataErrorException;

    public GameData getGame(int gameID) throws DataAccessException, DataErrorException;
    public Collection<GameData> listGames() throws DataAccessException, DataErrorException;
}

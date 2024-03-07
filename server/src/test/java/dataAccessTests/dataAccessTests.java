package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class dataAccessTests {

    static final GameDAOsql gameDAO;
    static final UserDAOsql userDAO;
    static final AuthDAOsql authDAO;

    static {
        try {
            gameDAO = new GameDAOsql();
            userDAO = new UserDAOsql();
            authDAO = new AuthDAOsql();

        } catch (DataErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clearDatabase() throws Exception{
        gameDAO.deleteGame();
        userDAO.deleteUser();
        authDAO.deleteAuth();
    }


    @Test
    public void testCreateUserPositive() throws Exception {
        userDAO.createUser(new UserData("username", "password", "email"));
        assertNotNull(userDAO.readUser("username"));
    }

    @Test
    public void testCreateUserNegative() throws Exception {
        userDAO.createUser(new UserData("username", "password", "email"));
        assertThrows(DataErrorException.class, () -> userDAO.createUser(new UserData("username", "password", "email")));
    }

    @Test
    public void testReadUserPositive() throws Exception {
        userDAO.createUser(new UserData("username", "password", "email"));
        assertNotNull(userDAO.readUser("username"));
    }

    @Test
    public void testReadUserNegative() throws Exception {
        assertNull(userDAO.readUser("stephen"));
    }

    @Test
    public void testDeleteUserPositive() throws Exception {
        userDAO.createUser(new UserData("username", "password", "email"));
        userDAO.deleteUser();
        assertNull(userDAO.readUser("username"));
    }

    @Test
    public void testFindUserPositive() throws Exception {
        userDAO.createUser(new UserData("username", "password", "email"));
        assertTrue(userDAO.findUser("username"));
    }

    @Test
    public void testFindUserNegative() throws Exception {
        assertFalse(userDAO.findUser("username"));
    }

    @Test
    public void testCreateAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);

        assertNotNull(authDAO.readAuth("authToken"));
    }

    @Test
    public void testCreateAuthNegative() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);
        assertThrows(Exception.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void testGetAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);
        assertNotNull(authDAO.readAuth("authToken"));
    }

    @Test
    public void testGetAuthNegative() throws Exception {
        assertNull(authDAO.readAuth("authToken"));
    }

    @Test
    public void testDeleteAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);
        authDAO.deleteAuth();
        assertNull(authDAO.readAuth("authToken"));
    }


    @Test
    public void testDeleteMyAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        authDAO.findAndDeleteAuth("auth");
        assertNull(authDAO.readAuth("auth"));
    }

    @Test
    public void testDeleteMyAuthNegative() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);
        assertThrows(Exception.class, () -> authDAO.findAndDeleteAuth("badAuth"));
    }

    @Test
    public void testFindAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("username");
        auth.setAuthToken("authToken");
        authDAO.createAuth(auth);
        assertTrue(authDAO.findAuth("authToken"));
    }

    @Test
    public void testFindAuthNegative() throws Exception {
        assertFalse(authDAO.findAuth("authToken"));
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);
        assertNotNull(gameDAO.listGames());

    }

    @Test
    public void testCreateGameNegative() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);
        assertThrows(Exception.class, () -> gameDAO.createGame(game));
    }

    @Test
    public void testListGamesPositive() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);
        assertNotNull(gameDAO.listGames());
    }

    @Test
    public void testListGamesNegative() throws Exception {
        assertTrue(gameDAO.listGames().isEmpty());
    }


    @Test
    public void testDeleteGamePositive() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);
        gameDAO.deleteGame();
        assertTrue(gameDAO.listGames().isEmpty());
    }


    @Test
    public void testJoinGamePositive() throws Exception {
        GameData game = new GameData(1, "gameName", null, "white", "black");
        gameDAO.createGame(game);
        gameDAO.joinGame(game);
        assertNotNull(gameDAO.listGames());
    }

    @Test
    public void testJoinGameNegative() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);
        game.setGameID(-1);
        assertThrows(Exception.class, () -> gameDAO.joinGame(game));
    }

    @Test
    public void testFindGamePositive() throws Exception {
        GameData game = new GameData("gameName");
        gameDAO.createGame(game);

        assertTrue(gameDAO.findGame("gameName"));
    }

    @Test
    public void testFindGameNegative() throws Exception {
        assertFalse(gameDAO.findGame("gameName"));
    }

    @Test
    public void testGetGamePositive() throws Exception {
        GameData game = new GameData(100, "gameName", null, "white", "black");
        gameDAO.createGame(game);

        assertNotNull(gameDAO.getGame(100));
    }

    @Test
    public void testGetGameNegative() throws Exception {
        assertNull(gameDAO.getGame(100));
    }


}

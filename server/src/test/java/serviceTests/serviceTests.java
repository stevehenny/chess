package serviceTests;
import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GameService;
import model.*;
import model.requests.*;
import model.results.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class serviceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    static GameService gameService = null;


    public serviceTests() {
        userDAO = new UserDAOMem();
        authDAO = new AuthDAOMem();
        gameDAO = new GameDAOMem();
        gameService = new GameService(userDAO, authDAO, gameDAO);
    }

    @BeforeEach
    public void clearService() throws DataAccessException, DataErrorException {
        gameService.clear();
    }
    @Test
    public void testRegisterPositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        assertNotNull(gameService.register(registerRequest).getAuthToken());

    }
    @Test
    public void testRegisterNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        RegisterRequest registerRequest2 = new RegisterRequest("username", "password", "email");
        try {
            gameService.register(registerRequest2);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: Username already taken"));
        }
    }

    @Test
    public void testLoginPositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        assertNotNull(gameService.login(loginRequest).getAuthToken());
    }

    @Test
    public void testLoginNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        gameService.login(loginRequest);
        LoginRequest loginRequest2 = new LoginRequest("username2", "password");
        try {
            gameService.login(loginRequest2);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: Unknown username"));
        }
    }

    @Test
    public void testLogoutPositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        gameService.logout(authToken);
        authToken = gameService.login(loginRequest).getAuthToken();
        assertNotNull(authToken);
    }

    @Test void testLogoutNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        gameService.logout(authToken);
        try {
            gameService.logout(authToken);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: AuthData not found"));
        }
    }

    @Test
    public void testCreateGamePositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        CreateGameRequest gameRequest = new CreateGameRequest("gameName", authToken);
        assert(gameService.createGame(gameRequest).getGameID() >= 0);
    }

    @Test
    public void testCreateGameNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        CreateGameRequest gameRequest = new CreateGameRequest("gameName", authToken);
        gameService.createGame(gameRequest);
        try {
            gameService.createGame(gameRequest);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: Game name already taken"));
        }
    }

    @Test
    public void testListGamesPositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        CreateGameRequest gameRequest = new CreateGameRequest("gameName", authToken);
        gameService.createGame(gameRequest);
        ListGameRequest listGameRequest = new ListGameRequest(authToken);
        List<GameData> games = (List<GameData>) gameService.listGames(listGameRequest);
        assertFalse(games.isEmpty());
    }

    @Test
    public void testListGamesNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        ListGameRequest listGameRequest = new ListGameRequest(authToken);
        try {
            gameService.listGames(listGameRequest);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: No games found"));
        }
    }

    @Test
    public void testJoinGamePositive() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        CreateGameRequest gameRequest = new CreateGameRequest("gameName", authToken);
        int gameID = gameService.createGame(gameRequest).getGameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, authToken, "BLACK");
        assertNotNull(gameService.joinGame(joinGameRequest).getAuthToken());

    }

    @Test
    public void testJoinGameNegative() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        String authToken = gameService.login(loginRequest).getAuthToken();
        CreateGameRequest gameRequest = new CreateGameRequest("gameName", authToken);
        int gameID = gameService.createGame(gameRequest).getGameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, authToken, "BLACK");
        gameService.joinGame(joinGameRequest);
        try {
            gameService.joinGame(joinGameRequest);
        } catch (DataErrorException e) {
            assertTrue(e.getMessage().contains("Error: Game is full"));
        }
    }

    @Test
    public void testClear() throws DataAccessException, DataErrorException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        gameService.register(registerRequest);
        gameService.clear();
        RegisterRequest registerRequest2 = new RegisterRequest("username", "password", "email");
        assertNotNull(gameService.register(registerRequest2).getAuthToken());
    }
}

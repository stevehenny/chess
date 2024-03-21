package clientTests;

import dataAccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterPositive() throws ResponseException {
       Assertions.assertDoesNotThrow(() -> serverFacade.register("user1", "password", "email"));

    }

    @Test
    public void testRegisterNegative() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register("user1", "password", ""));
    }

    @Test
    public void testLoginPositive() throws ResponseException {
        serverFacade.register("user2", "password", "email");
        Assertions.assertDoesNotThrow(() -> serverFacade.login("user2", "password"));
    }

    @Test
    public void testLoginNegative() throws ResponseException {
        serverFacade.register("user3", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("user3", "password1"));
    }

    @Test
    public void testLogoutPositive() throws ResponseException {
        serverFacade.register("user4", "password", "email");
        serverFacade.login("user4", "password");
        Assertions.assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    public void testLogoutNegative() throws ResponseException {
        serverFacade.register("user5", "password", "email");
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
    }

    @Test
    public void testCreateGamePositive() throws ResponseException {
        serverFacade.register("user6", "password", "email");
        serverFacade.login("user6", "password");
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game1"));
    }

    @Test
    public void testCreateGameNegative() throws ResponseException {
        serverFacade.register("user7", "password", "email");
        serverFacade.login("user7", "password");
        serverFacade.createGame("game2");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame("game2"));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        serverFacade.register("user8", "password", "email");
        serverFacade.login("user8", "password");
        serverFacade.createGame("game3");
        Assertions.assertNotNull(serverFacade.listGames());
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        serverFacade.register("user9", "password", "email");
        serverFacade.login("user9", "password");
        serverFacade.createGame("game4");
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames());
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        serverFacade.register("user10", "password", "email");
        serverFacade.login("user10", "password");
        serverFacade.createGame("game5");
        Collection<GameData> games = serverFacade.listGames();
        if(games.contains("game5")){
            for(GameData game : games){
                if(game.getGameName().equals("game5")){
                    Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(game.getGameID(), "white"));
                }
            }
        }


    }
    @Test
    public void joinGameNegative() throws ResponseException {
        serverFacade.register("user10", "password", "email");
        serverFacade.login("user10", "password");
        serverFacade.createGame("game5");
        serverFacade.logout();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(1, "white"));
    }

}

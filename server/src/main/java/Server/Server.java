package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import model.requests.*;
import model.results.*;
import spark.*;
import services.GameService;
import dataAccess.DataErrorException;
import com.google.gson.JsonObject;

public class Server {

    private final GameService gameService;

    public Server() {
        this.gameService =  new GameService();
    }

    /**
     * Starts the server
     * @param desiredPort the port to start the server on
     * @return the port the server is running on
     */
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Stops the server
     */
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Clears all data from the database
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     */
    public Object clear(Request request, Response response) throws DataErrorException, DataAccessException {
        gameService.clear();
        response.status(200);
        return "";
    }

    /**
     * Registers a new user with the given username, password, and email
     * @param request
     * @param response
     * @return
     * @throws DataErrorException
     * @throws DataAccessException
     */
    public Object register(Request request, Response response) throws DataErrorException, DataAccessException{
        try {
            var req = new Gson().fromJson(request.body(), RegisterRequest.class);
            RegisterResult res = gameService.register(req);
            return new Gson().toJson(res);
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 400) {
                response.status(400);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Username, password, or email is null or empty");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else if(e.getErrorCode() == 403) {
                response.status(403);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Already taken username");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Server error");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }

        }
    }

    /**
     * Logs in a user with the given username and password
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public Object login(Request request, Response response) throws DataAccessException, DataErrorException{
        try {
            var req = new Gson().fromJson(request.body(), LoginRequest.class);
            LoginResult login = gameService.login(req);
            return new Gson().toJson(login);
        }
        catch (DataErrorException e){
            if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized login");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Internal server error");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
        }
    }

    /**
     * Logs out the user with the given authToken
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     */
    public Object logout (Request request, Response response) throws DataAccessException {
        try {
            gameService.logout(request.headers("Authorization"));
            return "";
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized logout");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }

            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Internal server error: logout failed");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
        }

    }

    /**
     * Creates a new game with the given authToken
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public Object createGame(Request request, Response response) throws DataAccessException, DataErrorException{
        try {
            String authToken = request.headers("Authorization");

            var req = new Gson().fromJson(request.body(), CreateGameRequest.class);
            req.setAuthToken(authToken);
            CreateGameResult createGame = gameService.createGame(req);
            return new Gson().toJson(createGame);
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 400) {
                response.status(400);
                JsonObject error = new JsonObject();
                error.addProperty("error", "bad request");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized creation of game");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Internal game server error");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
        }
    }

    /**
     * Joins a game with the given authToken
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object joinGame(Request request, Response response) {
        try{
            String authToken = request.headers("Authorization");
            var req = new Gson().fromJson(request.body(), JoinGameRequest.class);
            req.setAuthToken(authToken);
            JoinGameResult joinGame = gameService.joinGame(req);
            return new Gson().toJson(joinGame);
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 400) {
                response.status(400);
                JsonObject error = new JsonObject();
                error.addProperty("error", "bad request to join game");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized join game");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);

            }
            else if (e.getErrorCode() == 403) {
                response.status(403);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Game is full");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Internal server error: cannot join game");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lists all games
     * @param request
     * @param response
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object listGames(Request request, Response response)throws DataAccessException, DataErrorException{
        try {
            String authToken = request.headers("Authorization");
            ListGameRequest listGamesRequest = new ListGameRequest(authToken);
            ListGameResult result = new ListGameResult(gameService.listGames(listGamesRequest));
            return new Gson().toJson(result);
        }
        catch(DataErrorException e){
            if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized list games request");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
            else {
                response.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Internal server error: can't list games");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }
        }
    }
}

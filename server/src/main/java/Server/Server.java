package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import org.eclipse.jetty.util.log.Log;
import spark.*;
import services.GameService;
import com.google.gson.Gson;
import dataAccess.DataErrorException;
import com.google.gson.JsonObject;


import javax.xml.crypto.Data;

public class Server {

    private final GameService gameService;

    public Server() {
        this.gameService =  new GameService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object clear(Request request, Response response) throws DataAccessException {
        gameService.clear();
        response.status(200);
        return "";
    }

    public Object register(Request request, Response response) throws DataErrorException, DataAccessException{
        // Register a new user
        // If the request is invalid, return a 400, 403, or 500 response
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
                error.addProperty("error", "Internal server error");
                error.addProperty("message", e.getMessage());
                return new Gson().toJson(error);
            }

        }
    }

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
                error.addProperty("error", "Unauthorized");
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

    public Object logout (Request request, Response response) throws DataAccessException {
        try {

            gameService.logout(AuthData.getUsername());
            return "";
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized");
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

    public Object createGame(Request request, Response response) throws DataAccessException, DataErrorException{
        try {
            var req = new Gson().fromJson(request.body(), CreateGameRequest.class);
            CreateGameResult createGame = gameService.createGame(req);
            return new Gson().toJson(createGame);
        }
        catch (DataErrorException e) {
            if (e.getErrorCode() == 401) {
                response.status(401);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Unauthorized");
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


}

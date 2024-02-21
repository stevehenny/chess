package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import org.eclipse.jetty.util.log.Log;
import spark.*;
import services.GameService;
import com.google.gson.Gson;

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

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object clear(Request request, Response response) throws DataAccessException {
        gameService.clear();
        response.status(204);
        return "";
    }

    public Object register(Request request, Response response) throws DataAccessException{
        RegisterRequest req = new Gson().fromJson(request.body(), RegisterRequest.class);
        RegisterResult res = gameService.register(req);
        return new Gson().toJson(res);
    }

    public Object login(Request request, Response response) throws DataAccessException{
        var req = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginResult login = gameService.login(req);
        return new Gson().toJson(login);
    }


}

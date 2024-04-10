package ui;
import exception.ResponseException;
import model.GameData;
import model.requests.*;
import model.results.*;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Objects;

public class ServerFacade {
    private String serverUrl;
    static String authToken = null;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void login(String username, String password) throws ResponseException {
        var request = new LoginRequest(username, password);
        LoginResult result = this.makeRequest("POST", "/session", request, LoginResult.class);
        authToken = result.getAuthToken();
    }

    public void logout() throws ResponseException {
        this.makeRequest("DELETE", "/session", null, null);
        authToken = null;
    }

    public void register(String username, String password, String email) throws ResponseException {
        var request = new RegisterRequest(username, password, email);
        RegisterResult result = this.makeRequest("POST", "/user", request, RegisterResult.class);
        authToken = result.getAuthToken();
    }

    public void createGame(String gameName) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, new CreateGameRequest(gameName, authToken), CreateGameResult.class);
    }

    public void joinObserver(int gameId) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest(gameId, authToken, null), JoinGameResult.class);
    }

    public void joinGame(int gameId, String playerColor) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest(gameId, authToken, playerColor), JoinGameResult.class);

    }


    public Collection<GameData> listGames() throws ResponseException {
        var path = "/game";
        ListGameResult result = this.makeRequest("GET", path, new ListGameRequest(authToken), ListGameResult.class);
        return result.getGames();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);


            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }

            if (Objects.equals(method, "GET")) {
                http.setDoOutput(false);
            } else {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public String clear() throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, null);
    }

    public String getAuthToken() {
        return authToken;
    }
}

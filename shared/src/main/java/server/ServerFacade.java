package server;

import com.google.gson.Gson;


import exception.DataAccessException;
import responserequest.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse createUser(RegisterRequest user) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, null, user, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest user) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, null, user, LoginResponse.class);
    }

    public void deleteUser(String authentication) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, authentication, null, null);
    }

    public MakeGameResponse createGame(MakeGameRequest game, String authentication) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, authentication, game, MakeGameResponse.class);
    }

    public GetAllGamesResponse listGames(String authentication) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, authentication, null, GetAllGamesResponse.class);
    }

    public void joinGame(JoinGameRequest player, String authentication) throws DataAccessException {
        var path = "/game";
        this.makeRequest("PUT", path, authentication, player, null);
    }

    public void deleteDatabase() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, "Blank", null, null);
    }

    private <T> T makeRequest(String method, String path, String authentication, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(authentication, request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(String authentication, Object request, HttpURLConnection http) throws IOException {
        if (authentication != null){
            http.setRequestProperty("Authorization", authentication);
        }
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
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
}

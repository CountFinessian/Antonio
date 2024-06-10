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

    public RegisterResponse createUser(RegisterRequest User) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, null, User, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest User) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, null, User, LoginResponse.class);
    }

    public void deleteUser(String Authentication) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, Authentication, null, null);
    }

    public MakeGameResponse createGame(MakeGameRequest game, String Authentication) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, Authentication, game, MakeGameResponse.class);
    }

    public GetAllGamesResponse listGames(String Authentication) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, Authentication, null, GetAllGamesResponse.class);
    }

    public void joinGame(JoinGameRequest player, String Authentication) throws DataAccessException {
        var path = "/game";
        this.makeRequest("PUT", path, Authentication, player, null);
    }

    public void deleteDatabase() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, "Blank", null, null);
    }

    private <T> T makeRequest(String method, String path, String Authentication, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(Authentication, request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(String Authentication, Object request, HttpURLConnection http) throws IOException {
        if (Authentication != null){
            http.setRequestProperty("Authorization", Authentication);
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

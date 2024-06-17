package server;
import com.google.gson.Gson;

import com.google.gson.JsonObject;
import dataaccess.*;
import exception.DataAccessException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import responserequest.*;
import service.*;

import server.websocket.WebSocketHandler;

import spark.*;
import java.util.List;

public class Server {

    private final WebSocketHandler webSocketHandler;
    private UserService userservice;
    private GameService gameservice;
    private AuthService authservice;

    public Server() {
        this.webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        try {
            gameservice = new GameService(new SQLGameDAO());
            authservice = new AuthService(new SQLAuthDAO());
            userservice = new UserService(new SQLUserDAO());

        } catch (exception.DataAccessException e) {
            throw new RuntimeException(e);
        }

        Spark.post("/user", this::createUser);
        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginUser);

        Spark.delete("/session", this::deleteUser);

        Spark.post("/game", this::createGame);

        Spark.get("/game", this::listGames);

        Spark.put("/game", this::joinGame);

        Spark.delete("/db", this::deleteDatabase);

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object createUser(Request req, Response res) throws exception.DataAccessException {
//      if statements to check for bad data such as an invalid request.
        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);

        if (user.username() == null || user.password() == null || user.email() == null) {
            res.status(400);
            RegistrationError registerReturn = new RegistrationError("Error: bad request");
            return new Gson().toJson(registerReturn);
        }


        if (userservice.getUser(user.username()) != null) {
            res.status(403);
            RegistrationError registerReturn = new RegistrationError("Error: already taken");
            return new Gson().toJson(registerReturn);
        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        UserData userdata = new UserData(user.username(), hashedPassword, user.email());

        UserData createdUser = userservice.createUser(userdata);

        AuthData createdAuth = authservice.createAuth(createdUser);

        RegisterResponse registerReturn = new RegisterResponse(createdAuth.username(), createdAuth.authToken());
        res.status(200);
        return new Gson().toJson(registerReturn);
    }

    private Object loginUser(Request req, Response res) throws exception.DataAccessException {
        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);

        UserData checkUserData = userservice.getUser(user.username());

        if (checkUserData == null || !BCrypt.checkpw(user.password(), checkUserData.password())) {
            res.status(401);

            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }

        AuthData createdAuth = authservice.createAuth(checkUserData);

        LoginResponse loginReturn = new LoginResponse(createdAuth.username(), createdAuth.authToken());
        res.status(200);
        return new Gson().toJson(loginReturn);

    }

    private Object deleteUser(Request req, Response res) throws exception.DataAccessException {

        AuthData auth = authenticator(req, "null");
        if (auth == null) {
            res.status(401);
            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }

        authservice.logoutAuth(auth.authToken());

        res.status(200);
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private Object createGame(Request req, Response res) throws exception.DataAccessException {

        if (authenticator(req, "null") == null) {
            res.status(401);
            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }

        MakeGameRequest makegamerequest = new Gson().fromJson(req.body(), MakeGameRequest.class);
        String gameName = makegamerequest.gameName();


        GameData newGame = gameservice.createGame(gameName);
        MakeGameResponse makegameresponse = new MakeGameResponse(newGame.gameID());
        res.status(200);
        return new Gson().toJson(makegameresponse);
    }

    private Object listGames(Request req, Response res) throws exception.DataAccessException {
        if (authenticator(req, "null") == null) {
            res.status(401);
            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }

        List<GameData> allGames = gameservice.getAllGames();

        GetAllGamesResponse getallgamesresponse = new GetAllGamesResponse(allGames);
        res.status(200);
        return new Gson().toJson(getallgamesresponse);
    }

    private Object joinGame(Request req, Response res) throws exception.DataAccessException {
        JoinGameRequest joingamerequest = new Gson().fromJson(req.body(), JoinGameRequest.class);

        if (joingamerequest.gameID() == null || joingamerequest.playerColor() == null) {

            res.status(400);
            RegistrationError registerReturn = new RegistrationError("Error: bad request");
            return new Gson().toJson(registerReturn);
        }

        AuthData auth = authenticator(req, "null");
        if (auth == null) {
            res.status(401);
            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }
        Boolean joinedGame = gameservice.joinGame(auth.username(), joingamerequest.playerColor(), joingamerequest.gameID());
        res.status(200);

        if (joinedGame) {
            JsonObject emptyJsonObject = new JsonObject();
            return new Gson().toJson(emptyJsonObject);
        } else {
            res.status(403);
            RegistrationError registerReturn = new RegistrationError("Error: already taken");
            return new Gson().toJson(registerReturn);
        }
    }

    private Object deleteDatabase(Request req, Response res) throws exception.DataAccessException {
        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();
        res.status(200);
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private AuthData authenticator(Request req, String workaround) throws DataAccessException {
        if (workaround == "null") {
            String authToken = req.headers("Authorization");
            AuthData myAuth = authservice.getAuth(authToken);
            return myAuth;
        } else {
            AuthData myAuth = authservice.getAuth(workaround);
            return myAuth;
        }
    }
}
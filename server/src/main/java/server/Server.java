package server;
import com.google.gson.Gson;
import RequestResponse.*;

import com.google.gson.JsonObject;
import dataaccess.*;
import model.*;
import service.*;

import spark.*;

import java.util.List;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::createUser);
        // Register your endpoints and handle exceptions here.
        Spark.post("/session", this::loginUser);

        Spark.delete("/session", this::deleteUser);

        Spark.post("/game", this::createGame);

        Spark.get("/game", this::listGames);
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object createUser( Request req, Response res ) throws DataAccessException {
//      if statements to check for bad data such as an invalid request.
        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);

        if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
            res.status(400);
            RegistrationError registerReturn = new RegistrationError("Error: bad request");
            return new Gson().toJson(registerReturn);
        }
        UserData userdata = new UserData(user.username(), user.password(), user.email());
        UserService userservice = new UserService(new MemoryUserDAO());

        if (userservice.getUser(userdata) != null){
            res.status(403);
            RegistrationError registerReturn = new RegistrationError("Error: already taken");
            return new Gson().toJson(registerReturn);
        }

        UserData createdUser = userservice.createUser(userdata);

        AuthService authservice = new AuthService(new MemoryAuthDAO());
        AuthData createdAuth = authservice.createAuth(createdUser);

        RegisterResponse registerReturn = new RegisterResponse(createdAuth.username(), createdAuth.authToken());
        res.status(200);
        return new Gson().toJson(registerReturn);
    }

    private Object loginUser( Request req, Response res ) throws DataAccessException {
        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);

        UserData userdata = new UserData(user.username(), user.password(), "chicken.com");
        UserService userservice = new UserService(new MemoryUserDAO());
        UserData checkUserData = userservice.getUser(userdata);

        if (checkUserData == null || !userdata.password().equals(checkUserData.password())) {
            res.status(401);
            RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            return new Gson().toJson(registerReturn);
        }

            AuthService authservice = new AuthService(new MemoryAuthDAO());
            AuthData createdAuth = authservice.createAuth(checkUserData);

            LoginResponse loginReturn = new LoginResponse(createdAuth.username(), createdAuth.authToken());
            res.status(200);
            return new Gson().toJson(loginReturn);

        }
        private Object deleteUser( Request req, Response res ) throws DataAccessException {
            String authToken = req.headers("Authorization");


            AuthService authservice = new AuthService(new MemoryAuthDAO());
            AuthData deletedAuth = authservice.logoutAuth(authToken);

            if (deletedAuth == null) {
                   res.status(401);
                RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            }

            res.status(200);
            JsonObject emptyJsonObject = new JsonObject();
            return new Gson().toJson(emptyJsonObject);
        }

        private Object createGame( Request req, Response res ) throws DataAccessException {
            String authToken = req.headers("Authorization");
            MakeGameRequest makegamerequest = new Gson().fromJson(req.body(), MakeGameRequest.class);
            String gameName = makegamerequest.gameName();

            AuthService authservice = new AuthService(new MemoryAuthDAO());
            AuthData myAuth = authservice.getAuth(authToken);
            if (myAuth == null) {
                res.status(401);
                RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            }

            GameService gameservice = new GameService(new MemoryGameDAO());
            GameData newGame = gameservice.createGame(gameName);
            MakeGameResponse makegameresponse = new MakeGameResponse(newGame.gameID());
            res.status(200);
            return new Gson().toJson(makegameresponse);
        }

        private Object listGames ( Request req, Response res ) throws DataAccessException {
            String authToken = req.headers("Authorization");
            AuthService authservice = new AuthService(new MemoryAuthDAO());
            AuthData myAuth = authservice.getAuth(authToken);
            if (myAuth == null) {
                res.status(401);
                RegistrationError registerReturn = new RegistrationError("Error: unauthorized");
            }
            GameService gameservice = new GameService(new MemoryGameDAO());
            List<GameData> allGames = gameservice.getAllGames();

            GetAllGamesResponse getallgamesresponse = new GetAllGamesResponse(allGames);
            res.status(200);
            return new Gson().toJson(getallgamesresponse);
        }


    }

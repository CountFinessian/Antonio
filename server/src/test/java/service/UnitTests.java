package service;
import RequestResponse.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passoff.model.TestUser;
import passoff.server.TestServerFacade;

import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
    private static UserService userservice = new UserService(new MemoryUserDAO());
    private static GameService gameservice = new GameService(new MemoryGameDAO());
    private static AuthService authservice = new AuthService(new MemoryAuthDAO());
    private static UserData user = new UserData("JAWILL", "password", "jawill@byu.edu");
    @BeforeEach
    void clear() throws DataAccessException {
        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();
    }

    @Test
    void Register() {
        var LoginIdentity = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(LoginIdentity);

        var LoginResult = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", LoginResult.username());

        var LoginIdentity2 = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNotNull(LoginIdentity2);

        var LoginResult2 = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", LoginResult2.username());
    }

    @Test
    void Login() throws DataAccessException {
        UserData LoginResult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData AuthResult = assertDoesNotThrow(() -> authservice.createAuth(LoginResult));

        String authToken = AuthResult.authToken();
        authservice.logoutAuth(authToken);

        AuthData createdAuth = authservice.createAuth(LoginResult);
        assertEquals("JAWILL", createdAuth.username());

        authservice.logoutAuth(authToken);
       assertNull(userservice.getUser("KAWILL"));
}

    @Test
    void Logout() throws DataAccessException {
        UserData LoginResult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData AuthResult = assertDoesNotThrow(() -> authservice.createAuth(LoginResult));

        String authToken = AuthResult.authToken();
        assertNotNull(authToken);

        authservice.logoutAuth(authToken);
        assertNull(authservice.getAuth(authToken));

        AuthData AuthResult2 = assertDoesNotThrow(() -> authservice.createAuth(LoginResult));
        String authToken2 = AuthResult2.authToken();
        assertNotNull(authToken2);

        authservice.logoutAuth(null);
        assertNotNull(authservice.getAuth(authToken2));
    }

    @Test
    void createGame() throws DataAccessException {
        UserData LoginResult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData AuthResult = assertDoesNotThrow(() -> authservice.createAuth(LoginResult));

        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newGame.gameName(), "GG");

        authservice.logoutAuth(AuthResult.authToken());
        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);

        GameData newGame2 = assertDoesNotThrow(() -> gameservice.createGame(null));
        assertEquals(newGame2.gameName(), null);
    }

    @Test
    void listGames() throws DataAccessException {
        List<GameData> GameList0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(GameList0.size(), 0);

        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        List<GameData> GameList1 = assertDoesNotThrow(() -> gameservice.getAllGames());

        assertEquals(GameList1.size(), 1);
        assertEquals(GameList1.get(0).gameName(), newGame.gameName());

        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);
    }

    @Test
    void joinGame() throws DataAccessException {
        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newGame.gameName(), "GG");

        Boolean status = gameservice.joinGame("JAWILL", "WHITE", 1);
        assertTrue(status);

        Boolean status2 = gameservice.joinGame("KAWILL", "WHITE", 1);
        assertFalse(status2);
    }

    @Test
    void clearGame() throws DataAccessException {
        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        UserData LoginResult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData AuthResult = assertDoesNotThrow(() -> authservice.createAuth(LoginResult));

        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();

        List<GameData> GameList0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(GameList0.size(), 0);

        assertNull(userservice.getUser("JAWILL"));
        assertNull(authservice.getAuth(AuthResult.authToken()));

    }
}
package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void register() {
        var loginidentity = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(loginidentity);

        var loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", loginresult.username());

        var loginidentity2 = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNotNull(loginidentity2);

        var loginresult2 = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", loginresult2.username());
    }

    @Test
    void login() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        String authToken = authresult.authToken();
        authservice.logoutAuth(authToken);

        AuthData createdAuth = authservice.createAuth(loginresult);
        assertEquals("JAWILL", createdAuth.username());

        authservice.logoutAuth(authToken);
       assertNull(userservice.getUser("KAWILL"));
}

    @Test
    void logout() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        String authToken = authresult.authToken();
        assertNotNull(authToken);

        authservice.logoutAuth(authToken);
        assertNull(authservice.getAuth(authToken));

        AuthData authresult2 = assertDoesNotThrow(() -> authservice.createAuth(loginresult));
        String authtoken2 = authresult2.authToken();
        assertNotNull(authtoken2);

        authservice.logoutAuth(null);
        assertNotNull(authservice.getAuth(authtoken2));
    }

    @Test
    void createGame() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newGame.gameName(), "GG");

        authservice.logoutAuth(authresult.authToken());
        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);

        GameData newgame2 = assertDoesNotThrow(() -> gameservice.createGame(null));
        assertEquals(newgame2.gameName(), null);
    }

    @Test
    void listGames() throws DataAccessException {
        List<GameData> GameList0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(GameList0.size(), 0);

        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        List<GameData> gamelist1 = assertDoesNotThrow(() -> gameservice.getAllGames());

        assertEquals(gamelist1.size(), 1);
        assertEquals(gamelist1.get(0).gameName(), newgame.gameName());

        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);
    }

    @Test
    void joinGame() throws DataAccessException {
        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newgame.gameName(), "GG");

        Boolean status = gameservice.joinGame("JAWILL", "WHITE", 1);
        assertTrue(status);

        Boolean status2 = gameservice.joinGame("KAWILL", "WHITE", 1);
        assertFalse(status2);
    }

    @Test
    void clearGame() throws DataAccessException {
        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();

        List<GameData> GameList0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(GameList0.size(), 0);

        assertNull(userservice.getUser("JAWILL"));
        assertNull(authservice.getAuth(authresult.authToken()));

    }
}
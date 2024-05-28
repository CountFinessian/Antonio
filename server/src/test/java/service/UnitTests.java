package service;
import RequestResponse.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passoff.model.TestUser;
import passoff.server.TestServerFacade;

import server.Server;

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

        //Login, but use the wrong Auth Token



    }

//
//    void Register() {
//        var passwordResult = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("JAWILL", passwordResult.getUsername());
//
//        var rentry = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("Error: already taken", rentry.getMessage());
//    }
//
//    void Register() {
//        var passwordResult = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("JAWILL", passwordResult.getUsername());
//
//        var rentry = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("Error: already taken", rentry.getMessage());
//    }
//
//
//    void Register() {
//        var passwordResult = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("JAWILL", passwordResult.getUsername());
//
//        var rentry = assertDoesNotThrow(() -> serverFacade.register(newUser));
//        assertEquals("Error: already taken", rentry.getMessage());
//    }


}
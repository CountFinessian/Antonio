package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
    private static UserService userservice;
    private static GameService gameservice;
    private static AuthService authservice;
    private static UserData user = new UserData("JAWILL", "password", "jawill@byu.edu");
    @BeforeEach
    void clear() throws DataAccessException {
        try {
            gameservice = new GameService(new SQLGameDAO());
            authservice = new AuthService(new SQLAuthDAO());
            userservice = new UserService(new SQLUserDAO());;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();
    }

    @Test
    void registerpos() {
        var loginidentity = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(loginidentity);

        var loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", loginresult.username());

        var loginidentity2 = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNotNull(loginidentity2);
    }
    @Test
    void registerneg() {
        var loginidentity = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(loginidentity);

        var loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", loginresult.username());

        var loginidentity2 = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNotNull(loginidentity2);
    }
    @Test
    void loginpos() throws DataAccessException {
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
    void loginneg() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        String authToken = authresult.authToken();
        authservice.logoutAuth(authToken);

        authservice.logoutAuth(authToken);
        assertNull(userservice.getUser("KAWILL"));
    }
    @Test
    void logoutpos() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        String authToken = authresult.authToken();
        assertNotNull(authToken);

        authservice.logoutAuth(authToken);
        assertNull(authservice.getAuth(authToken));
    }
    @Test
    void logoutneg() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult2 = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        String authtoken2 = authresult2.authToken();
        assertNotNull(authtoken2);

        authservice.logoutAuth(null);
        assertNotNull(authservice.getAuth(authtoken2));
    }
    @Test
    void creategamepos() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newGame.gameName(), "GG");
    }
    @Test
    void creategameneg() throws DataAccessException {
        UserData loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        AuthData authresult = assertDoesNotThrow(() -> authservice.createAuth(loginresult));

        authservice.logoutAuth(authresult.authToken());
        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameservice.createGame(null);
        });
        assertTrue(exception.getMessage().contains("Game name cannot be null or empty"));
    }

    @Test
    void listgamespos() throws DataAccessException {
        List<GameData> gamelist0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(gamelist0.size(), 0);

        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        List<GameData> gamelist1 = assertDoesNotThrow(() -> gameservice.getAllGames());

        assertEquals(gamelist1.size(), 1);
        assertEquals(gamelist1.get(0).gameName(), newgame.gameName());
    }
    @Test
    void listgamesneg() throws DataAccessException {
        List<GameData> gamelist0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(gamelist0.size(), 0);

        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        List<GameData> gamelist1 = assertDoesNotThrow(() -> gameservice.getAllGames());

        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);
    }
    @Test
    void joingamepos() throws DataAccessException {
        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newgame.gameName(), "GG");

        Boolean status = gameservice.joinGame("JAWILL", "WHITE", 1);
        assertTrue(status);
    }
    @Test
    void joingameneg() throws DataAccessException {
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

        List<GameData> gamelist0 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(gamelist0.size(), 0);

        assertNull(userservice.getUser("JAWILL"));
        assertNull(authservice.getAuth(authresult.authToken()));

    }

    @Test
    void editgamePos() throws DataAccessException {
        GameData newgame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newgame.gameName(), "GG");

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game FROM GameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, newgame.gameID());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String game = rs.getString("game");
                        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
                        chessGame.boardRefill();
                        mynew ChessMove();
                        chessGame.makeMove();


                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Invalid color specified" + e.getMessage());
        }
    }
}
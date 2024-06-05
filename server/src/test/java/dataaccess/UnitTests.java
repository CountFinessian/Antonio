package dataaccess;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnitTests {
    private static UserService userservice;
    private static GameService gameservice;
    private static AuthService authservice;
    private static UserData user = new UserData("JAWILL", "password", "jawill@byu.edu");
    private static UserData user2 = new UserData("KAWILL", "password", "kawill@byu.edu");

    @BeforeAll
    static void clear() throws DataAccessException {
        try {
            gameservice = new GameService(new SQLGameDAO());
            authservice = new AuthService(new SQLAuthDAO());
            userservice = new UserService(new SQLUserDAO());
            ;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();
    }

    @Test
    void registrationpositive() {
        var attemptToRegister = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", attemptToRegister.username());
    }

    @Test
    void registrationnegative() {
        var registerSameUser = assertThrows(DataAccessException.class, () -> { userservice.createUser(user); });
        assertEquals("Unable to configure database: Duplicate entry 'JAWILL' for key 'UserData.PRIMARY'", registerSameUser.getMessage());
    }

    @Test
    void loginpositive() throws DataAccessException {
        var userLogin = assertDoesNotThrow(() ->  userservice.getUser("JAWILL"));
        assertEquals(user, userLogin);

        var validAuthCreation = assertDoesNotThrow(() -> authservice.createAuth(user));
        assertEquals("JAWILL", validAuthCreation.username());
    }

    @Test
    void loginnegative() throws DataAccessException {
        var invalidAuthCreation = assertDoesNotThrow(() ->  userservice.getUser("KAWILL"));
        assertEquals(null, invalidAuthCreation);
    }

    @Test
    void logoutnegative() throws DataAccessException {
        var validAuthCreation = assertDoesNotThrow(() -> authservice.createAuth(user));
        assertDoesNotThrow(() -> authservice.logoutAuth(null));

        var tryToLogin = assertDoesNotThrow(() -> authservice.getAuth(validAuthCreation.authToken()));
        assertEquals("JAWILL", tryToLogin.username());
    }

    @Test
    void logoutpositive() throws DataAccessException {
        var validAuthCreation = assertDoesNotThrow(() -> authservice.createAuth(user));
        assertDoesNotThrow(() -> authservice.logoutAuth(validAuthCreation.authToken()));

        var tryToLogin = assertDoesNotThrow(() -> authservice.getAuth(validAuthCreation.authToken()));
        assertEquals(null, tryToLogin);
    }

    @Test
    void creategamepositive() throws DataAccessException {
        GameData newGame = assertDoesNotThrow(() -> gameservice.createGame("GG"));
        assertEquals(newGame.gameName(), "GG");
    }

    @Test
    void creategamenegative() throws DataAccessException {
        var emptyStringGame = assertThrows(DataAccessException.class, () -> gameservice.createGame(""));
        assertEquals("Game name cannot be null or empty", emptyStringGame.getMessage());
    }

    @Test
    void listgamespositive() throws DataAccessException {
        List<GameData> gamelist1 = assertDoesNotThrow(() -> gameservice.getAllGames());
        assertEquals(1, gamelist1.size());
    }

    @Test
    void listgamesnegative() throws DataAccessException {
        AuthData newUser = assertDoesNotThrow(() -> authservice.getAuth(null));
        assertNull(newUser);
    }

    @Test
    void joingamepositive() throws DataAccessException {
        Boolean joinGameStatus = assertDoesNotThrow(() -> gameservice.joinGame("JAWILL", "WHITE", 1));
        assertTrue(joinGameStatus);
    }

    @Test
    void joingamenegative() throws DataAccessException {
        Boolean duplicateJoinStatus = assertDoesNotThrow(() -> gameservice.joinGame("KAWILL", "WHITE", 1));
        assertNull(duplicateJoinStatus);
    }

    @Test
    void clearGame() throws DataAccessException {
        var registerDifferentUser = assertDoesNotThrow( () -> userservice.createUser(user2));
        var validAuthCreation = assertDoesNotThrow(() -> authservice.createAuth(user2));

        assertDoesNotThrow(() -> authservice.clearAuths());
        assertDoesNotThrow(() -> gameservice.clearGames());
        assertDoesNotThrow(() ->userservice.clearUsers());

        assertEquals(gameservice.getAllGames().size(), 0);
        assertNull(userservice.getUser(registerDifferentUser.username()));
        assertNull(authservice.getAuth(validAuthCreation.authToken()));
    }
    @Test
    void registerpos() {
        var loginidentity = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(loginidentity);

        var loginresult = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", loginresult.username());
    }
    @Test
    void registerneg() {
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
    void logout() throws DataAccessException {
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

        GameData newgame2 = assertDoesNotThrow(() -> gameservice.createGame(null));
        assertEquals(newgame2.gameName(), null);
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
    void clearGame1() throws DataAccessException {
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
    void editgame() throws DataAccessException {
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

                        ChessMove myChessMove2 = new ChessMove(new ChessPosition(2, 2), new ChessPosition(2, 4), null);
                        assertThrows(InvalidMoveException.class, () -> {
                            chessGame.makeMove(myChessMove2);
                        });

                        ChessMove myChessMove = new ChessMove(new ChessPosition(2, 2), new ChessPosition(4, 2), null);
                        chessGame.makeMove(myChessMove);
                        String jsongame = new Gson().toJson(chessGame);
                        var updateStatementW = "UPDATE GameData SET game=? WHERE gameID=?";
                        SQLUserDAO.executeUpdate(updateStatementW, jsongame, newgame.gameID());
                    }
                }
            }
        } catch (SQLException | InvalidMoveException e) {
            throw new DataAccessException("SQL Error: " + e.getMessage());
        }
    }
}

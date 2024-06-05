package dataaccess;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
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
        var attemptToLogin = assertDoesNotThrow(() -> userservice.getUser("JAWILL"));
        assertNull(attemptToLogin);

        var attemptToRegister = assertDoesNotThrow(() -> userservice.createUser(user));
        assertEquals("JAWILL", attemptToRegister.username());
    }
    @Test
    void registrationnegative() {
        var registerSameUser = assertThrows(DataAccessException.class, () -> { userservice.createUser(user); });
        assertEquals("Unable to configure database: Duplicate entry 'JAWILL' for key 'userdata.PRIMARY'", registerSameUser.getMessage());
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

        authservice.clearAuths();
        gameservice.clearGames();
        userservice.clearUsers();

        assertEquals(gameservice.getAllGames().size(), 0);
        assertNull(userservice.getUser(registerDifferentUser.username()));
        assertNull(authservice.getAuth(validAuthCreation.authToken()));
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
                        String JsonGame = new Gson().toJson(chessGame);
                        var updateStatementW = "UPDATE GameData SET game=? WHERE gameID=?";
                        SQLUserDAO.executeUpdate(updateStatementW, JsonGame, newgame.gameID());
                    }
                }
            }
        } catch (SQLException | InvalidMoveException e) {
            throw new DataAccessException("SQL Error: " + e.getMessage());
        }
    }
}
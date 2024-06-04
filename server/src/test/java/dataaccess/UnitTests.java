package dataaccess;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
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
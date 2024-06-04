package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    final static private List<GameData> GAMESLIST = new ArrayList<>();

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Game name cannot be null or empty");
        }
        var statement = "INSERT INTO GameData (gameName, game) VALUES (?, ?)";
        ChessGame chessBoard = new ChessGame();
        String JsonGame = new Gson().toJson(chessBoard);
        Integer ChessID = SQLUserDAO.executeUpdate(statement, gameName, JsonGame);
        GameData game = new GameData(ChessID, null, null, gameName, chessBoard);
        return game;
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        GAMESLIST.clear();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GAMESLIST.add(readGame(rs));
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Unable to read games: " + ex.getMessage());
        }
        return GAMESLIST;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameId = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var chessGame = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
    }

    @Override
    public Boolean joinGame(String username, String color, Integer gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");

                        GameData newGame;
                        switch (color) {
                            case "WHITE":
                                if (whiteUsername != null) {
                                    return false;
                                }
                                var updateStatementW = "UPDATE GameData SET whiteUsername=? WHERE gameID=?";
                                SQLUserDAO.executeUpdate(updateStatementW, username, gameId);
                                break;
                            case "BLACK":
                                if (blackUsername != null) {
                                    return false;
                                }
                                var updateStatementB = "UPDATE GameData SET blackUsername=? WHERE gameID=?";
                                SQLUserDAO.executeUpdate(updateStatementB, username, gameId);
                                break;
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Invalid color specified" + e.getMessage());
        }
        return null;
    }

    @Override
    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE GameData";
        SQLUserDAO.executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS GameData (
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(256) DEFAULT NULL,
            blackUsername VARCHAR(256) DEFAULT NULL,
            gameName VARCHAR(256) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY (gameID)
        )"""
    };

    private void configureDatabase() throws DataAccessException {
        SQLUserDAO.databaseConfigure(createStatements);
    }
}

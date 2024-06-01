package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
        //make new chess board
        //GSON.JSON.toString(board)


//        preparedstatement("gamename" "board")
//        preparedstatement.executeUpdate
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public Boolean joinGame(String username, String color, Integer gameId) {
        return null;
    }

    @Override
    public void clearGames() throws DataAccessException {

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

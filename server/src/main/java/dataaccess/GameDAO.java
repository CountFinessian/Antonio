package dataaccess;
import exception.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    GameData createGame (String gameName) throws DataAccessException;
    List<GameData> getAllGames() throws DataAccessException;
    Boolean joinGame(String username, String color, Integer gameId) throws DataAccessException;
    void clearGames () throws DataAccessException;
}

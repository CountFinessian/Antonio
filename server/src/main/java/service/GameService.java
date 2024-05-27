package service;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.List;


public class GameService {
    private final GameDAO GameAccess;

    public GameService(GameDAO gameAccess) {
        this.GameAccess = gameAccess;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return GameAccess.createGame(gameName);
    }
    public List<GameData> getAllGames() throws DataAccessException {
        return GameAccess.getAllGames();
    }
    public Boolean joinGame(String username, String color, Integer gameID) throws DataAccessException {
        return GameAccess.joinGame(username, color, gameID);
    }

}


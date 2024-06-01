package service;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.List;


public class GameService {
    private final GameDAO gameaccess;

    public GameService(GameDAO gameaccess) {
        this.gameaccess = gameaccess;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return gameaccess.createGame(gameName);
    }
    public List<GameData> getAllGames() throws DataAccessException {
        return gameaccess.getAllGames();
    }
    public Boolean joinGame(String username, String color, Integer gameID) throws DataAccessException {
        return gameaccess.joinGame(username, color, gameID);

    }
    public void clearGames() throws DataAccessException {
        gameaccess.clearGames();
    }

}


package dataaccess;

import chess.ChessGame;
import model.*;
import exception.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    final static private HashMap<Integer, GameData> GAMES = new HashMap<>();
    final static private List<GameData> GAMESLIST = new ArrayList<>();

    public GameData createGame(String gameName) {
        Integer gameID = GAMES.size() + 1;
        GameData game = new GameData(gameID,null, null, gameName, new ChessGame());
        GAMES.put(gameID, game);
        return game;
    }

    public List<GameData> getAllGames() {
        GAMESLIST.clear();
        GAMESLIST.addAll(GAMES.values());
        return GAMESLIST;
    }

    public Boolean joinGame(String username, String color, Integer gameID) {
        GameData game = GAMES.get(gameID);
        GameData newGame;
        switch (color) {
            case "WHITE":
                if (game.whiteUsername() != null) {
                    return false;
                }
                newGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
                GAMES.put(gameID, newGame);
                break;
            case "BLACK":
                if (game.blackUsername() != null) {
                    return false;
                }
                newGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
                GAMES.put(gameID, newGame);
                break;
        }
        return true;
    }

    public void clearGames () throws DataAccessException {
        GAMES.clear();
    }
}


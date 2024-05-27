package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    final static private HashMap<Integer, GameData> games = new HashMap<>();
    final static private List<GameData> gamesList = new ArrayList<>();

    public GameData createGame(String gameName) {
        Integer gameID = games.size() + 1;
        GameData game = new GameData(gameID,"", "", gameName, new ChessGame());
        games.put(gameID, game);
        return game;
    }

    public List<GameData> getAllGames() {
        gamesList.clear();
        gamesList.addAll(games.values());
        return gamesList;
    }

    public Boolean joinGame(String username, String color, Integer gameID) {
        GameData game = games.get(gameID);
        GameData newGame;
        switch (color) {
            case "WHITE":
                if (!game.whiteUsername().isEmpty()) {
                    return false;
                }
                newGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
                games.put(gameID, newGame);
                break;
            case "BLACK":
                if (!game.blackUsername().isEmpty()) {
                    return false;
                }
                newGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
                games.put(gameID, newGame);
                break;
        }
        return true;
    }
}


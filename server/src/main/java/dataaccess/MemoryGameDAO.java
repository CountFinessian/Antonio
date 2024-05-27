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
}

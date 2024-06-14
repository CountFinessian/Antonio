package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    ChessMove move;
    public MakeMove (int GameID, String authToken, ChessMove move){
        super(authToken, CommandType.MAKE_MOVE, GameID);
        this.move = move;
    }
}

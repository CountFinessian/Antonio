package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BISHOP {
    private ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        new KNIGHT().move(board, myPosition, -1, 1, moves, false);
        new KNIGHT().move(board, myPosition, 1, -1, moves, false);
        new KNIGHT().move(board, myPosition, 1, 1, moves, false);
        new KNIGHT().move(board, myPosition, -1, -1, moves, false);
        return moves;
    };

};
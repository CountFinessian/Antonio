package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BISHOP {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        skirtingSideways(board, myPosition, moves, false);
        return moves;
    };
    static void skirtingSideways(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> moves, boolean oneMove)  {
        new KNIGHT().move(board, myPosition, -1, 1, moves, oneMove);
        new KNIGHT().move(board, myPosition, 1, -1, moves, oneMove);
        new KNIGHT().move(board, myPosition, 1, 1, moves, oneMove);
        new KNIGHT().move(board, myPosition, -1, -1, moves, oneMove);
    }
};
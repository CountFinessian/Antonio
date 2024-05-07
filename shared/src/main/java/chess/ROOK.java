package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ROOK {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        WASD(board, myPosition, moves, false);
        return moves;
    }

    static void WASD(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> moves, boolean oneMove) {
        new KNIGHT().move(board, myPosition, 0, 1, moves, oneMove);
        new KNIGHT().move(board, myPosition, 0, -1, moves, oneMove);
        new KNIGHT().move(board, myPosition, 1, 0, moves, oneMove);
        new KNIGHT().move(board, myPosition, -1, 0, moves, oneMove);
    }

    ;

};
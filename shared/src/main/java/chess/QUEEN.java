package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QUEEN {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        ROOK.WASD(board, myPosition, moves, false);
        BISHOP.skirtingSideways(board, myPosition, moves, false);
        return moves;
    }



    ;
};
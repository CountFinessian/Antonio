package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KING {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        ROOK.WASD(board, myPosition, moves, true);
        BISHOP.skirtingSideways(board, myPosition, moves, true);
        return moves;
    };

};
package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KING {
    private ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        new KNIGHT().move(board, myPosition, 0, 1, moves, true);
        new KNIGHT().move(board, myPosition, 0, -1, moves, true);
        new KNIGHT().move(board, myPosition, 1, 0, moves, true);
        new KNIGHT().move(board, myPosition, -1, 0, moves, true);

        new KNIGHT().move(board, myPosition, -1, 1, moves, true);
        new KNIGHT().move(board, myPosition, 1, -1, moves, true);
        new KNIGHT().move(board, myPosition, 1, 1, moves, true);
        new KNIGHT().move(board, myPosition, -1, -1, moves, true);
        return moves;
    };

};
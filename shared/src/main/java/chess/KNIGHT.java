package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KNIGHT {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        move(board, myPosition, 1, 2, moves, true);
        move(board, myPosition, -1, 2, moves, true);
        move(board, myPosition, -2, 1, moves, true);
        move(board, myPosition, 2, 1, moves, true);

        move(board, myPosition, 1, -2, moves, true);
        move(board, myPosition, -1, -2, moves, true);
        move(board, myPosition, -2, -1, moves,true);
        move(board, myPosition, 2, -1, moves, true);

        return moves;
    };
    public void move(ChessBoard board, ChessPosition myPosition, int row_change, int col_change, ArrayList<ChessMove> moves, boolean oneMove) {
        int row_end = row_change + myPosition.getRow();
        int col_end = col_change + myPosition.getColumn();

        if (oneMove) {
            if (row_end >= 1 && row_end <= 8 && col_end >= 1 && col_end <= 8) {

                ChessPosition endPosition = new ChessPosition(row_end, col_end);

                if (board.getPiece(endPosition) != null) {
                    if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.addFirst(new ChessMove(myPosition, endPosition, null));
                    }
                    return;
                }

                moves.addFirst(new ChessMove(myPosition, endPosition, null));
            }
        }
        else {
                while (row_end >= 1 && row_end <= 8 && col_end >= 1 && col_end <= 8) {

                    ChessPosition endPosition = new ChessPosition(row_end, col_end);

                    if (board.getPiece(endPosition) != null) {
                        if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            moves.addFirst(new ChessMove(myPosition, endPosition, null));
                        }
                        return;
                    }

                    moves.addFirst(new ChessMove(myPosition, endPosition, null));
                    row_end += row_change;
                    col_end += col_change;
                }

        };

    };
};
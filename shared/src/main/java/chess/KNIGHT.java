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
    public void move(ChessBoard board, ChessPosition myPosition, int rowchange, int colchange, ArrayList<ChessMove> moves, boolean oneMove) {
        int rowend = rowchange + myPosition.getRow();
        int colend = colchange + myPosition.getColumn();

        if (oneMove) {
            if (rowend >= 1 && rowend <= 8 && colend >= 1 && colend <= 8) {

                if (newmove(board, myPosition, moves, rowend, colend)) return;
            }
        }
        else {
                while (rowend >= 1 && rowend <= 8 && colend >= 1 && colend <= 8) {

                    if (newmove(board, myPosition, moves, rowend, colend)) return;
                    rowend += rowchange;
                    colend += colchange;
                }

        };

    }

    private boolean newmove(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> moves, int rowend, int colend) {
        ChessPosition endPosition = new ChessPosition(rowend, colend);

        if (board.getPiece(endPosition) != null) {
            if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.addFirst(new ChessMove(myPosition, endPosition, null));
            }
            return true;
        }

        moves.addFirst(new ChessMove(myPosition, endPosition, null));
        return false;
    }

    ;
};
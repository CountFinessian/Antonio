package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BISHOP {
    private ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {


        move(board, myPosition, -1, 1);
        move(board, myPosition, 1, -1);
        move(board, myPosition, 1, 1);
        move(board, myPosition, -1, -1);
        //calculate up/down positions like battleship to pass first test case
        //use the board to see what color piece is in the way. If it's your same color, don't go there.
        return moves;
    };
    private void move(ChessBoard board, ChessPosition myPosition, int x, int y) {
        int a = x + myPosition.getRow();
        int b = y + myPosition.getColumn();
        System.out.println(board.getPiece(myPosition).getPieceType());
        while (a >= 1 && a <= 8 && b >= 1 && b <= 8) {
            ChessPosition endPosition = new ChessPosition(a, b);

            System.out.println(board.getPiece(endPosition));

            if (board.getPiece(endPosition) != null){
                if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.addFirst(new ChessMove(myPosition, endPosition, null));
                }
                return;
            }
            moves.addFirst(new ChessMove(myPosition, endPosition, null));
            a += x;
            b += y;
        }

    };
};
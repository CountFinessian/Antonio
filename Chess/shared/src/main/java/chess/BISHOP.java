package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BISHOP {
    private static ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {


        move(board, myPosition, -1, 1);
        move(board, myPosition, 1, -1);
        move(board, myPosition, 1, 1);
        move(board, myPosition, -1, -1);
        //calculate up/down positions like battleship to pass first test case
        //use the board to see what color piece is in the way. If it's your same color, don't go there.
        return moves;
    };
    private static void move(ChessBoard board, ChessPosition myPosition, int x, int y) {
        int a = x + myPosition.getRow();
        int b = y + myPosition.getColumn();

        while (a >= 1 && a <= 8 && b >= 1 && b <= 8) {
            ChessPosition endPosition = new ChessPosition(a, b);
            moves.addFirst(new ChessMove(myPosition, endPosition, null));
            a += x;
            b += y;
        }

    };
};
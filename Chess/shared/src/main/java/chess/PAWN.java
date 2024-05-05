package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PAWN {
    private ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {

        if (board.getPiece(myPosition).getTeamColor() == WHITE){
            move(board, myPosition, 1, 0);
            if (myPosition.getRow() == 2){
                move(board, myPosition, 2, 0);
            }
            attack(board, myPosition, 1, 1);
            attack(board, myPosition, 1, -1);
        }
        else {move(board, myPosition, -1, 0);
            if (myPosition.getRow() == 7){
                move(board, myPosition, -2, 0);
            }
            attack(board, myPosition, -1, 1);
            attack(board, myPosition, -1, -1);
        }

        return moves;
    };
    private void move(ChessBoard board, ChessPosition myPosition, int row_change, int col_change) {
        int row_end = row_change + myPosition.getRow();
        int col_end = col_change + myPosition.getColumn();

        if (row_end >= 1 && row_end <= 8 && col_end >= 1 && col_end <= 8) {

            if (row_change == 2){
                ChessPosition sandwich = new ChessPosition(1 + myPosition.getRow(), col_end);
                if (board.getPiece(sandwich) != null){
                    return;
                }
            }

            if (row_change == -2){
                ChessPosition sandwich = new ChessPosition(-1 + myPosition.getRow(), col_end);
                if (board.getPiece(sandwich) != null){
                    return;
                }
            }


            ChessPosition endPosition = new ChessPosition(row_end, col_end);

            if (board.getPiece(endPosition) != null){
                return;
            }

            if (row_end == 1 || row_end == 8) {
                promotion(myPosition, endPosition);
            }
           else  moves.addFirst(new ChessMove(myPosition, endPosition, null));
        }


    }

    private void attack(ChessBoard board, ChessPosition myPosition, int row_change, int col_change) {
        int row_end = row_change + myPosition.getRow();
        int col_end = col_change + myPosition.getColumn();

        if (row_end >= 1 && row_end <= 8 && col_end >= 1 && col_end <= 8) {

            ChessPosition endPosition = new ChessPosition(row_end, col_end);

            if (board.getPiece(endPosition) != null){
                if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    if (row_end == 1 || row_end == 8) {
                        promotion(myPosition, endPosition);
                    }
                    else  moves.addFirst(new ChessMove(myPosition, endPosition, null));
                }
                return;
            }

        }

    }

    private void promotion(ChessPosition startPosition, ChessPosition endPosition) {
        moves.addFirst(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moves.addFirst(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.addFirst(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.addFirst(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }
};


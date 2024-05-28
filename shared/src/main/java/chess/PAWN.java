package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PAWN {
    private final ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

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
    private void move(ChessBoard board, ChessPosition myPosition, int rowchange, int colchange) {
        int rowend = rowchange + myPosition.getRow();
        int colend = colchange + myPosition.getColumn();

        if (rowend >= 1 && rowend <= 8 && colend >= 1 && colend <= 8) {

            if (rowchange == 2){
                ChessPosition sandwich = new ChessPosition(1 + myPosition.getRow(), colend);
                if (board.getPiece(sandwich) != null){
                    return;
                }
            }

            if (rowchange == -2){
                ChessPosition sandwich = new ChessPosition(-1 + myPosition.getRow(), colend);
                if (board.getPiece(sandwich) != null){
                    return;
                }
            }


            ChessPosition endPosition = new ChessPosition(rowend, colend);

            if (board.getPiece(endPosition) != null){
                return;
            }

            if (rowend == 1 || rowend == 8) {
                promotion(myPosition, endPosition);
            }
           else  moves.addFirst(new ChessMove(myPosition, endPosition, null));
        }


    }

    private void attack(ChessBoard board, ChessPosition myPosition, int rowchange, int colchange) {
        int rowend = rowchange + myPosition.getRow();
        int colend = colchange + myPosition.getColumn();

        if (rowend >= 1 && rowend <= 8 && colend >= 1 && colend <= 8) {

            ChessPosition endPosition = new ChessPosition(rowend, colend);

            if (board.getPiece(endPosition) != null){
                if ((board.getPiece(endPosition)).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    if (rowend == 1 || rowend == 8) {
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


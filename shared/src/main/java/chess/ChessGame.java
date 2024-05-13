package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn = TeamColor.WHITE;
    private ChessBoard game = new ChessBoard();
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     * Takes as input a position on the chessboard and returns all moves the piece there can legally make.
     * If there is no piece at that location, this method returns null.
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
//        game, turn
        if (game.getPiece(startPosition) == null){
            return null;
        }

        ChessBoard placeholderBoard = game.copy();
        turn = game.getPiece(startPosition).getTeamColor();
        ChessPiece.PieceType type = game.getPiece(startPosition).getPieceType();
        ChessPiece myPiece = new ChessPiece(turn, type);



//            return all possible moves which would work to get King out of check
            Collection<ChessMove> checkedMoves = new ArrayList<>();
            Collection<ChessMove> possibleMoves = myPiece.pieceMoves(game, startPosition);

            for (ChessMove move : possibleMoves) {
                game.removePiece(startPosition);
                game.addPiece(move.getEndPosition(), myPiece);
                if (!isInCheck(turn)){
                    checkedMoves.add(move);
                }
                game = placeholderBoard.copy();
                }
            return checkedMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (game.emptyBoard()){
            game.resetBoard();
        }

        ChessPiece myPiece = game.getPiece(move.getStartPosition());
        if (myPiece == null){
            throw new InvalidMoveException();
        }
        if (myPiece.getTeamColor() != turn){
            throw new InvalidMoveException();
        }
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        for (ChessMove possibleMove : possibleMoves) {

            if (move.equals(possibleMove)) {
                game.removePiece(move.getStartPosition());

                if (move.getPromotionPiece() == null) {
                    game.addPiece(move.getEndPosition(), myPiece);
                }
                else {
                    ChessPiece promotionPiece = new ChessPiece(turn, move.getPromotionPiece());
                    game.addPiece(move.getEndPosition(), promotionPiece);
                }
                turnt();
                return;

            }
        }
        throw new InvalidMoveException();
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingsCrossing = getKing(teamColor);
        if (kingsCrossing == null){
            return false;
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {

                ChessPosition enemySpot = new ChessPosition(row, col);
                if (game.getPiece(enemySpot) != null) {
                    if (game.getPiece(enemySpot).getTeamColor() != teamColor) {
                        ChessPiece enemyPiece = new ChessPiece(game.getPiece(enemySpot).getTeamColor(), game.getPiece(enemySpot).getPieceType());
                        Collection<ChessMove> possibleMoves = (enemyPiece.pieceMoves(game, enemySpot));

                        for (ChessMove move : possibleMoves) {
                            if (move.getEndPosition().equals(kingsCrossing)) {
                                return true;
                            }
                        }

                        }
                    }
                }
            }

        return false;
    }

    public void turnt (){
        if (turn == TeamColor.BLACK){
            turn = TeamColor.WHITE;
        }
        else { turn = TeamColor.BLACK;
        }
    }
    public ChessPosition getKing(TeamColor teamColor) {

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition kingsCrossing = new ChessPosition(row, col);
                if (game.getPiece(kingsCrossing) != null) {
                    if (game.getPiece(kingsCrossing).getTeamColor() == teamColor &&
                            game.getPiece(kingsCrossing).getPieceType() == ChessPiece.PieceType.KING) {
                        return kingsCrossing;
                    }
                }
            }
        }
        return null;
        }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        game = board.copy();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(game, chessGame.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, game);
    }
}
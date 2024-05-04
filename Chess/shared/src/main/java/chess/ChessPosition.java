package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int rowed;
    private final int coled;

    public ChessPosition(int row, int col) {
        this.rowed = row;
        this.coled = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return rowed;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return coled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return rowed == that.rowed && coled == that.coled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowed, coled);
    }

    @Override
    public String toString() {
        return "CP{" +
                "r" + rowed +
                ", c" + coled +
                '}';
    }
}

/**
 * This class represents a Move of the Nim game. Each move has the row in
 * which it is performed, and left and right bounds of the sequence of sticks
 * to mark.
 * @author Idan Refaeli
 *
 */
public class Move {
	
	private int row;
	private int left;
	private int right;
	
	/**
	 * Constructs a Move object with the given parameters
	 * @param inRow The row in which the move is performed.
	 * @param inLeft The left bound of sequence of sticks to mark.
	 * @param inRight The right bound of sequence of sticks to mark.
	 */
	public Move(int inRow, int inLeft, int inRight) {
		row = inRow;
		left = inLeft;
		right = inRight;
	}
	
	/**
	 * Returns a string representation of the move
	 * @return The string representation.
	 */
	public String toString() {
		return row + ":" + left + "-" + right;
	}
	
	/**
	 * Returns the row in which the move is performed.
	 * @return The row.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns the left bound of sequence to mark.
	 * @return The left bound.
	 */
	public int getLeftBound() {
		return left;
	}
	
	/**
	 * Returns the right bound of sequence to mark.
	 * @return The right bound.
	 */
	public int getRightBound() {
		return right;
	}
	
}

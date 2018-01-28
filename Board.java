import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class Board {

	public static final Comparator<Board> BY_HAMMING = new ByHamming();
	public static final Comparator<Board> BY_MANHATTAN = new ByManhattan();

	private Board parentBoard;
	private char[][] boardBlocks;
	private int dim;
	private int moves;

	public Board(int[][] blocks) { 
		// construct a board from an n-by-n array of blocks
		// (where blocks[i][j] = block in row i, column j)
		parentBoard = null;
		dim = blocks.length;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				boardBlocks[i][j] = (char) blocks[i][j];
			}
		}
	}

	public int dimension() { // board dimension n
		return dim;
	}

	public int hamming() { // number of blocks out of place
		int count = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				int index = i * dim + j + 1;
				if (boardBlocks[i][j] != 0 && boardBlocks[i][j] != index) {
					count++;
				}
			}
		}

		return count;
	}

	public int manhattan() { // sum of Manhattan distances between blocks and goal
		int sum = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				int value = boardBlocks[i][j];
				if (value == 0) {
					continue;
				}
				int goalRow = (value - 1) / dim;
				int goalCol = (value - 1) % dim;
				sum += Math.abs(goalRow - i) + Math.abs(goalCol - j);
			}
		}

		return sum;
	}

	public boolean isGoal() { // is this board the goal board?
		return hamming() == 0;
	}

	public Board twin() { // a board that is obtained by exchanging any pair of blocks
		// copy our board
		int[][] blocks = copyBoard();
		// if neither of the first two blocks are blank,
		if (blocks[0][0] != 0 && blocks[0][1] != 0) {
			// switch first two blocks
			blocks[0][0] = boardBlocks[0][1];
			blocks[0][1] = boardBlocks[0][0];
		} else {
			// otherwise, switch first two blocks on second row
			blocks[1][0] = boardBlocks[1][1];
			blocks[1][1] = boardBlocks[1][0];
		}
		return new Board(blocks);
	}

	public boolean equals(Object y) { // does this board equal y? // TODO
		if (y == this) return true;
		if (y == null) return false;
		if (y.getClass() != this.getClass())  return false;
		Board that = (Board) y;
		return Arrays.deepEquals(this.boardBlocks, that.boardBlocks);
	}

	public Iterable<Board> neighbors() { // all neighboring boards
		Stack<Board> stack = new Stack<Board>();

		int blankRow = 0, blankCol = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (boardBlocks[i][j] == 0) {
					blankRow = i;
					blankCol = j;
					continue;
				}
			}
		}

		if (blankCol > 0) {
			int[][] blocks = copyBoard();
			swap(blocks, blankRow, blankCol, blankRow, blankCol - 1);
			stack.push(new Board(blocks));
		}

		if (blankCol < dim - 1) {
			int[][] blocks = copyBoard();
			swap(blocks, blankRow, blankCol, blankRow, blankCol + 1);
			stack.push(new Board(blocks));
		}

		if (blankRow > 0) {
			int[][] blocks = copyBoard();
			swap(blocks, blankRow, blankCol, blankRow - 1, blankCol);
			stack.push(new Board(blocks));
		}

		if (blankRow < dim - 1) {
			int[][] blocks = copyBoard();
			swap(blocks, blankRow, blankCol, blankRow + 1, blankCol);
			stack.push(new Board(blocks));
		}


		return stack;
	}

	public String toString() {
		return null; // string representation of this board (in the output format specified below)
	}

	private static class ByHamming implements Comparator<Board> {

		@Override
		public int compare(Board b1, Board b2) {
			int priority1 = b1.hamming() + b1.getMoves();
			int priority2 = b2.hamming() + b2.getMoves();

			int dPriority = priority1 - priority2;

			if (dPriority == 0) {
				return 0;
			} else if (dPriority > 0) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	private static class ByManhattan implements Comparator<Board> {

		@Override
		public int compare(Board b1, Board b2) {

			int priority1 = b1.manhattan() + b1.getMoves();
			int priority2 = b2.manhattan() + b2.getMoves();

			int dPriority = priority1 - priority2;

			if (dPriority == 0) {
				return 0;
			} else if (dPriority > 0) {
				return 1;
			} else {
				return -1;
			}

		}


	}

	private void swap(int[][] blocks, int i, int j, int k, int l) {
		int temp = blocks[i][j];
		blocks[i][j] = blocks[k][l];
		blocks[k][l] = temp;
	}

	private int[][] copyBoard() {
		int[][] boardCopy = new int[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				boardCopy[i][j] = boardBlocks[i][j];
			}
		}
		return boardCopy;
	}

	public static void main(String[] args) { // unit tests (not graded)

	}

	public Board getParentBoard() {
		return parentBoard;
	}

	public void setParentBoard(Board parentBoard) {
		this.parentBoard = parentBoard;
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}
}

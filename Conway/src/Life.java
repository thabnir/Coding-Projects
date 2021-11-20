import java.util.*;
public class Life {
	byte[][] cells;
	byte[][] nextBoard;
	int numRows; int numCols;
	int[][] neighBoard;
	// 1 is alive, 2 is dead
	// try langton's ant as well

	// constructor 
	public Life(int rows, int cols, double coverage) {
		numRows = rows;
		numCols = cols;
		cells = new byte[rows][cols];
		randomizeBoard(coverage);
		nextBoard = cells.clone();
		neighBoard = new int[numRows][numCols];
	}

	// methods
	public void randomizeBoard(double coverage) {
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
				cells[r][c] = (byte)(Math.random() * (1 + coverage)); //5% start as alive (1) 95% start as dead (0)
			}
		}
	}

	private void doNeighBoard() {
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
				if (cells[r-1][c-1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r-1][c] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r-1][c+1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r][c+1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r+1][c+1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r+1][c] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r+1][c-1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r][c-1] == 1) {
					neighBoard[r][c]++;
				}
			}
		}
	}

	public void doLogic() {
		doNeighBoard();
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
				if (cells[r][c] == 1 && (neighBoard[r][c] == 2 || neighBoard[r][c] == 3)) {
					cells[r][c] = 1; // survives
				} else if (cells[r][c] == 0 && neighBoard[r][c] == 4) {
					cells[r][c] = 1; // grows
				} else
					cells[r][c] = 0; // dies / stays dead
			}
		}
		// Any live cell with two or three live neighbors survives.
		// Any dead cell with three live neighbors becomes a live cell.
		// All other live cells die in the next generation. Similarly, all other dead cells stay dead.
	}

	public void doNextTick() {
		cells = nextBoard.clone();
	}

	public void printBoard(int[][] arr) {
		for (int row = 1; row < arr.length-1; row++) {
			for (int col = 1; col < arr[row].length-1; col++) {
				System.out.print(arr[row][col] + "  ");
			}
			System.out.println();
		}
	}
	public void printBoard(byte[][] arr) {
		for (int row = 1; row < arr.length-1; row++) {
			for (int col = 1; col < arr[row].length-1; col++) {
				System.out.print(arr[row][col]);
			}
			System.out.println();
		}
	}
	public byte[][] getBoard() {
		return cells;
	}
	public int[][] getNeighBoard() {
		return neighBoard;
	}
}
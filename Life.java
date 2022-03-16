import java.util.*;

public class Life {
	byte[][] cells;
	byte[][] nextBoard;
	int numRows;
	int numCols;
	byte[][] neighBoard;
	public static final String redge = "\u001B[31m";
	public static final String resetge = "\u001B[0m";
	public static final String greenge = "\u001B[32m";

	// 1 is alive, 2 is dead
	// try langton's ant as well

	// constructor
	public Life(int rows, int cols, double coverage) {
		numRows = rows;
		numCols = cols;
		cells = new byte[rows][cols];
		randomizeBoard(coverage);
		nextBoard = cells.clone();
		neighBoard = new byte[numRows][numCols];
	}

	public static void main(String[] args) {
		Life l = new Life(30, 30, .5);
		while (true) {
			l.printBoard(l.getNeighBoard());
			System.out.println();
			l.printBoard(l.getBoard());
			System.out.println();
			l.doLogic();
			l.doNextTick();
			try {
				Thread.sleep(1000 / 2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	// methods
	public void randomizeBoard(double coverage) {
		for (int r = 1; r < cells.length - 1; r++) {
			for (int c = 1; c < cells[r].length - 1; c++) {
				cells[r][c] = (byte) (Math.random() * (1 + coverage)); // 5% start as alive (1) 95% start as dead (0)
			}
		}
	}

	private void doNeighBoard() {
		for (int r = 0; r < neighBoard.length; r++) {
			for (int c = 0; c < neighBoard[r].length; c++) {
				neighBoard[r][c] = 0;
			}
		}
		for (int r = 1; r < cells.length - 1; r++) {
			for (int c = 1; c < cells[r].length - 1; c++) {
				if (cells[r - 1][c - 1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r - 1][c] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r - 1][c + 1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r][c + 1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r + 1][c + 1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r + 1][c] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r + 1][c - 1] == 1) {
					neighBoard[r][c]++;
				}
				if (cells[r][c - 1] == 1) {
					neighBoard[r][c]++;
				}
			}
		}
	}

	public void doLogic() {
		doNeighBoard();
		for (int r = 1; r < cells.length - 1; r++) {
			for (int c = 1; c < cells[r].length - 1; c++) {
				if (cells[r][c] == 1 && (neighBoard[r][c] == 2 || neighBoard[r][c] == 3)) {
					cells[r][c] = 1; // survives
				} else if (cells[r][c] == 0 && neighBoard[r][c] == 3) {
					cells[r][c] = 1; // grows
				} else
					cells[r][c] = 0; // dies / stays dead
			}
		}
		// Any live cell with two or three live neighbors survives.
		// Any dead cell with three live neighbors becomes a live cell.
		// All other live cells die in the next generation. Similarly, all other dead
		// cells stay dead.
	}

	public void doNextTick() {
		cells = nextBoard.clone();
	}

	public void printBoard(int[][] arr) {
		for (int row = 1; row < arr.length - 1; row++) {
			for (int col = 1; col < arr[row].length - 1; col++) {
				if (cells[row][col] == 1 && (neighBoard[row][col] == 2 || neighBoard[row][col] == 3)) {
					cells[row][col] = 1; // survives
				} else if (cells[row][col] == 0 && neighBoard[row][col] == 3) {
					cells[row][col] = 1; // grows
				} else
					System.out.print(redge + arr[row][col] + resetge + " ");
			}
			System.out.println();
		}
	}

	public void printBoard(byte[][] arr) {
		for (int row = 1; row < arr.length - 1; row++) {
			for (int col = 1; col < arr[row].length - 1; col++) {
				if (arr[row][col] == 1)
					System.out.print(redge + arr[row][col] + resetge + " ");
				if (arr[row][col] == 0)
					System.out.print(greenge + arr[row][col] + resetge + " ");
			}
			System.out.println();
		}
	}

	public byte[][] getBoard() {
		return cells;
	}

	public byte[][] getNeighBoard() {
		return neighBoard;
	}
}
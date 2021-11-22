import java.util.*;
public class Life {
	byte[][] cells;
	ArrayList<byte[][]> cellsHist = new ArrayList<byte[][]>();
	
	byte[][] nextBoard;
	byte[][] lastBoard;
	int numRows; int numCols;
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
		/* this is just an R-pentomino test pattern.
		// it works, so the code is accurate
							 cells[200][200] = 1; cells[200][201] = 1;
		cells[201][199] = 1; cells[201][200] = 1;
							 cells[202][200] = 1;
		*/
		nextBoard = cells.clone();
		lastBoard = cells.clone();
		neighBoard = new byte[numRows][numCols];
		cellsHist.add(cells);
		cellsHist.add(cells);
		cellsHist.add(cells);
		cellsHist.add(cells);
		cellsHist.add(cells);
	}
	/*
	public static void main(String[] args) {
		Life l = new Life(5,5,.5);
		while (true) {
			l.printBoard(l.getNeighBoard());
			System.out.println();
			l.printBoard(l.getBoard());
			System.out.println();
			l.doLogic();
			l.doNextTick();
			try {
				Thread.sleep(1000/2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	 */
	// methods
	public void randomizeBoard(double coverage) {
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
				cells[r][c] = (byte)(Math.random() * (1 + coverage)); //5% start as alive (1) 95% start as dead (0)
			}
		}
	}

	private void doNeighBoard() {
		// resets
		for (int r = 0; r < neighBoard.length; r++) {
			for (int c = 0; c < neighBoard[r].length; c++) {
				neighBoard[r][c] = 0;
			}
		}
		// gets neighbors
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
				if (cells[r-1][c-1] == 1)
					neighBoard[r][c]++;
				if (cells[r-1][c] == 1)
					neighBoard[r][c]++;
				if (cells[r-1][c+1] == 1)
					neighBoard[r][c]++;
				if (cells[r][c+1] == 1)
					neighBoard[r][c]++;
				if (cells[r+1][c+1] == 1)
					neighBoard[r][c]++;
				if (cells[r+1][c] == 1)
					neighBoard[r][c]++;
				if (cells[r+1][c-1] == 1)
					neighBoard[r][c]++;
				if (cells[r][c-1] == 1)
					neighBoard[r][c]++;
			}
		}
	}

	public void doLogic() {
		doNeighBoard();
		for (int r = 1; r < cells.length-1; r++) {
			for (int c = 1; c < cells[r].length-1; c++) {
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
		// All other live cells die in the next generation. Similarly, all other dead cells stay dead.
		cellsHist.add(cells);
	}

	public void doNextTick() {
		cells = nextBoard.clone();
	}

	public void printBoard(byte[][] arr) {
		for (int row = 1; row < arr.length-1; row++) {
			for (int col = 1; col < arr[row].length-1; col++) {
				if (arr[row][col] == 1)
					System.out.print(arr[row][col] + " ");
				if (arr[row][col] == 0)
					System.out.print(arr[row][col] + " ");
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
	public byte[][] getLastBoard() {
		return lastBoard;
	}
	public byte[][] getNextBoard() {
		return nextBoard;
	}
	public ArrayList<byte[][]> getCellsHist() {
		return cellsHist;
	}
	public void addCells(int row, int col) {
		if (row < cells.length && col < cells[row].length) {
			cells[row][col] = 1;
		} else {
			System.out.println("Out of bounds");
		}
	}
	public void removeCells(int row, int col) {
		if (row < cells.length && col < cells[row].length) {
			cells[row][col] = 0;
		} else {
			System.out.println("Out of bounds");
		}
	}
}
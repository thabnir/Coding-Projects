import java.util.Random;
public class BoardLogic {
	Cell[][] cells;
	// ArrayList<byte[][]> cellsHist = new ArrayList<byte[][]>();
	int numFlags = 0;
	int numCellsRevealed = 0;
	int numCorrectFlags = 0;
	int numBombs = 0;
	int maxFlags = numBombs;

	// win condition:
	// all unmined squares visible
	// OR all flags correct

	int numrs; int numcs;
	byte[][] neighBoard;
	boolean isLost = false;
	boolean isWon = false;
	boolean hasNewMove = false;

	public static final String redge = "\u001B[31m";
	public static final String resetge = "\u001B[0m";
	public static final String greenge = "\u001B[32m";

	// constructor 
	public BoardLogic(int r, int c, int coverage) {
		numrs = r;
		numcs = c;
		cells = new Cell[r][c];
		neighBoard = new byte[numrs][numcs];
		setBoard(coverage);
		doNeighBoard();
	}
	public BoardLogic (int r, int c) {
		numrs = r;
		numcs = c;
		cells = new Cell[r][c];
		neighBoard = new byte[numrs][numcs];
		setBoard(0);
	}

	// methods
	public void setBoard(int coverage) {
		for (int r = 0; r < cells.length; r++) {
			for (int c = 0; c < cells[r].length; c++) {
				cells[r][c] = new Cell(r, c);
				boolean value = new Random().nextInt(coverage)==0;
				if (value) {
					cells[r][c].addBomb(); // coverage is a percentage from 0 to 1, likelihood to have a bomb
					numBombs++;
				}
			}
		}
		maxFlags = numBombs;
	}

	public void checkWin() {
		if (numCorrectFlags == numBombs) {
			isWon = true;
			for (Cell[] rows : cells) {
				for (Cell seel : rows) {
					seel.win();
				}
			}
		}
	}
	
	public void toggleFlag(int r, int c) {
		boolean toggleOn = false;
		if (cells[r][c].isRevealed())
			return;
		if (!cells[r][c].hasFlag())
			toggleOn = true;

		if (numFlags < maxFlags || !toggleOn) {
			if (toggleOn) {
				numFlags++;
				if (cells[r][c].hasBomb) {
					numCorrectFlags++;
					checkWin();
				}
			} else {
				if (cells[r][c].hasBomb) {
					numCorrectFlags--;
				}
				numFlags--;
			}
			cells[r][c].toggleFlag();
		}
		hasNewMove = true;
	}

	public void checkCell(int r, int c) {
		if (cells[r][c].isRevealed())
			return;
		
		if (cells[r][c].hasBomb() && !cells[r][c].hasFlag()) {
			isLost = true;
			for (Cell[] rows : cells) {
				for (Cell seel : rows) {
					seel.lose();
					seel.reveal();
				}
			}
		} else if (!cells[r][c].hasFlag()) {
			// floodfill for tiles with no bomb
			cells[r][c].reveal();
			if (cells[r][c].show() == 0) {
				boolean topEdge = false; boolean botEdge = false;
				boolean leftEdge = false; boolean rightEdge = false;
				if (r == 0)
					topEdge = true;
				if (c == 0)
					leftEdge = true;
				if (c == cells[r].length-1)
					rightEdge = true;
				if (r == cells.length-1)
					botEdge = true;

				if (!topEdge) {
					if (!leftEdge) {
						checkCell(r-1,c-1);
					}
					checkCell(r-1,c);
					if (!rightEdge) {
						checkCell(r-1,c+1);
					}
				}
				if (!botEdge) {
					if (!rightEdge) {
						checkCell(r+1,c+1);
					}
					checkCell(r+1,c);
					if (!leftEdge) {
						checkCell(r+1,c-1);
					}
				}
				if (!leftEdge) {
					checkCell(r,c-1);
				}
				if (!rightEdge) {
					checkCell(r,c+1);
				}
			}
		}
	}
	public Cell getCell(int r, int c) {
		return cells[r][c];
	}

	public void doNeighBoard() {
		// resets
		for (int r = 0; r < neighBoard.length; r++) {
			for (int c = 0; c < neighBoard[r].length; c++) {
				neighBoard[r][c] = 0;
			}
		}
		// gets neighbors
		// something about botEdge and rightEdge is wrong

		boolean topEdge = true; boolean leftEdge = true;
		boolean botEdge = false; boolean rightEdge = false;
		for (int r = 0; r < cells.length; r++) {
			leftEdge = true;
			for (int c = 0; c < cells[r].length; c++) {
				if (c == cells[r].length-1) {
					rightEdge = true;
				} else {
					rightEdge = false;
				}
				if (r == cells.length-1) {
					botEdge = true;
				} else {
					botEdge = false;
				}

				if (!topEdge) {
					if (!leftEdge) {
						if (cells[r-1][c-1].hasBomb())
							neighBoard[r][c]++;
					}
					if (cells[r-1][c].hasBomb())
						neighBoard[r][c]++;
					if (!rightEdge) {
						if (cells[r-1][c+1].hasBomb())
							neighBoard[r][c]++;
					}
				}
				if (!botEdge) {
					if (!rightEdge) {
						if (cells[r+1][c+1].hasBomb())
							neighBoard[r][c]++;
					}
					if (cells[r+1][c].hasBomb())
						neighBoard[r][c]++;
					if (!leftEdge) {
						if (cells[r+1][c-1].hasBomb())
							neighBoard[r][c]++;
					}
				}
				if (!leftEdge) {
					if (cells[r][c-1].hasBomb())
						neighBoard[r][c]++;
				}
				if (!rightEdge) {
					if (cells[r][c+1].hasBomb())
						neighBoard[r][c]++;
				}
				cells[r][c].setNeighbors(neighBoard[r][c]);
				leftEdge = false;
			}
			topEdge = false;
		}
	}

	public void printBoard() {
		Character cellVal;
		for (int r = 0; r < cells.length; r++) {
			for (int c = 0; c < cells[r].length; c++) {
				System.out.print(cells[r][c].show() + " ");
			}
			System.out.println();
		}
	}
	public Cell[][] getBoard() {
		return cells;
	}
	public void setBoard(Cell[][] borda) {
		cells = borda.clone();
	}
	public boolean isWon() {
		return isWon;
	}
	public boolean isLost() {
		return isLost;
	}
	public byte[][] getNeighBoard() {
		return neighBoard;
	}
	public int getNumRows() {
		return numrs;
	}
	public int getNumCols() {
		return numcs;
	}
	/*
	public ArrayList<byte[][]> getCellsHist() {
		return cellsHist;
	}
	 */
}
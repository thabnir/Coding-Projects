
public class Cell {
	// class data
	boolean isRevealed = false;
	boolean isLost = false;
	boolean isWon = false;
	boolean hasBomb = false;
	boolean hasFlag = false;
	int row; int col;
	byte numNeighbors;

	// constructor
	public Cell(int r, int c) {
		row = r;
		col = c;
	}
	// methods
	public byte show() {
		if (hasFlag)
			return -3; // if has flag, return -3
		if (!isRevealed)
			return -2; // if not revealed or flagged, return -2
		if (hasBomb)
			return -1; // if has bomb and is revealed, return -1
		
		//System.out.println(numNeighbors);
		return numNeighbors; // if revealed with no bomb, return number of neighbors
	}
	public void setNeighbors(byte n) {
		numNeighbors = n;
	}
	public boolean isRevealed() {
		return isRevealed;
	}
	public boolean hasBomb() {
		return hasBomb;
	}
	public boolean hasFlag() {
		return hasFlag;
	}
	public void addBomb() {
		hasBomb = true;
	}
	public void toggleFlag() {
		if (!isRevealed)
			hasFlag = !hasFlag;
	}
	public void lose() {
		isLost = true;
	}
	public void win() {
		isWon = true;
	}
	public void reveal() {
		isRevealed = true;
	}
}
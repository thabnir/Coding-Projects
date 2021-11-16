import java.util.*;
import java.awt.*;
import java.security.KeyPair;
public class BoardLogic {
	char[][] tiles; // contains the board's data
	int boardSize;  // dimensions of the board (square)
	int numTiles; int numToWin;
	int longestStreak = 0; int piecesPlaced = 0; // for ties
	int lastRow; int lastCol; // the row and column of the most recently placed piece
	int aiRow; int aiCol;
	char p1Symbol = 'X'; String p1Name = "Player 1";
	char p2Symbol = 'O'; String p2Name = "Player 2";
	char currentTurn = p1Symbol;
	boolean isValidMove;
	boolean hasWinningMove;

	// constructor
	public BoardLogic(int n) {
		boardSize = n;
		if (boardSize == 3) 
			numToWin = 3;
		else if (boardSize >= 4 && boardSize <= 7)
			numToWin = 4;
		else if (boardSize > 7)
			numToWin = 5;
		else
			numToWin = boardSize;
		numTiles = boardSize * boardSize;
		tiles = new char[n][n];
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				tiles[y][x] = ' '; // fills each row column with spaces
			}
		}
	}
	// methods

	// printBoardArray (works but looks really crappy)
	public void printBoardArray() {
		System.out.println(Arrays.deepToString(tiles).replace("], ", "]\n").replace("[[","[").replace("]]","]"));
	}

	//for old 1d array Tic Tac Toe
	public void setBoardState(char[] b) {
		int count = 0;
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				tiles[y][x] = b[count];
				count++;
			}
		}
	}

	// sets it using a 2d array
	public void setBoardState(char[][] b) {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++){
				tiles[row][col] = b[row][col];
			}
		}
	}

	// move maker, won't make an illegal move.
	public void setPieceOn(int row, int column, char symbol) {
		if (row >= boardSize || row < 0 || column >= boardSize || column < 0 || tiles[row][column] != ' ') {
			setValidMove(false); // does nothing if move is out of bounds or already has a piece
		} else {
			setValidMove(true);
			tiles[row][column] = symbol; // sets a piece
			lastRow = row; lastCol = column;
			piecesPlaced++;
		}
	}

	// win checker for normal games (i'm pretty sure it works, there might be some bugs hidden in there)
	public char checkWin(char symbol) {
		int streak = 0; int colAt = 0; int rowAt = 0;
		// checks horizontally across lastRow
		while (colAt < boardSize) {
			while (colAt < boardSize && tiles[lastRow][colAt] == symbol) {
				streak++; colAt++; // while it is a match, keep iterating and add 1 to the streak
				if (longestStreak <= streak) {
					longestStreak = streak; // if the current streak is longer than the max streak, set the max streak equal to the current streak
					if (longestStreak >= numToWin)
						return symbol; // player 1 wins
				}
			}
			if (colAt < boardSize)
				colAt++; // while it's not a match, keep iterating
			streak = 0;
		}
		colAt = 0; rowAt = 0; // reset for next check

		// checks vertically down lastCol
		while (rowAt < boardSize) {
			while (rowAt < boardSize && tiles[rowAt][lastCol] == symbol) {
				streak++; rowAt++; // while it is a match, keep iterating and add 1 to the streak
				if (longestStreak <= streak) {
					longestStreak = streak; // if the current streak is longer than the max streak, set the max streak equal to the current streak
					if (longestStreak >= numToWin)
						return symbol; // player 1 wins
				}
			}
			if (rowAt < boardSize)
				rowAt++; // while it's not a match, keep iterating
			streak = 0;
		}
		colAt = 0; rowAt = 0; // reset for next check

		int diagRowR = lastRow; int diagColR = lastCol; int diagRowL = lastRow; int diagColL = lastCol;
		while (diagRowR > 0 && diagColR > 0) {diagRowR--; diagColR--;} // checks to the left edge to start from when going down + right
		while (diagRowL > 0 && diagColL < boardSize-1) {diagRowL--; diagColL++;} // checks to the right edge to start from when going down + left
		// checks diagonally down right
		while (diagRowR < boardSize && diagColR < boardSize) {
			while (diagRowR < boardSize && diagColR < boardSize && tiles[diagRowR][diagColR] == symbol) {
				streak++; diagRowR++; diagColR++;
				if (longestStreak <= streak) {
					longestStreak = streak; // if the current streak is longer than the max streak, set the max streak equal to the current streak
					if (longestStreak >= numToWin)
						return symbol; // player 1 wins
				}
			}
			if (diagRowR < boardSize && diagColR < boardSize) {
				diagRowR++; diagColR++; // while it's not a match, keep iterating
			}
			streak = 0;
		}
		rowAt = 0; colAt = boardSize;
		// checks diagonally down left
		while (diagRowL < boardSize && diagColL >= 0) {
			while (diagRowL < boardSize && diagColL >= 0 && tiles[diagRowL][diagColL] == symbol) {
				streak++; diagRowL++; diagColL--;
				if (longestStreak <= streak) {
					longestStreak = streak; // if the current streak is longer than the max streak, set the max streak equal to the current streak
					if (longestStreak >= numToWin)
						return symbol; // player 1 wins
				}
			}
			if (diagRowL < boardSize && diagColL < boardSize) {
				diagRowL++; diagColL--; // while it's not a match, keep iterating
			}
			streak = 0;
		}

		if (piecesPlaced == numTiles)
			return 't'; // tie (if there's a winner it'll still return the correct one)
		return 'n'; // no winner found
	}

	// insert a board and a symbol to look for and it'll see if that symbol won
	public char staticWinCheck(char[][] b, char symbol) {
		int n = b.length; int streak = 0;
		int wincon;
		if (n <= 4)
			wincon = n;
		else if (n > 4 && n <= 7)
			wincon = 4;
		else
			wincon = 5;

		// checks vertically
		for (int c = 0; c < n; c++) {
			int r = 0;
			while (r < n) {
				while (r < n && b[r][c] == symbol) {
					r++; streak++; // checks vertically
					if (streak >= wincon)
						return symbol;
				}
				streak = 0;
				r++;
			}
		}
		// checks horizontally
		for (int r = 0; r < n; r++) {
			int c = 0;
			while (c < n) {
				while (c < n && b[r][c] == symbol) {
					c++; streak++; // checks horizontally
					if (streak >= wincon)
						return symbol;
				}
				streak = 0;
				c++;
			}
		}
		// checks down right diagonally
		for (int r = 0; r < n; r++) {
			int c = 0;
			while (c < n) {
				while (c < n && r < n && b[r][c] == symbol) {
					c++; r++; streak++; // checks down right diagonally
					if (streak >= wincon)
						return symbol;
				}
				streak = 0;
				c++;
			}
		}
		// checks up right diagonally
		for (int r = n-1; r >= 0; r--) {
			int c = 0;
			while (c < n) {
				while (c < n && r >= 0 && b[r][c] == symbol) {
					c++; r--; streak++; // checks down right diagonally
					if (streak >= wincon)
						return symbol;
				}
				streak = 0;
				c++;
			}
		}
		// checks for no winner yet
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				if (b[r][c] == ' ')
					return 'n'; // no win
			}
		}
		return 't'; // tie
	}

	public void findWinningMove(BoardLogic sus, char symbol) {
		BoardLogic b = new BoardLogic(sus.getSize());
		b.setBoardState(sus.getBoardState());
		for (int r = 0; r < b.getSize(); r++) {
			for (int c = 0; c < b.getSize(); c++) {
				if (b.getTileState(r,c) == ' ') {
					b.setPieceOn(r, c, symbol);
					if (b.checkWin(symbol) == symbol) {
						aiRow = r;
						aiCol = c;
						hasWinningMove = true;
						return;
					}
				}
			}
		}
		hasWinningMove = false;
	}

	public void algorithmShenanagins() {
		// do the algorithm here
		// see if winning move is present, if not, try first move, that's 1st node (can do pruning stuff)
		// test to see if node has wins, then test to see if node of node has wins, so on to depth n
		// if node has no wins by depth n, pull out and try second move, continue pattern
	}

	public char[][] getBoardState() {
		return tiles;
	}
	public void setValidMove(boolean b) {
		isValidMove = b;
	}
	public boolean getValidMove() {
		return isValidMove;
	}
	public int getAIRow() {
		return aiRow;
	}
	public int getAIColumn() {
		return aiCol;
	}
	public int getLongestStreak() {
		return longestStreak;
	}
	public char getP1Symbol() {
		return p1Symbol;}
	public char getP2Symbol() {return p2Symbol;
	}
	public String getPlayerName() {
		if (currentTurn == p1Symbol) {
			return p1Name;
		} else
			return p2Name;
	}
	public int getSize() {
		return boardSize;
	}
	public char getTileState(int row, int col) {
		return tiles[row][col];
	}
	public void setTileState(int row, int col, char c) {
		tiles[row][col] = c;
	}
	public boolean hasWinningMove() {
		return hasWinningMove;
	}
	public void changeTurns() {
		currentTurn = getOtherSymbol(currentTurn);
	}
	public char getCurrentTurn() {
		return currentTurn;
	}
	public char getOtherSymbol(char sym) {
		if (sym == p1Symbol)
			return p2Symbol;
		else if (sym == p2Symbol)
			return p1Symbol;
		else
			return ' ';
	}
}

/*
should make a list of all empty tiles so that each time the ai iterates through all available moves it doesn't waste time checking illegal moves. for now it checks all of them, though
minimax
    + alpha-beta pruning for speed

make a perfect ai for the game, watch it face itself on a big tic tac toe board (turns out this is really difficult, 15x15 board has 3^(15x15) possible states (so comically large that a computer cries at the thought)
hey but it doesn't have that many options for a 3x3 game at least

https:// en.wikipedia.org/wiki/Tic-tac-toe_variants
oh shit there's a part of the article on the magic square version that i made before
no ideas are original i guess
called an "isomorphic variant" before it gets mapped to a magic square
 */

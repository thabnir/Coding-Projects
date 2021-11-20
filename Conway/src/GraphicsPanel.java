import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class GraphicsPanel extends JPanel {
	int numRows; int numCols;
	int tileSize; int height; int width;
	byte[][] board;
	byte[][] oldBoard;
	byte[][] oldestBoard;
	Color[][] tileColors;
	int pop;
	int red = 0;
	int green = 0;
	int blue = 0;
	public GraphicsPanel(Dimension size, byte[][] b) {
		board = b.clone();
		oldBoard = b.clone();
		oldestBoard = b.clone();
		numRows = b.length;
		numCols = b[0].length;
		this.setPreferredSize(size);
		this.setBackground(Color.white);
		this.setFocusable(true);
		tileColors = new Color[numRows][numCols];
		
		for (int r = 1; r < numRows-1; r++) {
			for (int c = 1; c < numCols-1; c++) {
				tileColors[r][c] = (new Color(0,0,0));
			}
		}
		
		resize();
	}
	
	public void resize() {
		height = this.getHeight();
		width = this.getWidth();
		if (this.getWidth() > this.getHeight()) {
			tileSize = this.getHeight()/numRows;
		} else {
			tileSize = this.getWidth()/numCols;
		}
		//System.out.println("tilesize: " + tileSize);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pop = 0;
		for (int r = 1; r < numRows-1; r++) {
			for (int c = 1; c < numCols-1; c++) {
				if (board[r][c] == 1) {
					pop++;
					g.setColor(tileColors[r][c]);
					g.fillRect((int) (c * tileSize),(int) (r * tileSize), (int)tileSize, (int)tileSize);
				}
			}

		}
		repaint();
	}
	public int getPop() {
		return pop;
	}
	
	public void setTileColor(int[][] brd) {
		int n ;
		for (int col = 1; col < numCols-1; col++) {
			for (int row = 1; row < numRows-1; row++) {
				n = brd[row][col];
				if (n < 2 || n > 3) {
					tileColors[row][col] = Color.red;
				} else if (oldBoard[row][col] == 0) {
					tileColors[row][col] = Color.green;
				} else {
					tileColors[row][col] = Color.black;
				}
			}
		}
	}

	public void refreshTiles(byte[][] b) {
		oldestBoard = oldBoard.clone(); oldBoard = board.clone(); board = b.clone();
	}
	
}

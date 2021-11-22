import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

public class GraphicsPanel extends JPanel implements MouseInputListener {
	int numRows; int numCols;
	int tileSize; int height; int width;
	byte[][] board;
	byte[][] lastBoard;
	byte[][] oldestBoard;
	Color[][] bgColors;
	Color[][] tileColors;
	

	int red = 0;
	int green = 0;
	int blue = 0;
	public GraphicsPanel(Dimension size, byte[][] b) {
		board = b.clone();
		numRows = b.length;
		numCols = b[0].length;
		this.setPreferredSize(size);
		this.setBackground(Color.white);
		this.setFocusable(true);
		tileColors = new Color[numRows][numCols];
		bgColors = new Color[numRows][numCols];

		for (int r = 1; r < numRows-1; r++) {
			for (int c = 1; c < numCols-1; c++) {
				tileColors[r][c] = (new Color(255,255,255));
				bgColors[r][c] = (new Color(0,0,0));
			}
		}

		resize();
	}

	public void resize() {
		height = this.getHeight();
		width = this.getWidth();
		if (this.getWidth() > this.getHeight()) {
			tileSize = (int)this.getHeight()/numRows;
		} else {
			tileSize = (int)this.getWidth()/numCols;
		}
		//System.out.println("tilesize: " + tileSize);
		//System.out.println(this.getHeight() + "h" + this.getWidth() + "w");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int r = 1; r < numRows-1; r++) {
			for (int c = 1; c < numCols-1; c++) {
				if (board[r][c] == 1) {
				g.setColor(tileColors[r][c]);
				//g.setColor(Color.white);
				} else {
					g.setColor(bgColors[r][c].darker().darker());
					//g.setColor(Color.black);
				}
				g.fillRect((int) (c * tileSize),(int) (r * tileSize), (int)tileSize, (int)tileSize);
			}
		}
		repaint();
	}

public void setBGColor() {
	double red; double green; double blue;
	for (int col = 1; col < numCols-1; col++) {
		for (int row = 1; row < numRows-1; row++) {
			red = Math.random()*col / numCols * 256;
			blue = Math.random()*row / numRows * 256;
			green = 0;
			bgColors[row][col] = new Color((int)Math.abs(red)%256, (int)Math.abs(green)%256, (int)Math.abs(blue)%256);
		}
	}
}
	public void setTileColor(byte[][] neighBoard, ArrayList<byte[][]> hist) {
		// it only shows the tiles that end up being green
		// so it only has tiles with n 2 or 3
		// so it only has tiles that specifically survived from last round
		// no newly grown tiles. the data is old by 1 turn.
		// aaaa
		int n;
		byte thisT;
		byte lastT;
		for (int col = 1; col < numCols-1; col++) {
			for (int row = 1; row < numRows-1; row++) {
				n = neighBoard[row][col];
				//thisT = (hist.get(hist.size()-1))[row][col];
				//lastT = (hist.get(hist.size()-2))[row][col];
				if ((hist.get(hist.size()-1))[row][col] == 1) {
					if (n == 1) {
						tileColors[row][col] = Color.blue;
					} else if (n == 2) {
						tileColors[row][col] = Color.green;
					} else if (n == 3) {
						tileColors[row][col] = Color.green;
					} else if (n == 4) {
						tileColors[row][col] = Color.white;
					} else if (n == 5) {
						tileColors[row][col] = Color.red.brighter();
					} else if (n == 6) {
						tileColors[row][col] = Color.gray.brighter().brighter();
					} else if (n == 7) {
						tileColors[row][col] = Color.gray.brighter().brighter().brighter();
					} else if (n == 8) {
						tileColors[row][col] = Color.white;
					}
				} else {
					tileColors[row][col] = Color.black; // it dead
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("hi");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
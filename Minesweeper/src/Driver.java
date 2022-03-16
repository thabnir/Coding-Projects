import java.awt.*;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
public class Driver {
	public static void main(String[] args) {
		final int NUM_ROWS = 10;
		final int NUM_COLS = 10; // not sure if breaks if diff
		
		final int COVERAGE = 6;
		// (1 / COVERAGE) chance of having a mine on a given tile
		
		final int FPS = 144;

		Dimension size = new Dimension(900, 900);
		JFrame frame = new JFrame("Minesweeper");
		BoardLogic board = new BoardLogic(NUM_ROWS, NUM_COLS, COVERAGE);
		GraphicsPanel panel = new GraphicsPanel(size, board.getBoard());
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int nextRow = -1; int nextCol = -1;

		panel.resize();
		panel.repaint();
		// Game loop (need to add player count select and play again options)
		while (true) {
			if (panel.isClicked()) {
				nextRow = panel.getRowClicked();
				nextCol = panel.getColClicked();
				if (nextRow != -1 && nextCol != -1) {
					if (panel.isLeftClick) {
						board.checkCell(nextRow, nextCol);
					}
					if (panel.isRightClick) {
						board.toggleFlag(nextRow, nextCol);
					}
				} else {
					System.out.println("invalid move");
				}
				panel.repaint(); panel.resize();
				panel.isClicked = false;
				panel.isRightClick = false;
				panel.isLeftClick = false;
			}
			panel.resize();
			try {
				Thread.sleep((int)(1000/FPS));
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
	}
}

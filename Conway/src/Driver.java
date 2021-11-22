import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
public class Driver {
	public static void main(String[] args) {
		final int NUM_ROWS = 466;
		final int NUM_COLS = 851;
		final double COVERAGE = 0.05;
		final double TPS = 500; // ticks per second

		Life gameOfLife = new Life(NUM_ROWS, NUM_COLS, COVERAGE);
		Dimension size = new Dimension(2555, 1400);
		JFrame frame = new JFrame("The Game of Life");
		GraphicsPanel panel = new GraphicsPanel(size, gameOfLife.getBoard());
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBGColor();
		panel.resize();
		panel.setTileColor(gameOfLife.getNeighBoard(), gameOfLife.getCellsHist());
		panel.setBGColor();
		int i = 0;
		while (true) {
			i++;
			panel.resize();
			gameOfLife.doLogic();
			panel.setTileColor(gameOfLife.getNeighBoard(), gameOfLife.getCellsHist());
			if (i%(TPS/10) == 0)
			// panel.setBGColor();
			gameOfLife.doNextTick();
			try {
				Thread.sleep((int)(1000/TPS));
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
	}
}
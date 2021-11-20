import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
public class Driver {
	public static void main(String[] args) {
		final int NUM_ROWS = 100;
		final int NUM_COLS = 100;
		final double COVERAGE = .05;
		final double TPS = 30; // ticks per second

		Life gameOfLife = new Life(NUM_ROWS, NUM_COLS, COVERAGE);
		Dimension size = new Dimension(800, 800);
		JFrame frame = new JFrame("The Game of Life");
		GraphicsPanel panel = new GraphicsPanel(size, gameOfLife.getBoard());
		ArrayList<Integer> populations = new ArrayList<Integer>();
		populations.add(-1);
		populations.add(-2);
		populations.add(-3);
		populations.add(-4);
		populations.add(-5);
		populations.add(-6);
		populations.add(-7);

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.resize();

		while (true) {
			//if (populations.size()-1!=populations.size()-2 && 
			//	populations.size()-2!=populations.size()-3) {
				
				panel.setTileColor(gameOfLife.getNeighBoard());
				panel.resize();
				panel.refreshTiles(gameOfLife.getBoard());
				gameOfLife.doLogic();
				gameOfLife.doNextTick();
				populations.add(panel.getPop());
				try {
					Thread.sleep((int)(1000/TPS));
				}
				catch(Exception e) {
					System.out.println(e);
				}
			//System.out.println(populations.size()-1);
			//}
			//gameOfLife.randomizeBoard(COVERAGE);
		}
	}
}
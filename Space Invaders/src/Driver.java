import java.awt.Dimension;

import javax.swing.JFrame;

public class Driver {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Space Invaders");
		Dimension size = new Dimension(1200, 800);
		GraphicsPanel panel = new GraphicsPanel(size);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (panel.isPlaying()) {
			panel.refresh();
			try { Thread.sleep(1000/200); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}
}

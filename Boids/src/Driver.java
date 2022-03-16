import java.awt.Dimension;

import javax.swing.JFrame;

public class Driver {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Boids");
		Dimension size = new Dimension(900, 980);
		GraphicsPanel panel = new GraphicsPanel(size);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // centers it
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (true) {
			panel.refresh();
			try {
				Thread.sleep(1000 / 200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

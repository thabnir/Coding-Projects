import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Driver {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Wordle");
		Dimension size = new Dimension(900, 980);
		GraphicsPanel panel = new GraphicsPanel(size);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panel.refresh();
		panel.resize();

		panel.w.printIt(panel.w.correctWord);

		while (true) {

			// panel.resize();
			if (panel.keyTyped) {
				panel.refresh();
				panel.keyTyped = false;
			}
			try {
				Thread.sleep(1000 / 200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

import java.awt.*;
import javax.swing.*;
public class Driver {
	public static void main(String[] args) {
		Dimension size = new Dimension(600,300);
		
		JFrame frame = new JFrame("My First GUI");
		GraphicsPanel panel = new GraphicsPanel(size);
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

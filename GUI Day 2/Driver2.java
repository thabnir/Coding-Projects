import java.awt.*;
import javax.swing.*;
public class Driver2 {
	public static void main(String[] args) {
		Dimension size = new Dimension(800, 800);
		
		JFrame frame = new JFrame("Susser");
		GraphicsPanel panel = new GraphicsPanel(size);
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Animation loop
		while(true) {
			try {
				Thread.sleep(1000/60); //fps
			}
			catch(Exception e) {
				System.out.println(e);
			}
			
			panel.refresh();
		}
	}
}

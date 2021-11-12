import java.awt.*;
import javax.swing.*;
public class Driver2 {
	Dimension size - new Dimension(800,800)
	Jframe frame = new JFrame("GUIs Day II");
	GraphicsPanel panel = new GraphicsPanel(size);
	
	frame.getContentPane().add(panel);
	frame.pack();
	frame.setResisable(true);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	
}

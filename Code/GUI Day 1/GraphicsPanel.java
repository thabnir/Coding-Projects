import java.awt.*;
import javax.swing.*;

public class GraphicsPanel extends JPanel {
	
	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.pink);
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(242, 23, 23));
		g.fillOval(275, 100, 75, 140);
		g.setColor(new Color(202, 23, 23));
		g.fillOval(300, 200, 50, 50);
		g.setColor(Color.white);
		g.fillOval(310, 120, 30, 20);
		
		g.setColor(new Color(222, 23, 23));
		//g.fillOval(300, 150, 50, 50);
		g.fillOval(300, 175, 800, 50);
		
		g.setColor(Color.black);
		g.drawString("Amongus with cock", 350, 175);
	}
}

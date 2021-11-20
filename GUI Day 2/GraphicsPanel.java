import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphicsPanel extends JPanel implements KeyListener{
	
	int boxX = 0;
	int boxY = 0;
	int boxXVelocity = 0;
	int boxYVelocity = 0;
	int speed = 13;
	
	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.pink);
		this.setFocusable(true);
		this.addKeyListener(this);
	}
	public void refresh() {
		repaint();
		boxX = boxX + boxXVelocity;
		boxY = boxY + boxYVelocity;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.fillRect(boxX,boxY,100,100); //x, y, width, height
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
		if (e.getKeyCode()==83) //w
			boxYVelocity = speed;
		if (e.getKeyCode()==87) //s
			boxYVelocity = -speed;
		
		if (e.getKeyCode()==65) //a
			boxXVelocity = -speed;
		if (e.getKeyCode()==68) //d
			boxXVelocity = speed;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==83 || e.getKeyCode()==87) //w/s
			boxYVelocity=0;
		if (e.getKeyCode()==65 || e.getKeyCode()==68) //a/d
			boxXVelocity = 0;
	}
}

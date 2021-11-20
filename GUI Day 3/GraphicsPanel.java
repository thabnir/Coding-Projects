import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GraphicsPanel extends JPanel implements KeyListener{

	//where tf do i put this chunk of code
	/*
	int numBalls = 5;
	Ball balls[];
	balls = new Ball[numBalls];
	for (int i = 0; i < numBalls; i++) {
		balls[i] = new Ball( (int)Math.random()*this.getWidth(), (int)Math.random()*this.getHeight(), (int)(Math.random()*200)+1 );
	}
	*/
	//where does it go??
	Ball oneBall = new Ball(200,200,50);
	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.pink);
		this.setFocusable(true);
		this.addKeyListener(this);
	}

	
	public void refresh() {
		repaint();
		//for (int i = 0; i < numBalls; i++) {
			if (oneBall.getX() <= 0)
				oneBall.setXVel(-oneBall.getXVel());

			if (oneBall.getY() <= 0)
				oneBall.setYVel(-oneBall.getYVel());

			if (oneBall.getX() + oneBall.getDiameter() >= this.getWidth())
				oneBall.setXVel(-oneBall.getXVel());

			if (oneBall.getY() + oneBall.getDiameter() >= this.getHeight())
				oneBall.setYVel(-oneBall.getYVel());
			
			oneBall.move();
		//}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		oneBall.paintSelf(g);
	}
	public void keyTyped(KeyEvent e) {

	}
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

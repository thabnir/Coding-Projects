import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener{
	
	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.pink);
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.gray);
		g.setColor(Color.white);
		g.fillRoundRect(250,150,50,200,10,10);
		g.fillRoundRect(340,150,50,200,10,10);
		g.setColor(Color.green);
		g.fillRoundRect(230,150,180,50,10,10);
		g.setColor(Color.pink);
		g.fillRoundRect(230,330,180,130,40,40);
		g.setColor(Color.black);
		g.fillRoundRect(180,400,280,200,50,50);
		

		//x, y, width, height, arcWidth, arcHeight
	}
	public void refresh() {
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel implements KeyListener {
	double width;
	double height;

	// STYLE ELEMENTS:
	boolean hasAntiAliasing = true;

	// END STYLE ELEMENTS

	GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(this);
		resize();
		System.setProperty("awt.useSystemAAFontSettings", "on"); // check if  this is necessary or not
	}

	void resize() {
		if (width != this.getWidth() || height != this.getHeight()) {
			width = this.getWidth();
			height = this.getHeight();
		}
	}

	void refresh() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		if (hasAntiAliasing) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		super.paintComponent(g);
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
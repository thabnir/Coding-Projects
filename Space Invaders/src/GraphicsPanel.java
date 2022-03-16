import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	double width;
	double height;
	ArrayList<Alien> aliens;
	ArrayList<Bullet> bullets;
	Player player;
	boolean isPlaying = true;
	int greenAlienSize = 100;
	int blueAlienSize = 100;
	int bulletSize = 50;
	int waveOn = 1;
	int alienSpacing = 50;
	int hitDmg = 5;
	int maxHealth = 10;
	int startingBullets = 50;
	int bulletsPerWave = 10;
	Integer ammoShown = -1;
	long timeSinceLastWave = System.currentTimeMillis();

	String loseMessage = "Lmao you suck";
	int FONT_SIZE = 50;
	Font myFont = new Font("Comic Sans MS", Font.BOLD, 50);

	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		width = size.getWidth();
		height = size.getHeight();

		player = new Player((int)width/2, (int)(height- 150), 100, 100);
		player.setHealth(maxHealth);
		player.setNumBullets(startingBullets);

		aliens = new ArrayList<Alien>();
		bullets = new ArrayList<Bullet>();	

	}

	public void refresh() {
		if (isPlaying) {
			if (aliens.size() == 0) {
				startNewWave(waveOn + 3, (int)waveOn/5);
				waveOn++;
				player.setNumBullets(player.getNumBullets() + bulletsPerWave);
			}
			player.move();
			for (int i = bullets.size() - 1; i >= 0; i--) {
				bullets.get(i).move();
				if (bullets.get(i).getY() < 0)
					bullets.remove(i);
			}
			for (int i = aliens.size() - 1; i >= 0; i--)
				aliens.get(i).move((int)width);
			for (int i = aliens.size() - 1; i >= 0; i--) {
				if (player.contains(aliens.get(i))) {
					player.setHealth(player.getHealth() - hitDmg); // player collides with alien
					aliens.remove(i); // test to see if this is good or not
				}
			}
			for (int i = aliens.size() - 1; i >= 0; i--) {
				if (aliens.get(i).getY() >= height) {
					player.setHealth((player.getHealth() - hitDmg)); // alien reaches the bottom
					aliens.remove(i);
				}
			}
			for (int i = aliens.size() - 1; i >= 0; i--) {
				for (int j = bullets.size() - 1; j >= 0; j--) {
					if (aliens.size() > i) {
						if (aliens.get(i).contains(bullets.get(j))) {
							aliens.get(i).receiveDamage(bullets.get(j).getDamage()); // alien gets shot
							bullets.remove(j);
							if (aliens.get(i).getHealth() <= 0)
								aliens.remove(i);
						}
					}
				}
			}
			if (player.getHealth() <= 0) {
				gameOver();
				this.getGraphics().setColor(Color.red);
				this.getGraphics().fillRect(0,0,(int)width,(int)height);
			}
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Alien a : aliens)
			a.show(g);
		for (Bullet b : bullets)
			b.show(g);
		player.show(g);

		ammoShown = player.getNumBullets();
		g.setFont(myFont);
		g.setColor(Color.red);
		g.drawString(ammoShown.toString(), 10, (int)height - 10);

		if (System.currentTimeMillis() - timeSinceLastWave < 3000) {
			waveOn = waveOn - 1;
			g.setFont(myFont);
			g.setColor(Color.white);
			g.drawString("Wave " + waveOn,
					this.getWidth() / 2 - g.getFontMetrics().stringWidth("Wave : " + waveOn) / 2,
					this.getHeight() / 2 - FONT_SIZE/4);
			waveOn = waveOn + 1;
		}
		if (!isPlaying) { 
			g.setColor(Color.WHITE);
			g.fillRect(0,this.getHeight() / 2 - (int)(FONT_SIZE * 1.5), this.getWidth(), (int)(FONT_SIZE * 1.5));
			g.setFont(myFont);
			g.setColor(Color.blue);
			g.drawString(loseMessage,
					this.getWidth() / 2 - g.getFontMetrics().stringWidth(loseMessage) / 2,
					this.getHeight() / 2 - FONT_SIZE/4);
		}
	}

	public void shoot() {
		if (player.getNumBullets() > 0) {
			bullets.add(new Bullet(player.getX() + player.getWidth()/2 - bulletSize/2, player.getY(), 0, 5, bulletSize, bulletSize));
			player.setNumBullets(player.getNumBullets() - 1);
		}
	}

	public void gameOver() {
		isPlaying = false;
		// do some other stuff, i guess
	}

	public void startNewWave(int numGreens, int numBlues) {
		// aliens start on top left off-screen moving right
		// blue aliens spawn first in a wave with both types (they're faster)
		timeSinceLastWave = System.currentTimeMillis();
		bulletsPerWave = numGreens * 3 + numBlues * 13;
		for (int i = 0; i < numBlues; i++) {
			aliens.add(new BlueAlien(-i * blueAlienSize - i*alienSpacing, 0, blueAlienSize, blueAlienSize, 0, (int)width));
		}
		for (int i = 0; i < numGreens; i++) {
			aliens.add(new GreenAlien((-i - numBlues-1) * greenAlienSize - i*alienSpacing, 0, greenAlienSize, greenAlienSize, 0, (int)width));
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		shoot();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ' && player.getNumBullets() > 0)
			shoot();
		if (e.getKeyChar() == 'a')
			player.xVel = -player.speed;
		if (e.getKeyChar() == 'd')
			player.xVel = player.speed;
		if (e.getKeyChar() == 'w')
			player.yVel = -player.speed;
		if (e.getKeyChar() == 's')
			player.yVel = player.speed;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == 'a')
			player.xVel = 0;
		if (e.getKeyChar() == 'd')
			player.xVel = 0;
		if (e.getKeyChar() == 'w')
			player.yVel = 0;
		if (e.getKeyChar() == 's')
			player.yVel = 0;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// change the angle at which you're firing to follow the mouse
	}

	boolean isPlaying() {
		return isPlaying;
	}

	// required but unused methods:
	@Override
	public void keyTyped(KeyEvent e) { }
	@Override
	public void mouseDragged(MouseEvent e) { }
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
}

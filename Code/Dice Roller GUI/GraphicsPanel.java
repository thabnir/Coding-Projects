import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GraphicsPanel extends JPanel implements KeyListener{

	final int NUM_DICE = 30; final int MAX_VEL = 20;
	final int MIN_SIZE = 5; final int MAX_VARIANCE = 200;
	boolean hasGravity = false;
	double gravity = 1;
	double bounciness = .96;
	boolean hasHitSound = false;
	boolean hasRollSound = false;
	double hitSoundThresh = MAX_VEL/5;
	double hitSoundThreshGravity = hitSoundThresh + 10;
	boolean hasDropShadow = true;
	boolean hasTransparentDie = false;
	
	String uno = "Press";
	String dos = "*Space*";
	String tres = "to Roll the Dice";

	SoundPlayer sound = new SoundPlayer();
	Die[] dice = new Die[NUM_DICE];
	String[] hitsfx = new String[] {"Vine Boom.wav", "Bonk.wav", "Metal pipe falling.wav", "Wet fart.wav" };
	String[] rollsfx = new String[] {"Among us yell.wav", "Role reveal.wav"};
	Random gen = new Random();

	boolean isFrozen = false;
	long lastTime = System.currentTimeMillis();

	public GraphicsPanel(Dimension size) {
		this.setPreferredSize(size);
		this.setBackground(new Color(178, 167, 212));
		this.setFocusable(true);
		this.addKeyListener(this);

		for (int i = 0; i < dice.length; i++) {
			int red = (int)(Math.random()*231)+25;
			int green = (int)(Math.random()*231)+25;
			int blue = (int)(Math.random()*231)+25;
			int alpha = 255;
			if (hasTransparentDie) {
				alpha = (int)(Math.random()*196)+60; //level of transparency, 255 is completely opaque
			}
			Color randColor = new Color(red,green,blue,alpha);
			int dieSize = MIN_SIZE+gen.nextInt(MAX_VARIANCE);
			dice[i] = new Die(6, size.getWidth()/2 - (double)dieSize/2, size.getHeight()/2 - (double)dieSize/2, Math.random()*2*MAX_VEL-MAX_VEL, Math.random()*2*MAX_VEL-MAX_VEL, dieSize, randColor, bounciness, sound, hitsfx, hitSoundThresh);
			//(sides, x,     y, x velocity,     y velocity,       size)
		}
	}

	public void refresh() {
		repaint();
	}
	public void moveDice(long dt, double constant) {
		for (Die die : dice) {
			die.move(dt, constant, getSize(), hasHitSound, gravity, hasGravity);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font f = new Font("Times New Roman", Font.PLAIN, 60);
		Font F = new Font("Times New Roman", Font.BOLD, 60);
		g.setFont(F); g.setColor(Color.yellow);
		g.drawString(dos, getThisWidth()/2 - g.getFontMetrics().stringWidth(dos)/2, this.getHeight()*2/6+70);
		g.setFont(f); g.setColor(Color.black);
		g.drawString(uno, getThisWidth()/2 - g.getFontMetrics().stringWidth(uno)/2, this.getHeight()*2/6);
		g.drawString(tres, getThisWidth()/2 - g.getFontMetrics().stringWidth(tres)/2, this.getHeight()*2/6+140);
		for (Die die : dice) {
			die.paintSelf(g, hasDropShadow);
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
		if(e.getKeyCode()==32) //spacebar to roll
		{
			if (hasRollSound)
				//sound.play(hitsfx[0]);
				sound.play(rollsfx[gen.nextInt(rollsfx.length)]);
			for (Die die : dice)
				die.roll();
		}
		
		if(e.getKeyCode() == 82) //r to reverse
			{
			for (Die die : dice) {
				die.setYVel( -die.getYVel() ); //reverse
				die.setXVel( -die.getXVel() ); //reverse (take it back now y'all)
				gravity = -gravity;
			}
		}
		
		if (e.getKeyCode() == 65) //a to jump left
		{
			for (Die die : dice)
				die.jump(-30, 0);
		}
		
		if (e.getKeyCode() == 68) //d to jump right
		{
			for (Die die : dice)
				die.jump(30, 0);
		}
		
		if (e.getKeyCode() == 87) //w to jump up
		{
			for (Die die : dice)
				die.jump(0, 30);
		}
		if (e.getKeyCode() == 83) //s to jump down
		{
			for (Die die : dice)
				die.jump(0, -30);
		}
		if (e.getKeyCode() == 40) //down arrow for higher gravity
		{
			gravity += .3;
		}
		if (e.getKeyCode() == 38) //up arrow for lower gravity
		{
			gravity -= .3;
		}
		
		if (e.getKeyCode() == 71) //g to toggle gravity
		{
			hasGravity = !hasGravity;
		}
		if (e.getKeyCode() == 70 && !isFrozen) //f to freeze and unfreeze
		{
			isFrozen=true;
			for (Die die : dice)
				die.freeze(die.getXVel(), die.getYVel(), gravity); //f to freeze,
		} else if (e.getKeyCode() == 70 && isFrozen) {
			isFrozen=false;
			for (Die die : dice)
				die.unfreeze(); //back to shmoving
		}
	}
	public int getThisHeight() {return this.getHeight();}
	public int getThisWidth() {return this.getWidth();}

	@Override
	public void keyReleased(KeyEvent e) {}
}

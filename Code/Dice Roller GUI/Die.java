import java.util.Random;
import java.awt.*;

public class Die {
	private int sides; private int sideUp;
	double x; double y; int d;
	double xvel; double yvel;
	double gravity = 2;
	double bounciness; // 1 = no energy lost, .5 = half energy lost, 0 = all energy lost
	double tempXVel; double tempYVel; double tempGrav;
	boolean xHit = false; boolean yHit = false;
	boolean hasGravity;
	Random gen = new Random();
	SoundPlayer sound;
	String sfx[];
	double hitSoundThresh;


	Color dieColor;
	Dot[] dots = new Dot[6];

	public Die (int s, double xloc, double yloc, double xvell, double yvell, int dimensions, Color c, double b, SoundPlayer snd, String[] fx, double HST) {
		if (s>3) 
			sides = s;
		else 
			sides = 6;
		x = xloc; y = yloc;
		xvel = xvell; yvel = yvell;
		d = dimensions;
		dieColor = c;
		bounciness = b;
		sfx = fx;
		sound = snd;
		hitSoundThresh = HST;
		roll();
	}


	public void roll() {
		Random generator = new Random();
		sideUp = generator.nextInt(sides)+1;
		//initializes the dots on the die
		if (sideUp == 1) {
			dots[0] = new Dot(d*4/10, 4*d/10, this);
		} else if (sideUp == 2) {
			dots[0] = new Dot(d/10,   d/10,   this);
			dots[1] = new Dot(d*7/10, d*7/10, this);
		} else if (sideUp == 3) {
			dots[0] = new Dot(d/10  , d/10,   this);
			dots[1] = new Dot(d*7/10, d*7/10, this);
			dots[2] = new Dot(d*4/10, d*4/10, this);
		} else if (sideUp == 4) {
			dots[0] = new Dot(d/10,   d/10,   this);
			dots[1] = new Dot(d*7/10, d/10,   this);
			dots[2] = new Dot(d/10,   d*7/10, this);
			dots[3] = new Dot(d*7/10, d*7/10, this);
		} else if (sideUp == 5) {
			dots[0] = new Dot(d/10, d/10,     this);
			dots[1] = new Dot(d*7/10, d/10,   this);
			dots[2] = new Dot(d/10,   d*7/10, this);
			dots[3] = new Dot(d*7/10, d*7/10, this);
			dots[4] = new Dot(d*4/10, d*4/10, this);
		} else {
			dots[0] = new Dot(d/10  , d/10,   this);
			dots[1] = new Dot(d*7/10, d/10,   this);
			dots[2] = new Dot(d/10,   d*7/10, this);
			dots[3] = new Dot(d*7/10, d*7/10, this);
			dots[4] = new Dot(d/10,   d*4/10, this);
			dots[5] = new Dot(d*7/10, d*4/10, this);
		}
	}

	public void paintSelf(Graphics g, boolean hasDropShadow) {
		if (hasDropShadow) {
			g.setColor(new Color(dieColor.getRed(), dieColor.getGreen(), dieColor.getBlue(), dieColor.getAlpha()*3/4).darker().darker().darker().darker().darker());
			g.fillRoundRect((int)x+d/10,(int)y+d/10,d,d,d/10,d/10); //x, y, width, height, arcWidth, arcHeight
		}
		g.setColor(dieColor);
		g.fillRoundRect((int)x,(int)y,d,d,d/10,d/10); //x, y, width, height, arcWidth, arcHeight
		g.setColor(Color.BLACK);
		for (int i = 0; i < getSideUp(); i++)
			dots[i].paintSelf(g);
	}

	public void move(long dt, double constant, Dimension size, boolean hasSound, double gravity, boolean hasG) {
		hasGravity = hasG;
		if (x < 0) {
			x = 0;
			xvel = -1 * bounciness * xvel;
			if (hasSound && Math.abs(xvel) > hitSoundThresh)
				sound.play(sfx[0]);
			//sound.play(sfx[gen.nextInt(sfx.length)]);
		} else if (x + d > size.getWidth()) {
			x = size.getWidth() - d;
			xvel = -1 * bounciness * xvel;
			if (hasSound && Math.abs(xvel) > hitSoundThresh)
				//sound.play(sfx[0]);
				sound.play(sfx[gen.nextInt(sfx.length)]);
		}
		if (y < 0) {
			y = 0;
			yvel = -1 * bounciness * yvel;
			if (hasSound && Math.abs(yvel) > hitSoundThresh)
				//sound.play(sfx[0]);
				sound.play(sfx[gen.nextInt(sfx.length)]);
		} else if (y + d > size.getHeight()) {
			y = size.getHeight() - d;
			yvel = -1 * bounciness * yvel;
			if (hasSound && Math.abs(yvel) > hitSoundThresh)
				//sound.play(sfx[0]);
				sound.play(sfx[gen.nextInt(sfx.length)]);
		}
		if (hasGravity && gravity != 0)
			yvel += gravity / constant * dt;
		x += xvel / constant * dt;
		y += yvel / constant * dt;
		for(int i = 0; i < getSideUp(); i++) {
			dots[i].move(this, dt, constant, gravity, hasGravity);
		}
	}

	public void freeze(double xt, double yt, double tg) {
		xvel = 0;
		yvel = 0;
		gravity = 0;
		tempXVel = xt;
		tempYVel = yt;
		tempGrav = tg;
	}

	public void unfreeze() {
		xvel = tempXVel;
		yvel = tempYVel;
		gravity = tempGrav;
	}

	public void jump(double jumpSide, double jumpUp) {
		yvel -= jumpUp;
		xvel += jumpSide;
	}

	public int getNumSides() {return sides;} //(numberOfSides)
	public int getSideUp() {return sideUp;} //(currentSideUp)
	public double getX() {return x;}
	public double getY() {return y;}
	public double getXVel() {return xvel;}
	public double getYVel() {return yvel;}
	public double getXOld() {return tempXVel;}
	public double getYOld() {return tempYVel;}
	public int getSize() {return d;}
	public void setSize(int dimensions) {d = dimensions;}
	public void setXVel(double xv) {xvel = xv;}
	public void setYVel(double yv) {yvel = yv;}
	public void setX(int xloc) {x = xloc;}
	public void setY(int yloc) {y = yloc;}
}
import java.awt.*;
public class Dot {
	int d;
	Color dotColor; int red; int green; int blue; int alpha;
	double x; double y;
	double xvel; double yvel;
	final double bounciness = .94; // 1 = no energy lost, .5 = half energy lost, 0 = all energy lost

	public Dot(double xstart, double ystart, Die die) {
		xvel = die.getXVel(); yvel = die.getYVel();
		x = die.getX() + xstart; y = die.getY() + ystart;
		d = die.getSize()/5;
		red = die.dieColor.getRed(); green = die.dieColor.getGreen();
		blue = die.dieColor.getBlue(); alpha = die.dieColor.getAlpha();
	}

	public void move(Die die, long dt, double constant, double gravity, boolean hasGravity) {
		//collision detection
		if (x + xvel / constant * dt + die.getXVel() / constant * dt < die.getX()) {
			x = die.getX();
			xvel = -1 * bounciness * (xvel - (die.getXVel()));
		}
		if (x + d + xvel / constant / dt + die.getXVel() / constant * dt > die.getX() + die.getSize()) {
			x = die.getX() + die.getSize()-d;
			xvel = -1 * bounciness * (xvel - (die.getXVel()));
		}
		if (y + yvel / constant * dt + die.getYVel() / constant * dt < die.getY()) {
			y = die.getY();
			yvel = -1 * bounciness * (yvel - (die.getYVel()));
		}
		if (y + d + yvel / constant * dt + die.getYVel() / constant * dt > die.getY() + die.getSize()) {
			y = die.getY() + die.getSize()-d;
			yvel = -1 * bounciness * (yvel - (die.getYVel()));
		}
		//adding movement to position
		if (hasGravity && gravity != 0)
			yvel += gravity / constant * dt;
		x += xvel / constant * dt; //System.out.println("x=" + x);
		y += yvel / constant * dt; //System.out.println("y=" + y);
	}

	public void paintSelf(Graphics g) {
		g.setColor(new Color(red, green, blue, alpha).darker().darker().darker().darker());
		g.fillOval((int)x, (int)y, d, d);
	}
	public double getX() {return x;}
	public double getY() {return y;}
	public int getD() {return d;}
}


public class BlueAlien extends Alien {
	public BlueAlien(double x, double y, int width, int height, int minXBounds, int maxXBounds) {
		super(x, y, width, height, "BlueAlien.png", minXBounds, maxXBounds);
		super.setHealth(10);
		super.setDamage(2);
		super.setxVel(1.75);
	}
}

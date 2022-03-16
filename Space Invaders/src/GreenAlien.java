
public class GreenAlien extends Alien {
	public GreenAlien(double x, double y, int width, int height, int minXBounds, int maxXBounds) {
		super(x, y, width, height, "GreenAlien.png", minXBounds, maxXBounds);
		super.setHealth(4);
		super.setDamage(1);
		super.setxVel(1.5);
	}
}

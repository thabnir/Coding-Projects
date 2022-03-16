import java.awt.*;
public abstract class Alien extends Mover {
	int health;
	int damage;
	int minXBounds;
	int maxXBounds;

	static double speedUpAmount = .2; // amount the alien speeds up by going down one layer

	int layerAt = 0; // 0 is the top layer

	public Alien (double x, double y, int width, int height, String fileName, int minXBounds, int maxXBounds) {
		super(x, y, 0, 0, width, height, fileName);
		this.minXBounds = minXBounds;
		this.maxXBounds = maxXBounds;
	}

	// once again, thank god for auto-generated setters and getters
	public void receiveDamage(int damage) {
		health -= damage;
	}

	public boolean contains(Bullet b) {
		if (b.getX() <= x + width && b.getX() >= x && b.getY() <= y + width && b.getY() >= y)
			return true;
		return false;
	}

	public void move(int screenWidth) {
		super.move();
		if (x + width >= screenWidth && xVel > 0) {
			layerAt += 1; // right side of screen
			super.setY(layerAt * layerHeight);
			super.setX(screenWidth - width - 1);
			super.setxVel(-super.getxVel() - layerAt * speedUpAmount);
		} else if (x <= 0 && xVel < 0) {
			layerAt += 1;
			super.setY(layerAt * layerHeight);
			super.setX(1);
			super.setxVel(-super.getxVel() + layerAt * speedUpAmount);
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getMinXBounds() {
		return minXBounds;
	}

	public void setMinXBounds(int minXBounds) {
		this.minXBounds = minXBounds;
	}

	public int getMaxXBounds() {
		return maxXBounds;
	}

	public void setMaxXBounds(int maxXBounds) {
		this.maxXBounds = maxXBounds;
	}
}
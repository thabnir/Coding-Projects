
public class Bullet extends Mover {
	int damage = 1;

	public Bullet(double x, double y, double xVel, double yVel, int width, int height) {
		super(x, y, xVel, -yVel, width, height, "Bullet.png");
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
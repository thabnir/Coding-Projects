
public class Player extends Mover {
	int health;
	int numBullets;
	long lastHit = 0; // last time the player got damaged by collision
	double speed = 3; // 5 is the starting speed for the player
	long gracePeriod = 1 * 1000; // 1 second grace period after collision where you are invulnerable

	public Player(int x, int y, int width, int height) {
		super(x, y, 0, 0, width, height, "Player.png");
	}

	Boolean contains(Alien a) {
		if (super.contains(a) && System.currentTimeMillis() - lastHit > gracePeriod) {
			lastHit = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	

	public void goLeft() {
		if (super.getX() > 0)
			super.setxVel(-speed);
	}

	public void goRight() {
		if (super.getX() < screenWidth)
			super.setxVel(speed);
	}

	public void goUp() {
		if (super.getY() > 0)
			super.setyVel(-speed);
	}

	public void goDown() {
		if (super.getY() < screenHeight)
			super.setxVel(speed);
	}

	public void stopHorizontal() {
		super.setxVel(0);
	}

	public void stopVertical() {
		super.setyVel(0);
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getNumBullets() {
		return numBullets;
	}

	public void setNumBullets(int numBullets) {
		this.numBullets = numBullets;
	}
	
}
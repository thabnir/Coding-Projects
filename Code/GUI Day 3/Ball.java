import java.awt.*;
public class Ball {
	double xVel; double yVel;
	int x; int y;
	int diameter;
	
	public Ball(int initialX, int initialY, int d) {
		x = initialX;
		y = initialY;
		diameter = d;
		xVel = Math.random()*10-6;
		yVel = Math.random()*10-6;
	}
	public void move() {
		x += xVel;
		y += yVel;
	}
	
	public  void setXVel(double d) {
		xVel = d;
	}
	public void setYVel(double d) {
		yVel = d;
	}
	
	public int getX(){
		return x;
	}
	public int getY() {
		return y;
	}
	public double getXVel() {
		return xVel;
	}
	public double getYVel() {
		return yVel;
	}
	public int getDiameter() {
		return diameter;
	}
	
	public void paintSelf(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x,y,diameter,diameter);
	}
}
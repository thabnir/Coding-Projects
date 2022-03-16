import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;

public abstract class Mover {

	static int screenWidth = 1200;
	static int screenHeight = 800; // this is a terrible way of doing it
	static int numLayers = 8; // layer 0 is the top layer that aliens move across

	static double layerHeight = screenHeight / numLayers; // number of pixels that aliens go down per layer

	double x;
	double y;
	double xVel;
	double yVel;
	int width;
	int height;
	Image image;

	public Mover(double x, double y, double xVel, double yVel, int width, int height, String fileName) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.width = width;
		this.height = height;
		try {image = ImageIO.read(this.getClass().getResource(fileName));}
		catch (IOException e) {e.printStackTrace();}
	}

	public void move() {
		x += xVel;
		y += yVel;
	}

	Boolean contains(Mover m) {
		if (	   (m.getX() <= x + width) 
				&& (m.getX() + m.getWidth() >= x) 
				&& (m.getY() <= y + width) 
				&& (m.getY() + m.getHeight() >= y))
			return true;
		return false;
	}

	public void show(Graphics g) {
		//g.setColor(Color.white);
		//g.fillRect((int)x, (int)y, width, height);
		try { g.drawImage(image, (int)x, (int)y, width, height, null);}
		catch (Exception e) { }
	}

	// thank god for auto-generated setters and getters

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getxVel() {
		return xVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		Mover.screenHeight = screenHeight;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		Mover.screenWidth = screenWidth;
	}

	public static int getNumLayers() {
		return numLayers;
	}

	public static void setNumLayers(int numLayers) {
		Mover.numLayers = numLayers;
	}

	public static double getLayerHeight() {
		return layerHeight;
	}
}
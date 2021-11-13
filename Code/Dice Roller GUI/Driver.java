import java.awt.*;
import javax.swing.*;
public class Driver {
	public static void main(String[] args) {
		Dimension size = new Dimension(2560, 1440);
		JFrame frame = new JFrame("tRoller");
		GraphicsPanel panel = new GraphicsPanel(size);

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final int PHYSICS_RATE = 1000/1000;
		final int FRAMERATE = 1000/144;

		long dt; long lastTimeMoved = System.currentTimeMillis();
		long frameTime; long lastFrameStart = System.currentTimeMillis();

		//long lastTPSTime = System.currentTimeMillis(); int numTicks = 0; double tps;
		//long lastFPSTime = System.currentTimeMillis(); int numFrames = 0; double fps;
		//final double STAT_UPDATE_TIME_MS = 1000/3;
		
		//Animation loop
		while(true) {
			dt = System.currentTimeMillis() - lastTimeMoved;
			frameTime = System.currentTimeMillis() - lastFrameStart;
			
			if (dt >= PHYSICS_RATE) {
				panel.moveDice(dt, 30.0);
				dt = System.currentTimeMillis() - lastTimeMoved;
				lastTimeMoved = System.currentTimeMillis();
				/*
				numTicks++;
				if (System.currentTimeMillis() - lastTPSTime > STAT_UPDATE_TIME_MS) {
					tps = numTicks * (1000/((double)System.currentTimeMillis()-lastTPSTime));
					System.out.println((int)tps + "tps");
					numTicks = 0;
					lastTPSTime = System.currentTimeMillis();
				}
				*/
			}
			if (frameTime >= FRAMERATE) {
				panel.refresh();
				frameTime = System.currentTimeMillis() - lastFrameStart;
				lastFrameStart = System.currentTimeMillis();
				/*
				numFrames++;
				if (System.currentTimeMillis() - lastFPSTime > STAT_UPDATE_TIME_MS) {
					fps = numFrames * (1000/((double)System.currentTimeMillis()-lastFPSTime));
					System.out.println("           "+(int)fps + "fps");
					numFrames = 0;
					lastFPSTime = System.currentTimeMillis();
				}
				*/
			}

		}

	}
}
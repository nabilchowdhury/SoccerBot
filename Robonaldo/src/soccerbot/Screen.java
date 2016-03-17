package soccerbot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

public class Screen extends Thread{
	private TextLCD t = LocalEV3.get().getTextLCD();
	Odometer odometer;
	private static final long DISPLAY_PERIOD = 250;
	
	Screen(Odometer odometer){
		this.odometer = odometer;
	}
	
	public void run() {
		long displayStart, displayEnd;
		double[] position = new double[3];
		// clear the display once
		t.clear();
		while (true) {
			displayStart = System.currentTimeMillis();

			// clear the lines for displaying odometry information
			t.drawString("x: "+(Math.round(odometer.getX() * 100.0)/100.0), 0, 0);
			t.drawString("y: "+(Math.round(odometer.getY() * 100.0)/100.0), 0, 1);
			t.drawString("t: "+(Math.round(odometer.getTheta() * 57.2958 * 100.0)/100.0), 0, 2);
			
			// throttle the OdometryDisplay
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that OdometryDisplay will be interrupted
					// by another thread
				}
			}
		}
	}
	
}

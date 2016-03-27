package soccerbot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

/**
 * Screen handles information grabbed from the Odometer to be outputed to
 * the LCD Screen. This screen is refreshed every 250 ms. 
 * Both one and only one Odometer and Screen can exist.
 */
public class Screen extends Thread{
	private TextLCD t = LocalEV3.get().getTextLCD();
	Odometer odometer;
	private static final long DISPLAY_PERIOD = 250;
	/**
	 * @param odometer The <code>Odometer</code> to be polled for information
	 */
	Screen(Odometer odometer){
		this.odometer = odometer;
	}
	
	/**
	 * The current x and y position and heading are drawn to the screen and refreshed every 250 ms
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long displayStart, displayEnd;
		// clear the display once
		t.clear();
		while (true) {
			displayStart = System.currentTimeMillis();

			// clear the lines for displaying odometry information
			t.drawString("x: "+odometer.getX(), 0, 0);
			t.drawString("y: "+odometer.getY(), 0, 1);
			t.drawString("t: "+Math.toDegrees(odometer.getTheta()), 0, 2);			

			
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

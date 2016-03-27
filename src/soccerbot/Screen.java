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
	USPoller usp1, usp2;
	LSPoller lsp;
	private static final long DISPLAY_PERIOD = 250;
	/**
	 * @param odometer The <code>Odometer</code> to be polled for information
	 */
	Screen(Odometer odometer, USPoller usp1, USPoller usp2, LSPoller lsp){
		this.odometer = odometer;
		this.usp1 = usp1;
		this.usp2 = usp2;
		this.lsp = lsp;
	}
	
	/**
	 * The current x and y position and heading are drawn to the screen and refreshed every 250 ms
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long displayStart, displayEnd;
		double[] position = new double[3];
		// clear the display once
		t.clear();
		while (true) {
			displayStart = System.currentTimeMillis();

			// clear the lines for displaying odometry information
			t.drawString("x: "+/*(Math.round(odometer.getX() * 100.0)/100.0)*/odometer.getX(), 0, 0);
			t.drawString("y: "+/*(Math.round(odometer.getY() * 100.0)/100.0)*/odometer.getY(), 0, 1);
			t.drawString("t: "+((Math.toDegrees(odometer.getTheta())/* * 100.0)/100.0*/)), 0, 2);
			t.drawString("Left US: " + this.usp1.getDistance(), 0, 3);
			t.drawString("Right US: " + this.usp2.getDistance(), 0, 4);
			t.drawString(String.format("Light: %.2g", this.lsp.getDifferentialData()), 0, 5);

			
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

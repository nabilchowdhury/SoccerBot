package soccerbot;

import lejos.hardware.Sound;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * <code>OdometryCorrection</code> is a <code>Thread</code> that runs alongside the Odometer thread
 * to correct its reported position using the color sensor to detect black grid lines on the field.
 * We know that grid lines are located at various multiples of 15 and so we either adjust
 * the x-position or the y-position reported by the odometer depending on which multiple 
 * of 15 and at what heading the the odometer currently reports.
 */
public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	
	private Odometer odometer;
		
	// Color sensor is used to detect known landmarks (in this case black lines)
	private LSPoller poller;

	/**
	 * The constructor requires the Odometer thread object being used and uses the <code>LSPoller</code> passed to retreive 
	 * color sensor data.
	 * 
	 *  @param odometer The odometer thread object to be passed
	 *  @param poller The <code>LSPoller</code> object to be passed
	 *  @see Odometer
	 */
	public OdometryCorrection(Odometer odometer, LSPoller poller) {
		this.odometer = odometer;
		this.poller = poller;
		
	}

	/**
	 * When this thread is ran, this thread will continuously check for black grid lines and, depending on the current position
	 * being reported by the odometer, will update the odometer position. 
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long correctionStart, correctionEnd;
		
		// Perpendicular distance from center of tile to black line
		double length = 30.45;	//in cm
		while (true) {
			
			correctionStart = System.currentTimeMillis();
			
			if(Robonaldo.leftMotor.getRotationSpeed() == Robonaldo.rightMotor.getRotationSpeed()){
			
				//	Odometry correction algorithm based on orientation on the plane
				if(poller.getDifferentialData() < 0.17){	// Detection of black line
					double xPos, yPos, theta;	// Used to store current position values
					int multiplier;		// Used to store multiplier calculated from current odometer reading
					xPos = odometer.getX();
					yPos = odometer.getY();
					theta = odometer.getTheta();
					double thetaConvert = Math.toDegrees(theta); // Converts radians to degrees for simpler algorithm
					
					// If-else if statements used to section plane based on angle
					if(thetaConvert <= 5){	// Left edge
						Sound.beep();
						multiplier = (int)(Math.round(yPos/30));
						odometer.setY(multiplier*length);
					}else if(thetaConvert >= 85 && thetaConvert <= 95){	// Top edge
						Sound.buzz();
						multiplier = (int)(Math.round(xPos/30));
						odometer.setX(multiplier*length);
					
					}else if(thetaConvert >= 175 && thetaConvert <= 185){ // Right edge
						Sound.beep();
						multiplier = (int)(Math.round(yPos/30));
						odometer.setY(multiplier*length);
					}else if(thetaConvert >= 265 && thetaConvert <= 275){ // Bottom edge
						Sound.buzz();
						multiplier = (int)(Math.round(xPos/30));
						odometer.setX(multiplier*length);					
					}else if(thetaConvert >= 355 && thetaConvert <= 360){	//Left edge, approaching initial orientation
						Sound.beep();
						multiplier = (int)(5*(Math.round(yPos/30)));
						odometer.setY(multiplier*length);
						multiplier = (int)(5*(Math.round(xPos/30)));
						odometer.setX(multiplier*length);
					}
					
					try{
						Thread.sleep(500);
					}catch(Exception e){}
					
				}
			

				// this ensure the odometry correction occurs only once every period
				correctionEnd = System.currentTimeMillis();
				if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
					try {
						Thread.sleep(CORRECTION_PERIOD - (correctionEnd - correctionStart));
					} catch (InterruptedException e) {
						// there is nothing to be done here because it is not
						// expected that the odometry correction will be
						// interrupted by another thread
					}
				}
			
			}
		}
	}
}

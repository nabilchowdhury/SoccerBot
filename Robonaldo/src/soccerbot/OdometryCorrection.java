package soccerbot;

import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	
	private Odometer odometer;
		
	// Color sensor is used to detect known landmarks (in this case black lines)
	private EV3ColorSensor colorSensor;
	private SampleProvider cs;
	private float[] csData;

	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		
		// Initiate color sensor detection system
		this.colorSensor = new EV3ColorSensor(SensorPort.S1);
		this.cs = colorSensor.getRedMode();
		int sampleSize = colorSensor.sampleSize();
		csData = new float[sampleSize];
	}

	// Run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		
		// Perpendicular distance from center of tile to black line
		double length = 15.24;	//in cm
		
		while (true) {
			correctionStart = System.currentTimeMillis();
			
			// Obtain color sensor data
			cs.fetchSample(csData, 0);
			int csValue = (int)(csData[0]*100);
			
			//	Odometry correction algorithm based on orientation on the plane
			if(csValue < 37 && csValue > 5){	// Detection of black line in beteween these values
				double xPos, yPos, theta;	// Used to store current position values
				int multiplier;		// Used to store multiplier calculated from current odometer reading
				xPos = odometer.getX();
				yPos = odometer.getY();
				theta = odometer.getTheta();
				double thetaConvert = theta*57.2958; // Converts radians to degrees for simpler algorithm
				
				// If-else if statements used to section plane based on angle
				if(thetaConvert <= 5){	// Left edge
					Sound.beep();
					multiplier = (int)(Math.round(yPos/15));
					odometer.setY(multiplier*length);
					odometer.setTheta(0.0);
				}else if(thetaConvert >= 85 && thetaConvert <= 95){	// Top edge
					Sound.buzz();
					multiplier = (int)(Math.round(xPos/15));
					odometer.setX(multiplier*length);
					odometer.setTheta(90.0/57.2958);
				
				}else if(thetaConvert >= 175 && thetaConvert <= 185){ // Right edge
					Sound.beep();
					multiplier = (int)(Math.round(yPos/15));
					odometer.setY(multiplier*length - 1.5);
					odometer.setTheta(180.0/57.2958);
				}else if(thetaConvert >= 265 && thetaConvert <= 275){ // Bottom edge
					Sound.buzz();
					multiplier = (int)(Math.round(xPos/15));
					odometer.setX(multiplier*length - 3);
					odometer.setTheta(270.0/57.2958);
					
				}else if(thetaConvert >= 355 && thetaConvert <= 360){	//Left edge, approaching initial orientation
					Sound.beep();
					multiplier = (int)(5*(Math.round(yPos/15)));
					odometer.setY(multiplier*length);
					multiplier = (int)(5*(Math.round(xPos/15)));
					odometer.setX(multiplier*length);
				}
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

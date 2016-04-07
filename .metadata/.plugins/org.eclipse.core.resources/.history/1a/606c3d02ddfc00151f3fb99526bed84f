package ev3navigation;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/*
 * DPM LAB 3
 * Group 33
 * Abbas Yadollahi(ID: 260680343) & Nabil Chowdhury(ID: 260622155)
 */

public class Lab3 extends Thread {

	// Motor declaration
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	
	// Ultrasonic Sensor declaration
	private EV3UltrasonicSensor usSensor;
	
	// Class constants
	public final double WHEEL_RADIUS = 2.072;
	public final double TRACK = 19.10;
		
	public boolean isNavigating;
	
	// Text display
	final TextLCD t = LocalEV3.get().getTextLCD();
	
	// Initialize odometer and display
	public Odometer odometer = new Odometer(leftMotor, rightMotor);
	public OdometryDisplay odometryDisplay = new OdometryDisplay(odometer, t);
	
	// Initialize avoider for USSensor
	public Avoider avoider;
	
	// lock object for mutual exclusion
	private Object lock = new Object();
	
	private int buttonChoice;
	private int distance;
	private static final int bandCenter = 8;	// Distance kept from obstacle
	private static int filterValue = 5; 	// Used to filter out bad distance values from sensor
	
	private int filter;	// Counter used to filter US sensor
	

	
	// Initialize navigation
	public static void main(String[] args){
		Lab3 navigation = new Lab3();
		navigation.start();
	}
	
	
	public void run(){
		
		// Start Display
		do{
			t.clear();
			
			t.drawString("< Left | Right >", 0, 0);
			t.drawString("       |        ", 0, 1);
			t.drawString(" Task  | Task  ", 0, 2);
			t.drawString("   1   |   2   ", 0, 3);
			t.drawString("       |Obstacle", 0, 4);

			
			buttonChoice = Button.waitForAnyPress();
		}while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		
		// Choose option
		if(buttonChoice == Button.ID_LEFT){	// Task 1 (No obstacle)
			odometer.start();	// odometer thread
			odometryDisplay.start();	// odometryDisplay thread
			this.isNavigating = false;
			
			// Initialize trajectory sequence
			synchronized(lock){
				travelTo(60, 30);
			}
			synchronized(lock){
				travelTo(30, 30);
			}
			synchronized(lock){
				travelTo(30, 60);
			}
			synchronized(lock){
				travelTo(60, 0);
			}
			
		}else if(buttonChoice == Button.ID_RIGHT){	// Task 2 (Obstacle)
			// Obstacle avoidance initiated
			usSensor = new EV3UltrasonicSensor(SensorPort.S2);
			avoider = new Avoider(usSensor);
			
			odometer.start();	// Odometer thread
			odometryDisplay.start();	// OdometryDisplay thread
			avoider.start();	// Avoider thread for avoiding obstacles
			
			this.isNavigating = false;	// Not yet navigating
			
			// Initialize trajectory sequence
			synchronized(lock){
				travelTo(0, 180);
			}

	
		}
	}
	
	// Moves robot to absolute position (x,y)
	public void travelTo(double x, double y){
		this.isNavigating = true;	// Navigation is true

		synchronized(lock){		
			// Current position variables
			double odoX = odometer.getX();
			double odoY = odometer.getY();
			double odoTheta = odometer.getTheta();
			
			// Error threshold
			double margin = 0.4;
			double angularMargin = 0.015;
			leftMotor.setSpeed(140);
			rightMotor.setSpeed(140);
			
			// absolute heading to desired (x, y) from current position
			double heading = Math.atan2(x-odoX, y-odoY);
	
			// Loop until target position is reached
			while(true){
				leftMotor.setSpeed(140);
				rightMotor.setSpeed(140);
				
				// Correct heading(angle) if angle is not within error margins
				if(!(heading<(odoTheta+angularMargin) && heading>(odoTheta-angularMargin))){
					turnTo(angleToHeading(x, y));
				}
				
				// Obstacle avoidance algorithm
				if(buttonChoice == Button.ID_RIGHT){
					
					// Obtain distance from US Sensor
					this.distance = avoider.getDistance();
					
					// Basic filter used to filter out bad values
					if(avoider.getDistance() > 3*bandCenter && filter <= filterValue){
						this.filter++;
					}else{
						this.distance = avoider.getDistance();
					}	
					
					// Display distance on screen
					t.drawString("Obstacle: "+ this.distance, 0, 5);
					
					// Turn 90 degrees when obstacle is detected in front of robot
					if(this.distance <= bandCenter){
						leftMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.PI/2), true);	
						rightMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.PI/2), false);
					
						// Calculates absolute slope used to get back on trajectory after avoiding obstacle
						double slope = (x - odoX)/(y - odoY);
						double changingSlope;
						double slopeError = 0.04;
						boolean ignore = true;	// Used to ignore first slope value as soon as turn starts. We only want the slope after the turn.
						
						while(true){
							// Calculates slope as robot is turning to avoid obstacle. Once changingSlope == absolute slope, we are on trajectory
							changingSlope = (x - odometer.getX())/(y - odometer.getY());
							this.distance = avoider.getDistance();
							avoid(this.distance);
							Sound.beep();
							// First if statement used to ignore first slope value
							if((changingSlope < slope+slopeError) && (changingSlope > slope-slopeError) && ignore == true){
								ignore = false;
								// Sleeps for 500 ms to avoid first slope calculation. We only want the slope after the turn not before
								try{
									Thread.sleep(500);
								}catch(Exception e){
									
								}
								
							// Once back on trajectory, stop motors and break out of while loop
							}else if((changingSlope < slope+slopeError) && (changingSlope > slope-slopeError) && ignore == false){
								leftMotor.setSpeed(0);
								rightMotor.setSpeed(0);
								leftMotor.backward();
								rightMotor.backward();
								break;
							}
						}
						
					}
					
				}
				
				// Move forward. Backward() is used due to robot design
				leftMotor.backward();
				rightMotor.backward();
				
				// Update current position
				odoX = odometer.getX();
				odoY = odometer.getY();
				odoTheta = odometer.getTheta();
				
				// Break loop once destination is reached
				if(odometer.getX() > (x - margin) && odometer.getX() < (x + margin) && odometer.getY() > (y - margin) && odometer.getY() < (y + margin)){
					rightMotor.setSpeed(0);
					leftMotor.setSpeed(0);
					rightMotor.forward();
					leftMotor.forward();
					break;
				}
			}	
		}
		
	}
	
	// Causes robot to turn to absolute heading theta via minimum angle
	public void turnTo(double theta){		
		synchronized(lock){
			leftMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, theta), true);	
			rightMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, theta), false);
		}
	}
	
	public boolean isNavigating(){
		return this.isNavigating;
	}
	
	// Converts absolute distance in cm into degrees the wheels are required to turn
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	// Converts absolute angle on plane to degrees each wheel is required to turn
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
	}
	
	// Calculates the difference in angle required to point to desired destination. This method is passed into "turnTo()"
	public double angleToHeading(double x, double y){
		// absolute heading
		double heading = Math.atan2(x-odometer.getX(), y-odometer.getY());
					
		// Minimal angle to correct trajectory
		double angularError = heading - odometer.getTheta();
		double angleToHeading = 0;
		if(angularError<= Math.PI && angularError >= -Math.PI){
			angleToHeading = angularError;
		}else if(angularError < -Math.PI){
			angleToHeading = angularError + 2*Math.PI;
		}else if(angularError > Math.PI){
			angleToHeading = angularError - 2*Math.PI;
		}
		return angleToHeading;
	}
	
	// Bang-bang style controller used to avoid obstacle
	private void avoid(int value){
		
		int error = value - bandCenter;
		
		// Turns away if too close
		if(error < 2){
			leftMotor.setSpeed(208);
			rightMotor.setSpeed(100);
			leftMotor.backward();
			rightMotor.backward();
		// Turns towards obstacle if too far
		}else if(error > 2){
			leftMotor.setSpeed(100);
			rightMotor.setSpeed(208);
			leftMotor.backward();
			rightMotor.backward();
		}
		
	}
	

}

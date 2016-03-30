 package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * <code>Navigation</code> is the class tasked with navigating the robot
 * from coordinate to coordinate and alter its angle of orientation.
 * Information required to process the functions of this class depend on information 
 * stored in the <code>Odometer</code> class.
 * <p>
 * Only a single instance of both <code>Navigation</code> and <code>Odometer</code> should exist.
 * 
 * @author Nabil Chowdhury
 * @author Omar Akkila
 */
public class Navigation {
	// class constants
	private final static double W_RADIUS = 2.072;
	private final static double W_BASE = 19.15;
	private final static int FAST = 200, SLOW = 200, REGULAR = 160, SMOOTH = 2000, DEFAULT = 6000;
	private final static double DEG_ERR = 0.04, CM_ERR = 0.2, BAND = 9;
	
	// motors
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	
	//odometer
	private Odometer odometer;
	
	// ultrasonic sensors
	USPoller leftPoller;
	USPoller rightPoller;
	
	private Object lock;
	
	/**
	 * This constructor assumes two <code>EV3LargeRegualtedMotor</code> objects are linked to the brick 
	 * and also requires an <code>Odometer</code> to process navigation.
	 * 
	 * @param odometer The odometer object used to retrieve coordinates and heading
	 * @param leftMotor One of two EV3LargeRegulatedMotor objects passed to navigate the robot
	 * @param rightMOtor Second EV3LargeRegualtedMotor object passed to navigate the robot
	 */
	public Navigation(Odometer odometer, USPoller left, USPoller right, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.odometer = odometer;
		this.leftPoller = left;
		this.rightPoller = right;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.lock = new Object();
	}
	
	/**
	 * Travels to the specified coordinate (x, y).
	 * The current x and y positions of the robot is retrieved from the Odometer
	 * where the specified coordinates and the current coordinates are used to determine
	 * the correct heading the robot should face before attempting to travel to 
	 * the desired coordinate. 
	 * <p>
	 * Once the current heading reported by the Odometer is within 0.05 degrees error, the robot
	 * will then begin its motion towards the desired coordinate until the distance between the polled
	 * coordinates grabbed from the Odometer and the desired coordinate is within 0.5 cm.
	 * The motors are then stopped.
	 * 
	 *  @param x X-position of the desired coordinate
	 *  @param y Y-position of the desired coordinate
	 *  @see Odometer
	 */
	public void travelTo(double x, double y, boolean avoid) {
		synchronized(lock){
			double thetaD;
			x = x*30.45;
			y = y*30.45;
			
			this.setSpeeds(SLOW, SLOW, false, DEFAULT);
			
			thetaD = (Math.atan2(x - odometer.getX(), y - odometer.getY()));
			
			//theta from 0 to 2PI
			if (thetaD < 0){
				thetaD += Math.PI*2;
			}
						
			double angularError = thetaD - this.odometer.getTheta();
			
			if(angularError > Math.PI){
				angularError -= Math.PI*2;
			}
		
			if(angularError < -Math.PI){
				angularError += Math.PI*2;
			}
			
			if(Math.abs(angularError) > DEG_ERR){
				turnTo(angleToHeading(x,y));
			}
			
			
			while (Math.abs(x - odometer.getX()) > CM_ERR || Math.abs(y - odometer.getY()) > CM_ERR) {
			
				this.setSpeeds(SLOW, SLOW, true, SMOOTH);
				
				/*
				///////////////////////
				// OBSTACLE AVOIDANCE//
				///////////////////////
				if(avoid){
				// Turn 90 degrees when obstacle is detected in front of robot
				if((leftPoller.getDistance() <= BAND) || (rightPoller.getDistance() <= BAND)){
					goStraight(100, 100, -6);
					if(odometer.getX() > odometer.getY()){
						turnTo(-Math.PI/4);
					}else {
						turnTo(Math.PI/4);
					}
					
					
				
					// Calculates absolute slope used to get back on trajectory after avoiding obstacle
					double slope = (x - odometer.getX())/(y - odometer.getY());
					double changingSlope;
					double slopeError = 0.06;
					boolean ignore = true;	// Used to ignore first slope value as soon as turn starts. We only want the slope after the turn.
					double odoX = odometer.getX(); double odoY = odometer.getY();
					while(true){
						// Calculates slope as robot is turning to avoid obstacle. Once changingSlope == absolute slope, we are on trajectory
						changingSlope = (x - odometer.getX())/(y - odometer.getY());
						
						if(odoX > odoY){
							avoid(rightPoller.getDistance(), odoX, odoY);
						}else {
							avoid(leftPoller.getDistance(), odoX, odoY);
						}

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
							stopMotors();
							break;
						}
						
						if(Math.abs(x-odometer.getX()) <= 7 || Math.abs(y-odometer.getY()) <= 7){
							return;
						}
						
					}
					
					
				}
				}*/
				
			}
		}
		stopMotors();
	}
	
	/**
	 * This method causes the robot to turn to absolute heading theta via a minimum angle
	 * 
	 * @param theta Absolute angle from the y-axis to turn to
	 * @see Odometer
	 */
	public void turnTo(double theta){	
		leftMotor.rotate(-convertAngle(W_RADIUS, W_BASE, theta), true);	
		rightMotor.rotate(convertAngle(W_RADIUS, W_BASE, theta), false);
	}
	
	
	/**
	 * Convert absolute distance in cm into degrees the wheels are required based
	 * on the radius of the wheel to be rotated.
	 * Equation used is (180.0 * distance) / (pi * radius)
	 * 
	 * @param radius Radius of wheel being rotated in cm
	 * @param distance Absolute distance in cm
	 * @return Degrees wheel is required to rotate
	 */
	private int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	//go straight
	public void goStraight(int lSpd, int rSpd, double distance){
		setSpeeds(lSpd, rSpd, true, SMOOTH);
			leftMotor.rotate(-convertDistance(W_RADIUS, distance), true);
			rightMotor.rotate(-convertDistance(W_RADIUS, distance), false);
	}
	

	/**
	 * Convert absolute angle on place to degrees each wheel is required to turn based
	 * on the radius of the wheel to be rotated and the width of the robot.
	 * Through the angle and the width we can calculate the distance required to move using
	 * (width * angle) / 2.0
	 * 
	 * @param radius Radius of wheel being rotated in cm
	 * @param width Wheel-to-wheel distance measured in cm from the center of each wheel
	 * @param angle Absolute angle to be rotated in radians
	 * @return Degrees wheel is required to rotate
	 * @see convertDistance(dobule radius, double distance)
	 * 
	 */ 
	private int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
	}
	
	
	
	
	/**
	 * Calculate the absolute angle to orient this robot toward the coordinate (x, y).
	 * This method is called in the <code>travelTo</code> method to orient this robot toward
	 * the desired coordinate before starting its motion toward it.
	 * 
	 * @param x X-position of coordinate to turn to
	 * @param y Y-position of coordinate to turn to
	 * @return Absolute angle to turn to relative to y-axis
	 */ 
	private double angleToHeading(double x, double y){
		// absolute heading
		double heading = Math.atan2(x-odometer.getX(), y-odometer.getY());
		
		//theta from 0 to 2PI
		if (heading < 0){
			heading += Math.PI*2;
		}
						
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
	
	public void turnTo(double x, double y){
		turnTo(angleToHeading(x, y));
	}
	
	
	
	/**
	 * Set the speeds of each motor and give the option on whether to move directly after
	 * setting.
	 * 
	 * @param leftSpeed Speed of left motor
	 * @param rightSpeed Speed of right motor
	 * @param move Begin motion of motors if <code>true</code> otherwise do nothing
	 */
	public void setSpeeds(int leftSpeed, int rightSpeed, boolean move, int acceleration){
		leftMotor.setSpeed(leftSpeed); leftMotor.setAcceleration(acceleration);
		rightMotor.setSpeed(rightSpeed); rightMotor.setAcceleration(acceleration);
		
		if(leftSpeed == 0 && rightSpeed == 0){
			stopMotors();
		}
		
		if(move){
			if(leftSpeed < 0){
				leftMotor.forward();
			}else {
				leftMotor.backward();
			}
			
			if(rightSpeed < 0){
				rightMotor.forward();
			}else {
				rightMotor.backward();
			}
		}
	}
	
	public void stopMotors(){
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		leftMotor.backward();
		rightMotor.backward();
	}
	
	// Bang-bang style controller used to avoid obstacle
	private void avoid(double value, double odoX, double odoY){
		
		double error = value - BAND;
		
		if(odoY > odoX){	
			// Turns away if too close
			if(error < 2){
				setSpeeds(200, 150, true, DEFAULT);
			// Turns towards obstacle if too far
			}else if(error > 2){
				setSpeeds(150, 200, true, DEFAULT);
			}else {
				setSpeeds(150,150, true, DEFAULT);
			}
		}else {
			// Turns away if too close
			if(error < 2){
				setSpeeds(150,200, true, DEFAULT);
			// Turns towards obstacle if too far
			}else if(error > 2){
				setSpeeds(200, 150, true, DEFAULT);
			}else {
				setSpeeds(150,150, true, DEFAULT);
			}
		}
		
	}
	
}


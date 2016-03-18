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
	private final static double W_RADIUS = 2.096;
	private final static double TRACK = 13.68;
	private final static int FAST = 200, SLOW = 100, REGULAR = 160, SMOOTH = 500, DEFAULT = 6000;
	private final static double DEG_ERR = 0.05, CM_ERR = 0.5;
	
	// motors
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	
	//odometer
	private Odometer odometer;
	
	private Object lock;
	
	/**
	 * This contuctor assumes two <code>EV3LargeRegualtedMotor</code> objects are linked to the brick 
	 * and also requires an <code>Odometer</code> to process navigation.
	 * 
	 * @param odometer The odometer object used to retreive coordinates and heading
	 * @param leftMotor One of two EV3LargeRegulatedMotor objects passed to navigate the robot
	 * @param rightMOtor Second EV3LargeRegualtedMotor object passed to navigate the robot
	 */
	public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		lock = new Object();
	}
	
	/**
	 * Travels to the specified coordinate (x, y).
	 * The current x and y positions of the robot is retreived from the Odometer
	 * where the specificed coordiantes and the current coordinates are used to determine
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
	public void travelTo(double x, double y){		
		synchronized(lock){

			double odoX = odometer.getX();
			double odoY = odometer.getY();
			double odoTheta = odometer.getTheta();
			double heading = Math.atan2(x-odoX, y-odoY);
			double angularError;
			
			while (euclidianDist(odoX, odoY, x, y) > CM_ERR) {
				angularError = Math.abs(heading - odoTheta);
				if(angularError > DEG_ERR){
					turnTo(angleToHeading(x, y));
				}
				
				setSpeeds(REGULAR, REGULAR, true, SMOOTH);
				
				// (make simpler later)
				// Update current position
				odoX = odometer.getX();
				odoY = odometer.getY();
				odoTheta = odometer.getTheta();
			}
			//stopMotors();
		}
	}
	
	/**
	 * This method causes the robot to turn to absolute heading theta via a minimum angle
	 * 
	 * @param theta Absolute angle from the y-axis to turn to
	 * @see Odometer
	 */
	public void turnTo(double theta){	
		synchronized(lock){
			setSpeeds(SLOW, SLOW, true, DEFAULT);
			leftMotor.rotate(convertAngle(W_RADIUS, TRACK, theta), true);	
			rightMotor.rotate(-convertAngle(W_RADIUS, TRACK, theta), false);
		}
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
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	

	/**
	 * Convert absolute angle on place to degrees each wheel is required to turn based
	 * on the radius of the wheel to be rotated and the width of the robot.
	 * Through the angle and the width we can calculate the distance required to move using
	 * (width * angle) / 2.0
	 * 
	 * @param radius Radius of wheel being rotated in cm
	 * @param width Wheel-to-wheel distance measured in cm from the center of each wheel
	 * @param angle Absolute angle to be rotated in rads
	 * @return Degrees wheel is required to rotate
	 * @see convertDistance(dobule radius, double distance)
	 * 
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
	}
	
	/**
	 * Calculate the absolute angle to orient this robot toward the coordinate (x, y).
	 * This method is called in the <code>travelTo</code> method to orient this robot toward
	 * the desied coordinate before starting its motion toward it.
	 * 
	 * @param x X-position of coordinate to turn to
	 * @param y Y-position of coordinate to turn to
	 * @return Absolute angle to turn to relative to y-axis
	 */
	private double angleToHeading(double x, double y){
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

	/**
	 * Set the speeds of each motor and give the option on whether to move directly after
	 * setting.
	 * 
	 * @param leftSpeed Speed of left motor
	 * @param rightSpeed Speed of right motor
	 * @param move Begin motion of motors if <code>true</code> otherwise do nothing
	 */
	private void setSpeeds(int leftSpeed, int rightSpeed, boolean move, int acceleration){
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.setAcceleration(acceleration);
		rightMotor.setAcceleration(acceleration);
		if(leftSpeed == 0 && rightSpeed == 0){
			stopMotors();
		}
		
		if(move){
			if(leftSpeed < 0){
				leftMotor.backward();
			}else {
				leftMotor.forward();
			}
			
			if(rightSpeed < 0){
				rightMotor.backward();
			}else {
				rightMotor.forward();
			}
		}
	}
	
	private void stopMotors(){
		leftMotor.setAcceleration(DEFAULT);
		rightMotor.setAcceleration(DEFAULT);
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		leftMotor.backward();
		rightMotor.backward();
	}
	
	private double euclidianDist(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(y2-y1, 2) + Math.pow(x2 - x1, 2));
	}
	
}


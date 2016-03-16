package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Navigation {
	// class constants
	private final static double W_RADIUS = 2.096;
	private final static double TRACK = 15.92;
	private final static int FAST = 200, SLOW = 100, REGULAR = 170;
	private final static double DEG_ERR = 0.015, CM_ERR = 0.5;
	
	// motors
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	
	//odometer
	private Odometer odometer;
	
	private Object lock;
	
	public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		lock = new Object();
	}
	
	//navigation methods
	
	private void travelTo(double x, double y){		
		synchronized(lock){
			
			double odoX = odometer.getX();
			double odoY = odometer.getY();
			double odoTheta = odometer.getTheta();
			double heading = Math.atan2(x-odoX, y-odoY);
			double angularError;
			
			while (Math.abs(x - odometer.getX()) > CM_ERR || Math.abs(y - odometer.getY()) > CM_ERR) {
				angularError = Math.abs(heading - odoTheta);
				if(angularError > DEG_ERR){
					turnTo(angleToHeading(x, y));
				}
				
				setSpeeds(REGULAR, REGULAR, true);
				
				// (make simpler later)
				// Update current position
				odoX = odometer.getX();
				odoY = odometer.getY();
				odoTheta = odometer.getTheta();
			}
			
		}
	}
	
		
	// Causes robot to turn to absolute heading theta via minimum angle
	private void turnTo(double theta){		
		synchronized(lock){
			setSpeeds(SLOW, SLOW, true);
			leftMotor.rotate(convertAngle(W_RADIUS, TRACK, theta), true);	
			rightMotor.rotate(-convertAngle(W_RADIUS, TRACK, theta), false);
			stopMotors();
		}
	}
	
	// helper methods
	// Converts absolute distance in cm into degrees the wheels are required to turn
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	// Converts absolute angle on plane to degrees each wheel is required to turn
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
	}
	
	// Calculates the difference in angle required to point to desired destination. This method is passed into "turnTo()"
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
	
	private void setSpeeds(int leftSpeed, int rightSpeed, boolean move){
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
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
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		leftMotor.backward();
		rightMotor.backward();
	}
	
	
}


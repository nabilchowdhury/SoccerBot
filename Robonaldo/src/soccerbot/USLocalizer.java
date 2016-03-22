package soccerbot;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * TODO
 *
 */
public class USLocalizer {
	
	// Constants
	private static final int ROTATION_SPD = 100;
	private static final int D_HIGH = 41;
	private static final int D_LOW = 39;
	private static final int ACC = 6000;
	
	private Odometer odo;
	private USPoller usPoller;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	// Constructor
	USLocalizer(Odometer odometer, USPoller poller, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.odo = odometer;
		this.usPoller = poller;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}
	
	public void Localize(){
		double [] pos = new double [3];
		double angleA; double angleB;
		
		// Temporary variables used to calculate angleA and angleB
		Double angleHigh = null;
		Double angleLow = null;
		double deltaT = 0;
		
		Navigation navigate = new Navigation(odo, leftMotor, rightMotor);
		
		// RISING EDGE
		
		//INITIATE COUNTERCLOCKWISE SEQUENCE
		navigate.setSpeeds(ROTATION_SPD, -ROTATION_SPD, true, ACC);
		
		// If robot starts facing away from a wall, continue to turn until it is facing a wall
		while(usPoller.getDistance() > D_LOW){
			//empty loop
		}
		
		while(true){
			if(usPoller.getDistance() >= D_LOW && angleHigh == null){
				angleHigh = odo.getTheta();
			}else if(usPoller.getDistance() >= D_HIGH && angleHigh != null){
				angleLow = odo.getTheta();
			}
			
			// Calculate angleA and break out of loop
			if(angleHigh != null && angleLow != null){
				angleA = (angleHigh+angleLow)/2;
				angleHigh = null;
				angleLow = null;
				
				//Stop motors briefly
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		// INITIATE CLOCKWISE SEQUENCE
		navigate.setSpeeds(-ROTATION_SPD, ROTATION_SPD, true, ACC);
		
		// Empty loop used to filter out if robot begins facing wall (this is a precautionary step)
		while(usPoller.getDistance() > D_LOW){
			//empty loop
		}
		
		while(true){
			// Set high and low angles
			if(usPoller.getDistance() >= D_HIGH && angleHigh == null){
				angleHigh = odo.getTheta();
			}else if(usPoller.getDistance() >= D_HIGH && angleHigh != null){
				angleLow = odo.getTheta();
			}
			
			// Calculate both angles and break out of loop
			if(angleHigh != null && angleLow != null){
				angleB = (angleHigh+angleLow)/2;
				angleHigh = null;
				angleLow = null;
				
				//Stop motors briefly
				navigate.stopMotors();
				Sound.beep();
				break;
				
			}
			
		}
		
		// Correct robot to approximately 0 deg heading
		if(angleA < angleB){
			deltaT = 43- (angleA+angleB)/2;
		}else {
			deltaT = 225- (angleA+angleB)/2;
		}
		
		double correctionAngle = angleB + deltaT;
		
		navigate.turnTo(correctionAngle, false);
	}
	
	
}

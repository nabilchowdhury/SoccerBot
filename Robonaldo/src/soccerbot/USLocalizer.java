package soccerbot;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * USLocalizer performs the tasks required for localizing the robot 
 * using the ultrasonic sensor at startup.
 * 
 * The robot's center of rotation must initially intersect with the corner-to-corner diagonal of the field.
 * This class will use the 'rising edges' during wall detection and the diagonal to determine its true orientation relative to the field.
 * We call this rising edge localization. We call a rising edge the moment a robot detects a wall, rotates, and then detects no wall.
 * 
 * @see Odometer
 */
public class USLocalizer {
	
	// Constants
	private static final int ROTATION_SPD = 250;
	private static final int D_HIGH = 35;
	private static final int D_LOW = 33;
	
	private Odometer odo;
	private Navigation navigate;
	private USPoller usPollerL;
	private USPoller usPollerR;
			
	/**
	 * This constructor assumes two ultrasonic sensors are connected. The <code>Odometer</code> object is required to be able to update its heading and two <code>USPoller</code>
	 * objects to receive polled data from each sensor to determine the rising edges. <code>Navigation</code> object is necessary since we require this robot to 
	 * use its motors for in-place rotation during the localization process.
	 * <p>
	 * NOTE: Only a single instance of both <code>Odometer</code> and <code>Navigation</code> should exist. These instances are to be passed around and any
	 * other instances passed will cause disastrous results. 
	 * 
	 * @param odometer The <code>Odometer</code> object to be passed
	 * @param navigate The <code>Navigation</code> object to be passed
	 * @param pollerL Polls the left ultrasonic sensor and retrieves data from it
	 * @param pollerR Polls the right ultrasonic sensor and retrieves data from it
	 */
	public USLocalizer(Odometer odometer, Navigation navigate, USPoller pollerL, USPoller pollerR){
		this.odo = odometer;
		this.navigate = navigate;
		this.usPollerL = pollerL;
		this.usPollerR = pollerR;
	}
	
	/**
	 * This method performs the rising edge localization. It will allow the robot to rotate and detect walls until 
	 * the moment it doesn't. We call this the rising edge. When taking note of the rising edges, we are able to determine
	 * true orientation relative to the field and update the odometer to reflect this.
	 */
	public void localize(){
		Robonaldo.loadMotor.stop();  // stop loadMotor from moving while localizing
		
		double deltaT = 0;
		double angleA; double angleB;
		// Temporary variables used to calculate angleA and angleB
		Double angleHigh = null;
		Double angleLow = null;
				
		
		//INITIATE COUNTERCLOCKWISE SEQUENCE
		navigate.setSpeeds(-ROTATION_SPD, ROTATION_SPD, true, 6000);
		// If robot starts facing away from a wall, continue to turn until it is facing a wall
		while(usPollerL.getDistance() > D_LOW-2){
			//empty loop
		}
		// Robot is now facing the wall. Use left USSensor for to detect first rising edge
		while(true){
			
			// find angleHigh and angleLow
			if(usPollerL.getDistance() >= D_LOW && angleHigh == null){
				angleHigh = odo.getTheta();
			}else if(usPollerL.getDistance() >= D_HIGH && angleHigh != null){
				angleLow = odo.getTheta();
			}
			
			// Calculate angleA and break out of loop
			if(angleHigh != null && angleLow != null){
				angleA = (angleHigh+angleLow)/2;
				// reset angleHigh and angleLow
				angleHigh = null;
				angleLow = null;
				
				//Stop motors briefly
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		
		
		// INITIATE CLOCKWISE SEQUENCE
		navigate.setSpeeds(ROTATION_SPD, -ROTATION_SPD, true, 6000);
		// Empty loop used to filter out if robot begins facing wall (this is a precautionary step)
		try{
			Thread.sleep(1000);
		}catch(Exception e){}
		while(usPollerR.getDistance() > D_LOW){
			//empty loop	
		}
		while(true){
			// Set high and low angles
			if(usPollerR.getDistance() >= D_LOW && angleHigh == null){
				angleHigh = odo.getTheta();
			}else if(usPollerR.getDistance() >= D_HIGH && angleHigh != null){
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
		
		
		// Correct robot to angle (//enter details of orientation here)
		if(angleA < angleB){
			deltaT = Math.toRadians(30) - (angleA+angleB)/2; // 35.125
		}else {
			deltaT = Math.toRadians(213) - (angleA+angleB)/2; //218.5
		}
		navigate.setSpeeds(ROTATION_SPD, ROTATION_SPD, false, 6000);
		// Update odometer angle
		navigate.turnTo(odo.getTheta()+deltaT);	
	}

}

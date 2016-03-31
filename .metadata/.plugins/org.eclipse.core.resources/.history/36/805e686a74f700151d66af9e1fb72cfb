package soccerbot;

import lejos.hardware.Sound;

public class UltrasonicLocalizer {
	// Constants
	private static final int ROTATION_SPD = 300;
	private static final int D_HIGH = 32;
	private static final int D_LOW = 30;
		
	private Odometer odo;
	private Navigation navigate;
	private USPoller leftPoller;
	
	public UltrasonicLocalizer(Odometer odometer, Navigation nav, USPoller leftPoller){
		this.odo = odometer;
		this.navigate = nav;
		this.leftPoller =leftPoller;
	}
	
	public void localize(){
		Robonaldo.loadMotor.stop();
		/*
		// Initiate counterclockwise sequence
		navigate.setSpeeds(-ROTATION_SPD, ROTATION_SPD, true);
		
		while(leftPoller.getDistance() > D_LOW){
			// empty loop if robot starts facing away from wall
		}
		
		try{
			Thread.sleep(800);
		}catch(Exception e){}
		
		// Robot is now facing wall. Use left USSensor for to detect rising edge
		while(true){					
			// find angleHigh and angleLow
			if(leftPoller.getDistance() >= D_LOW){
				navigate.stopMotors();
				break;
			}
		}
	
		navigate.turnTo(-Math.toRadians(165));	*/
		
		
	}
	
}

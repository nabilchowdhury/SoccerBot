package soccerbot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * <code>LightLocalizer</code> is in charge of using the color sensor to perform localization. This is performed
 * after the localization of this robot using the ultrasonic sensor.
 * <p>
 * TODO: Describe and detail light localization process.
 * 
 * @author Omar Akkila
 * @author Nabil Chowdhury
 *
 */
public class LightLocalizer{
	private Odometer odo;
	private Navigation navigate;
	
	private LSPoller leftCS;
	private LSPoller rightCS;
	
	private int startingCorner;
	
	
	/**
	 * This constructor requires the <code>Odometer</code> object to update its position, a <code>Navigation</code> object to control the motors during localization,
	 * two <code>LSPoller</code> objects to detect grid lines, and an integer representing the starting corner to perform the proper localization process
	 * 
	 * @param odo The <code>Odometer</code> object to be passed
	 * @param navigate The <code>Navigation</code> object to be passed
	 * @param leftCS The <code>LSPoller</code> object representing the left EV3 Color Sensor to be passed
	 * @param rightCS The <code>LSPoller</code> object representing the right EV3 Color Sensor to be passed
	 * @param startingCorner Integer between 1-4 representing the four corners of the field;
	 * 						 Bottom-left = 1, Bottom-Right = 2, Top-Left = 3, Top-Right = 4
	 */
	public LightLocalizer(Odometer odo, Navigation navigate, LSPoller leftCS, LSPoller rightCS, int startingCorner) {
		this.odo = odo;
		this.navigate = navigate;
		this.leftCS = leftCS;
		this.rightCS = rightCS;
		this.startingCorner = startingCorner;	
	}
	
	/**
	 * Performs the localization process.
	 */
	public void localize() {
		navigate.goStraight(300,300, -23);
		
		preventTwitch();
		
		correctAngle();
		
		/*
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}*/
		
		
		
		preventTwitch();
		
		navigate.goStraight(200, 200, 4.5);
		
		preventTwitch();
		
		navigate.setSpeeds(200, 200, false, 2000);
		navigate.turnTo(Math.PI/2);
		
		preventTwitch();
		
		navigate.goStraight(200, 200, -8);
		
		preventTwitch();
		
		correctAngle();
		
		/*
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}*/
		
		preventTwitch();
		
		navigate.goStraight(220, 220, 11.3);
		
		preventTwitch();
		
		navigate.setSpeeds(-180, 180, true, 6000);
		
		int count = 0;
		
		while(true){
			if(count == 2){
				navigate.stopMotors();
				break;
			}
			if(leftCS.getDifferentialData() > 0.13){
				count++;
				Sound.beep();
			}
		}
		
		
		navigate.setSpeeds(150, 150, false, 6000);
		
		navigate.turnTo(Math.toRadians(-14.4));
		
		preventTwitch();
		
		navigate.goStraight(150, 150, 7.95);	
		
		preventTwitch();
		

		// fix for actual competition
		if(this.startingCorner == 1){
			double[] position = {0.0, 0.0, 0.0};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 2){
			double[] position = {6*30.45, 0.0, 3*Math.PI/2};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 3){
			double[] position = {6*30.45, 6*30.45, Math.PI};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 4){
			double[] position = {0.0, 6*30.45, Math.PI/2};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}
		
		Sound.beep();
		
	}

	/**
	 * This method uses the two light sensors and grid lines to correct the angle during localization.
	 */
	public void correctAngle(){
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}
	}
	
	/**
	 * Prevents motor malfunction when switching between movements.
	 */
	public void preventTwitch(){
		navigate.stopMotors();
		try{
			Thread.sleep(50);
		}catch(Exception e){}
	}
	
	
}

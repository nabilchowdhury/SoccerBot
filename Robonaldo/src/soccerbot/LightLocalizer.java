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
	
	private LSPoller lsPoller;
	
	//display variables, delete this
	public static double x;
	public static double y;
	public static double deltaT;
	public static double thetaX;
	public static double thetaY;
	
	
	/**
	 * This constructor requires the <code>Odometer</code> object to update its position, a <code>Navigation</code> object to control the motors during localization,
	 * and a <code>LSPoller</code> to detect grid lines during the localization process
	 * 
	 * @param odo The <code>Odometer</code> object to be passed
	 * @param navigate The <code>Navigation</code> object to be passed
	 * @param lsPoller The <code>LSPoller</code> object to be passed
	 */
	public LightLocalizer(Odometer odo, Navigation navigate, LSPoller lsPoller) {
		this.odo = odo;
		this.navigate = navigate;
		this.lsPoller = lsPoller;
		
	}
	
	/**
	 * Performs the localization process.
	 */
	public void localize() {
		navigate.goStraight(280,280, -23);
		
		preventTwitch();
		
		navigate.setSpeeds(250, 250, true, 2000);
		
		while(true){
			if(lsPoller.getDifferentialData() > 0.06){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		preventTwitch();
		
		navigate.goStraight(250, 250, 3.3);
		
		preventTwitch();
		
		navigate.setSpeeds(250, 250, false, 2000);
		navigate.turnTo(Math.PI/2);
		
		preventTwitch();
		
		navigate.goStraight(280, 280, -8);
		
		preventTwitch();
		
		navigate.setSpeeds(250, 250, true, 2000);
		
		while(true){
			if(lsPoller.getDifferentialData() > 0.06){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		preventTwitch();
		
		navigate.goStraight(250, 250, 9.15);
		
		preventTwitch();
		
		navigate.setSpeeds(-180, 180, true, 6000);
		
		int count = 0;
		
		while(true){
			if(count == 2){
				navigate.stopMotors();
				break;
			}
			if(lsPoller.getDifferentialData() > 0.06){
				count++;
				Sound.beep();
			}
		}
		
		
		navigate.setSpeeds(150, 150, false, 6000);
		
		navigate.turnTo(Math.toRadians(13));
		
		preventTwitch();
		
		navigate.goStraight(150, 150, 5.9);	
		
		preventTwitch();
				
		odo.setX(0.0); odo.setY(0.0); odo.setTheta(90.0);
		Sound.beep();
		
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

package testingPackage;

import java.io.IOException;
import java.util.HashMap;
import soccerbot.USPoller;

import wifi.WifiConnection;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * Robonaldo instantiates all the threads and operations required at start-up of
 * the program. The initial parameters fed into the robot will be received in
 * this class and be passed along to the rest of the system.
 */
public class DataAcquisition7 {
	public static final TextLCD LCD = LocalEV3.get().getTextLCD();

	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));

	//US
	public static final Port usPortL = LocalEV3.get().getPort("S2");
	public static final Port usPortR = LocalEV3.get().getPort("S1");


	// MAIN METHOD
	public static void main(String[] args){			
		
		USPoller leftPoller = new USPoller(usPortL);
		USPoller rightPoller = new USPoller(usPortR);
		leftPoller.start(); rightPoller.start();

		while(true){
			LCD.drawString("left: "+leftPoller.getDistance(), 0, 4);
			LCD.drawString("Right: "+rightPoller.getDistance(), 0, 5);
			try{
				Thread.sleep(500);
			}catch(Exception e){}
		}	
	}

}

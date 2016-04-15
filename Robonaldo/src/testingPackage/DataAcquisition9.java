package testingPackage;
import java.io.IOException;
import java.util.HashMap;

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
 * Robonaldo instantiates all the threads and operations required at start-up of the program.
 * The initial parameters fed into the robot will be received in this class and be passed along to the rest of
 * the system. 
 */
public class DataAcquisition9 {
	public static final TextLCD LCD = LocalEV3.get().getTextLCD();
	
	private static Object lock;
	
	private static final String SERVER_IP = "192.168.10.124";//"localhost";
	private static final int TEAM_NUMBER = 14;
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	
	//US
	public static final Port usPortL = LocalEV3.get().getPort("S2");
	public static final Port usPortR = LocalEV3.get().getPort("S1");
	
	//Colorsensor Middle
	public static final Port colorPortM = LocalEV3.get().getPort("S3");	
	
	//Colorsensor T
	public static final Port colorPortT = LocalEV3.get().getPort("S4");	
	
	public static double WIDTH = 19.10;
	public static double RADIUS = 2.072;
	
	
	//MAIN METHOD
	public static void main(String[] args){	
		// bring launching arm backward, ready to launch
		launchMotor.setAcceleration(6000); launchMotor.setSpeed(600);
		launchMotor.rotate(140, false);

		// bring loading mechanism up, rolling the ball in place for launch
		loadMotor.setSpeed(100);
		loadMotor.rotate(-110, false);
		
		// pause before launch
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// launch ball
		launchMotor.setAcceleration(20000000); launchMotor.setSpeed(600);
		launchMotor.rotate(-110, false); launchMotor.flt();
			
	}
	
}

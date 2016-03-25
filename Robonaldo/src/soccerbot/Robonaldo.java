package soccerbot;
import lejos.hardware.Button;
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
public class Robonaldo {
	public static final TextLCD t = LocalEV3.get().getTextLCD();
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	//public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	
	//US
	private static final Port usPortL = LocalEV3.get().getPort("S2");
	private static final Port usPortR = LocalEV3.get().getPort("S1");
	
	public static double WIDTH = 19.17;
	public static double RADIUS = 2.072;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 160, SMOOTH = 2000, DEFAULT = 6000; 
	
	//MAIN METHOD
	public static void main(String[] args){
	
		Odometer odo = new Odometer(leftMotor, rightMotor); 
		Screen lcd = new Screen(odo);
		odo.start();
		lcd.start();
		
		/*
		leftMotor.setSpeed(REGULAR); rightMotor.setSpeed(REGULAR);
		leftMotor.rotate(-convertAngle(RADIUS, WIDTH, Math.PI/2), true);
		rightMotor.rotate(convertAngle(RADIUS, WIDTH, Math.PI/2), false);
		*/
		
		Navigation nav = new Navigation(odo, leftMotor, rightMotor);
		
		/*
		USPoller leftPoller = new USPoller(usPortL);
		USPoller rightPoller = new USPoller(usPortR);

		leftPoller.start();
		rightPoller.start();*/
		
		//USLocalizer loco = new USLocalizer(odo, nav, leftPoller, rightPoller, leftMotor, rightMotor);
		//loco.localize();
		
//		nav.travelTo(0, 150);
//		nav.travelTo(150, 150);
		nav.travelTo(150, 0);
//		nav.travelTo(0, 0);
		
	
		
	}
	
	
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	public static EV3LargeRegulatedMotor[] getMotors(){
		EV3LargeRegulatedMotor[] m = {leftMotor, rightMotor};
		return m;
	}
	
}

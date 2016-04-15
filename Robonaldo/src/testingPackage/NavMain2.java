package testingPackage;
import soccerbot.Odometer;
import soccerbot.USPoller;
import soccerbot.LSPoller;
import soccerbot.Screen;
import soccerbot.Navigation;
import soccerbot.USLocalizer;
import soccerbot.LightLocalizer;
import java.io.IOException;
import java.util.HashMap;
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
public class NavMain2{
	public static final TextLCD LCD = LocalEV3.get().getTextLCD();
	
	private static Object lock;
	
	// WIFI
	private static final String SERVER_IP = "192.168.10.124"; //"localhost";
	private static final int TEAM_NUMBER = 14;
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	
	//US
	public static final Port usPortL = LocalEV3.get().getPort("S2");
	public static final Port usPortR = LocalEV3.get().getPort("S1");
	
	//Colorsensor Middle
	public static final Port colorPortM = LocalEV3.get().getPort("S3");	
	
	//Colorsensor T
	public static final Port colorPortT = LocalEV3.get().getPort("S4");	
	
	public static double WIDTH = 19.08;
	public static double RADIUS = 2.072;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 200, SMOOTH = 500, DEFAULT = 6000; 
	
	public static int BALL_LLX = 6, BALL_LLY = 3, BALL_URX = 7, BALL_URY = 4;
	public static int shootingX = 0, shootingY = 6;
	
	//MAIN METHOD
	public static void main(String[] args){
		
		USPoller leftPoller = new USPoller(usPortL);
		USPoller rightPoller = new USPoller(usPortR);
		LSPoller leftCS = new LSPoller(colorPortT);
		LSPoller rightCS = new LSPoller(colorPortM);
		leftPoller.start(); rightPoller.start(); leftCS.start(); rightCS.start();
		
		Odometer odo = new Odometer(leftMotor, rightMotor); 
		Screen lcd = new Screen(odo);
		odo.start();
		lcd.start();
		
		Navigation navigate = new Navigation(odo,leftPoller, rightPoller, leftCS, rightCS, leftMotor, rightMotor);
		
		USLocalizer usLocalizer = new USLocalizer(odo, navigate, leftPoller, rightPoller);
		LightLocalizer lLocalizer = new LightLocalizer(odo, navigate, leftCS, rightCS, 1);
		
		usLocalizer.localize();
		lLocalizer.localize();
		
		
		navigate.travelTo(50/30.5,155/30.5,false, false);
		navigate.travelTo(150/30.5,165/30.5,false, false);
		navigate.travelTo(45/30.5,40/30.5,false, false);	
		navigate.travelTo(30/30.5,175/30.5 , false, false);
		navigate.travelTo(160/30.5, 30/30.5, false, false);
		navigate.setSpeeds(150, 150, false, 6000);
		navigate.turnTo(160, 0);
	}
	
	/**
	 * Convert absolute angle on place to degrees each wheel is required to turn based
	 * on the radius of the wheel to be rotated and the width of the robot.
	 * Through the angle and the width we can calculate the distance required to move using
	 * (width * angle) / 2.0
	 * 
	 * @param radius Radius of wheel being rotated in cm
	 * @param width Wheel-to-wheel distance measured in cm from the center of each wheel
	 * @param angle Absolute angle to be rotated in radians
	 * @return Degrees wheel is required to rotate
	 * @see convertDistance(dobule radius, double distance)
	 * 
	 */ 
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, width * angle / 2);
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
	 * Retrieves each motor object. Assumes only 2 motor objects on each side of the robot are connected. 
	 * 
	 * @return Array of size 2 representing each motor object starting with the left one.
	 */
	public static EV3LargeRegulatedMotor[] getMotors(){
		EV3LargeRegulatedMotor[] m = {leftMotor, rightMotor};
		return m;
	}
	
	
}
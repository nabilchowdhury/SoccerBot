package testingPackage;
import soccerbot.LSPoller;
import soccerbot.Odometer;
import soccerbot.Screen;
import soccerbot.Navigation;
import soccerbot.USPoller;
import soccerbot.LSPoller;
import soccerbot.USLocalizer;
import soccerbot.LightLocalizer;
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
public class LocalizationTest {
	public static final TextLCD t = LocalEV3.get().getTextLCD();
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	//public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	
	//US
	public static final Port usPortL = LocalEV3.get().getPort("S2");
	public static final Port usPortR = LocalEV3.get().getPort("S1");
	
	//Colorsensor Middle
	public static final Port colorPortM = LocalEV3.get().getPort("S3");	
	
	//Colorsensor T
	public static final Port colorPortT = LocalEV3.get().getPort("S4");		
	
	//Colorsensor T
	//public static final Port colorPortT = LocalEV3.get().getPort("S4");	
	
	public static double WIDTH = 19.05;
	public static double RADIUS = 2.072;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 140, SMOOTH = 500, DEFAULT = 6000; 
	
	//MAIN METHOD
	public static void main(String[] args) throws InterruptedException{
	
		Odometer odo = new Odometer(leftMotor, rightMotor); 
		Screen lcd = new Screen(odo);
		odo.start();
		lcd.start();
		
		USPoller leftPoller = new USPoller(usPortL);
		USPoller rightPoller = new USPoller(usPortR);
		LSPoller leftCS = new LSPoller(colorPortT);
		LSPoller rightCS = new LSPoller(colorPortM);
		leftPoller.start(); rightPoller.start(); leftCS.start(); rightCS.start();
		
		Navigation navigate = new Navigation(odo, leftPoller, rightPoller, leftCS, rightCS, leftMotor, rightMotor);
		
		USLocalizer usLocalizer = new USLocalizer(odo, navigate, leftPoller, rightPoller);
		LightLocalizer lLocalizer = new LightLocalizer(odo, navigate, leftCS, rightCS, 1);
		
		usLocalizer.localize();
		lLocalizer.localize();		
		
	}
}
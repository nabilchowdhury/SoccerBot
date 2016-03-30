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
	
	private static Object lock;
	
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
	
	public static double WIDTH = 19.05;
	public static double RADIUS = 2.072;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 200, SMOOTH = 500, DEFAULT = 6000; 
	
	//MAIN METHOD
	public static void main(String[] args){
		lock = new Object();
		Odometer odo = new Odometer(leftMotor, rightMotor); 
		Screen lcd = new Screen(odo);
		odo.start();
		lcd.start();
		
		USPoller leftPoller = new USPoller(usPortL);
		USPoller rightPoller = new USPoller(usPortR);
		LSPoller lsPoller = new LSPoller(colorPortM);
		//LSPoller correctionPoller = new LSPoller(colorPortT);
		leftPoller.start(); rightPoller.start(); lsPoller.start();	//correctionPoller.start();
		Navigation navigate = new Navigation(odo,leftPoller, rightPoller, leftMotor, rightMotor);
		
		USLocalizer usLocalizer = new USLocalizer(odo, navigate, leftPoller, rightPoller);
		LightLocalizer lLocalizer = new LightLocalizer(odo, navigate, lsPoller);
		
		usLocalizer.localize();
		lLocalizer.localize();
		
		//OdometryCorrection correct = new OdometryCorrection(odo, correctionPoller);
		//correct.start();
		
		
		navigate.goStraight(150, 150, 30.45*4);
		navigate.setSpeeds(200, 200, false, 6000);
		navigate.turnTo(Math.PI/2);
		navigate.goStraight(150, 150, 30.45*6.15);
		
		navigate.stopMotors();
		try{
			Thread.sleep(50);
		}catch(Exception e){}
		
		navigate.setSpeeds(300, 300, false, 6000);
		navigate.turnTo(Math.PI/2);
		navigate.goStraight(150, 150, -30.45*0.65);
		
		
		
		navigate.stopMotors();
		try{
			Thread.sleep(50);
		}catch(Exception e){}
		
		
		loadMotor.setSpeed(400);
		loadMotor.rotate(180, false);
		
		launchMotor.setAcceleration(6000); launchMotor.setSpeed(150);
		launchMotor.rotate(140, false);
		
		loadMotor.setAcceleration(500);
		loadMotor.setSpeed(160);
		loadMotor.rotate(-120, false);
		
		navigate.goStraight(150,150, 5);
		
		navigate.setSpeeds(150, 150, false, 6000);
		navigate.turnTo(3*30.45, 0.5*30.45);
		
		try{
			Thread.sleep(1500);
		}catch(Exception e){}
		
		
		launchMotor.setAcceleration(6000); launchMotor.setSpeed(600);
		launchMotor.rotate(-140, false); launchMotor.flt();
		
		
		
		
		
		
		//System.exit(0);
		
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

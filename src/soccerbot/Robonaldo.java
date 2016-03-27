package soccerbot;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
/**
 * Robonaldo instantiates all the threads and operations required at start-up of the program.
 * The initial parameters fed into the robot will be received in this class and be passed along to the rest of
 * the system. 
 */
public class Robonaldo {
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	private static final Port usPort1 = LocalEV3.get().getPort("S1");
	private static final Port usPort2 = LocalEV3.get().getPort("S2");
	private static final Port colorPort = LocalEV3.get().getPort("S3");
	
	public static double WIDTH = 18.2;
	public static double RADIUS = 2.05;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 160, SMOOTH = 2000, DEFAULT = 6000; 

	
	
	//MAIN METHOD
	public static void main(String[] args){
		
		Odometer odo = new Odometer(leftMotor, rightMotor);
		
		EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(usPort1);
		EV3UltrasonicSensor us2 = new EV3UltrasonicSensor(usPort2);
		
		SensorModes colorSensor = new EV3ColorSensor(colorPort);
		colorSensor.setCurrentMode("Red");
		SampleProvider colorValue = colorSensor.getMode("Red");			// colorValue provides samples from this instance
		float[] colorData = new float[colorValue.sampleSize()];
		
		
		LSPoller lsp = new LSPoller(colorSensor, colorData);
		USPoller usp1 = new USPoller(us1);
		USPoller usp2 = new USPoller(us2);
		
		Screen screen = new Screen(odo, usp1, usp2, lsp);
		odo.start();
		usp1.start();
		usp2.start();
		lsp.start();
		screen.start();

		leftMotor.setSpeed(150);
		rightMotor.setSpeed(150);
		leftMotor.forward();
		rightMotor.forward();
		
		while(true) {
			if (lsp.getDifferentialData() >= 0.17) Sound.beep();
		}
		
		// 13.68, 2.096
//		leftMotor.setSpeed(140); rightMotor.setSpeed(140);
//		
//		leftMotor.rotate(-convertDistance(RADIUS, 30.48), true);
//		rightMotor.rotate(-convertDistance(RADIUS, 30.48), false);
		
		
		/*
		for(int k=0; k<4; k++){
			//leftMotor.setAcceleration(SLOW);rightMotor.setAcceleration(SLOW);
			rightMotor.rotate(-convertDistance(RADIUS, 60.48), true);
			leftMotor.rotate(-convertDistance(RADIUS, 60.48), false);
			//rightMotor.rotate(-convertDistance(RADIUS, 60.48), false);
			
			//leftMotor.setAcceleration(DEFAULT);rightMotor.setAcceleration(DEFAULT);
			leftMotor.rotate(-convertAngle(RADIUS, WIDTH, Math.PI/2), true);
			rightMotor.rotate(convertAngle(RADIUS, WIDTH, Math.PI/2), false);

		}*/
		
	
		/*loadMotor.setSpeed(120);
		loadMotor.rotateTo(-110);
		loadMotor.stop();
		launchMotor.setAcceleration(2000000000);
		launchMotor.setSpeed(200);
		launchMotor.rotate(360);
		
		
		
		int button = Button.waitForAnyPress();
		while(button == Button.ID_ENTER){
			launchMotor.stop();
			loadMotor.setAcceleration(150);			
			loadMotor.rotateTo(110);
			loadMotor.setSpeed(70);
			loadMotor.rotateTo(-110);
			loadMotor.stop();
			
			try{
				Thread.sleep(2000);
			}catch(Exception e){};
			
			launchMotor.setSpeed(700);
			launchMotor.rotate(-300);
			button = Button.waitForAnyPress();
			launchMotor.rotateTo(300);
		}*/
		
		
		
		
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

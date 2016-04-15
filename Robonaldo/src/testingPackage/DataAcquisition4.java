package testingPackage;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
/**
 * Robonaldo instantiates all the threads and operations required at start-up of the program.
 * The initial parameters fed into the robot will be received in this class and be passed along to the rest of
 * the system. 
 */
public class DataAcquisition4 {
	
	public static final EV3LargeRegulatedMotor loadMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor launchMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
	
	// ROBOT WIDTH
	public static double WIDTH = 18.2;
	
	// WHEEL RADIUS
	public static double RADIUS = 2.072;
	
	private final static int FAST = 200, SLOW = 100, REGULAR = 160, SMOOTH = 6000, DEFAULT = 6000; 

	
	
	//MAIN METHOD
	public static void main(String[] args){
		
		
		leftMotor.setAcceleration(DEFAULT);
		rightMotor.setAcceleration(DEFAULT);
		leftMotor.setSpeed(120); rightMotor.setSpeed(120);
		
		// move robot straight 300 cm
		rightMotor.rotate(-convertDistance(RADIUS, 300), true);
		leftMotor.rotate(-convertDistance(RADIUS, 300), false);

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

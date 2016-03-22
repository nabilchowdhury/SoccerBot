package unitTests;

import soccerbot.Odometer;
import soccerbot.Robonaldo;
import org.junit.Test;
import static org.junit.Assert.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/*
 * Diamond Path
 */
public class TestOdometerPath2 {

	private static final double DEG_ERR = 1.00;
	private static final double XCM_ERR = 0.50;
	private static final double YCM_ERR = 0.50;
	private static final double WR = 2.096;
	EV3LargeRegulatedMotor[]  motors = Robonaldo.getMotors();
	EV3LargeRegulatedMotor leftMotor = motors[0], rightMotor = motors[1];
	Odometer o = new Odometer(leftMotor, rightMotor);
	 
	@Test
	public void testPath() {
		performPath();
		testX();
		testY();
		testHeading();
	}
	
	public void performPath() {
		leftMotor.setSpeed(150);
		rightMotor.setSpeed(150);
		leftMotor.rotate(30, true);
		rightMotor.rotate(-30, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(120, true);
		rightMotor.rotate(-120, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(30, true);
		rightMotor.rotate(-30, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(120, true);
		rightMotor.rotate(-120, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(30, true);
		rightMotor.rotate(-30, false);
	}
	
	
	public void testX() { assertTrue(o.getX() < XCM_ERR); }
	
	public void testY() { assertTrue(o.getX() < YCM_ERR); }
	
	public void testHeading() { assertTrue(Math.toDegrees(o.getTheta()) < DEG_ERR); }
	
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
}

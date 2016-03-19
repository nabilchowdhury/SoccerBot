package unitTests;

import soccerbot.Odometer;
import soccerbot.Robonaldo;
import org.junit.Test;
import static org.junit.Assert.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestYPos {

	private static final double YCM_ERR = 0.50;
	private static final double WR = 2.096;
	EV3LargeRegulatedMotor[]  motors = Robonaldo.getMotors();
	EV3LargeRegulatedMotor leftMotor = motors[0], rightMotor = motors[1];
	Odometer o = new Odometer(leftMotor, rightMotor);

	@Test
	public void testYPos() {
		leftMotor.setSpeed(150);
		rightMotor.setSpeed(150);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(180, true);
		rightMotor.rotate(-180, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);		
		assertTrue(o.getY() < YCM_ERR);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
}
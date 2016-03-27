package unitTests;

import soccerbot.Odometer;
import soccerbot.Robonaldo;
import org.junit.Test;
import static org.junit.Assert.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestXPos {

	private static final double XCM_ERR = 0.50;
	private static final double WR = 2.096;
	EV3LargeRegulatedMotor[]  motors = Robonaldo.getMotors();
	EV3LargeRegulatedMotor leftMotor = motors[0], rightMotor = motors[1];
	Odometer o = new Odometer(leftMotor, rightMotor);

	@Test
	public void testXPos() {
		leftMotor.setSpeed(150);
		rightMotor.setSpeed(150);
		leftMotor.rotate(90, true);
		rightMotor.rotate(-90, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);
		leftMotor.rotate(180, true);
		rightMotor.rotate(-180, false);
		leftMotor.rotate(convertDistance(WR, 30.5), true);
		rightMotor.rotate(convertDistance(WR, 30.5), false);		
		assertTrue(o.getY() < XCM_ERR);
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
}

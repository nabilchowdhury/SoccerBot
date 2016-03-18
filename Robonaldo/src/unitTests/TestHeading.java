package unitTests;

import soccerbot.Odometer;
import soccerbot.Robonaldo;
import org.junit.Test;
import static org.junit.Assert.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestHeading {

	private static final double DEG_ERR = 1.00;
	EV3LargeRegulatedMotor[]  motors = Robonaldo.getMotors();
	EV3LargeRegulatedMotor leftMotor = motors[0], rightMotor = motors[1];
	Odometer o = new Odometer(leftMotor, rightMotor);
	
	@Test
	public void testHeading() {
		leftMotor.setSpeed(150);
		rightMotor.setSpeed(150);
		leftMotor.rotate(360*4, true);
		rightMotor.rotate(-360 * 4, false);
		leftMotor.stop();
		rightMotor.stop();
		double thetaInDeg = Math.toDegrees(o.getTheta());
		assertFalse(thetaInDeg < 0);
		assertTrue((360.00 - thetaInDeg < DEG_ERR) || (thetaInDeg < DEG_ERR));
	}

}
	
package soccerbot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Robonaldo instantiates all the threads and operations required at start-up of the program.
 * The iinitial parameters fed into the robot will be received in this class and be passed along to the rest of
 * the system. 
 */
public class Robonaldo {
	
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	
	//MAIN METHOD
	public static void main(String[] args){

		Odometer odo = new Odometer(leftMotor, rightMotor);
		Screen screen = new Screen(odo);
		odo.start();
		screen.start();
		
		
		Navigation n = new Navigation(odo, leftMotor, rightMotor);
		
		n.travelTo(0, 15.24);
		n.travelTo(15.24, 0);
	}
	
	public static EV3LargeRegulatedMotor[] getMotors() {
		return new EV3LargeRegulatedMotor[] {leftMotor, rightMotor};
	}
}
